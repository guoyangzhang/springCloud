package com.zhang.study.controller;


import com.google.common.collect.Lists;
import com.zhang.study.base.BaseController;
import com.zhang.study.base.Result;
import com.zhang.study.common.MsgConsts;
import com.zhang.study.entity.TreeDemo;
import com.zhang.study.mapper.DragFileMapper;
import com.zhang.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 拖拽文件
 *
 * @author Mr.zhang
 */
@CrossOrigin
@Controller
@RequestMapping("/dragFile")
public class DragFileController extends BaseController {


    @Autowired
    private DragFileMapper dragFileMapper;
    public Logger logger = LoggerFactory.getLogger(getClass());


    @RequestMapping(value = "/selectList", method = RequestMethod.POST)
    @ResponseBody
    public Result selectList(@RequestBody TreeDemo treeDemo, HttpServletRequest request) throws Exception {
        List<TreeDemo> list = dragFileMapper.selectList(treeDemo);
        List<TreeDemo> demoList = dealTree(list);
        if (CollectionUtils.isEmpty(demoList)) {
            return Result.error("文件不存在,请先上传");
        } else {
            return Result.ok(demoList);
        }

    }

    /**
     * 拖拽文件夹
     * 首先确认数据库是否存在该文件夹的内容,存在替换
     */

    @RequestMapping(value = "/dragTree", method = RequestMethod.POST)
    @ResponseBody
    public Result dragTree(@RequestBody List<TreeDemo> treeDemoList, HttpServletRequest request) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String rootName = "";
        Map<String, String> map = new HashMap<>();
        List<TreeDemo> demoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(treeDemoList)) {
            TreeDemo treeDemo = new TreeDemo();
            String pathUrl = treeDemoList.get(0).getPathUrl();
            rootName = pathUrl.substring(1, pathUrl.indexOf("/", 1));

//            清除数据库中已经存在的数据
            int count = dragFileMapper.selectCountRootName(rootName);
            if (count > 0) {
                dragFileMapper.deleteRootName(rootName);
            }
            String uuid = StringUtils.uuid32len();
            treeDemo.setGuid(uuid);
            treeDemo.setName(rootName);
            treeDemo.setIsHidden(false);
            treeDemo.setRootName(rootName);
            map.put("/" + rootName, uuid);
            treeDemo.setParentId("-1");
            demoList.add(treeDemo);
            treeList(map, treeDemoList, rootName, demoList);
            dragFileMapper.insertListSelective(demoList);
        }
        //处理数据成tree
        demoList = dealTree(demoList);
        //本地文件上传服务器
//        uploadFile(map, rootName);
        resultMap.put("demoList", demoList);
        resultMap.put("treeDemoMap", map);
        return Result.ok(resultMap);

    }

    public boolean delFile(File file) {
        if (!file.exists()) {
            return false;
        }

        if (file.isFile()) {
            return file.delete();
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
            }
            return file.delete();
        }
    }


    public List<TreeDemo> dealTree(List<TreeDemo> demoList) {
        List<TreeDemo> oneLeverList = new ArrayList<>();
        for (TreeDemo treeDemo : demoList) {
            if ("-1".equals(treeDemo.getParentId())) {
                oneLeverList.add(treeDemo);
            }
        }
        recursiveTree(oneLeverList, demoList);
        return demoList;
    }

    private void recursiveTree(List<TreeDemo> oneLeverList, List<TreeDemo> list) {
        for (TreeDemo oneLever : oneLeverList) {
            List<TreeDemo> childrenList = Lists.newArrayList();
            for (TreeDemo qualityCost : list) {
                if (qualityCost.getParentId().equals(oneLever.getGuid())) {
                    childrenList.add(qualityCost);
                }
            }
            if (null != childrenList || !childrenList.isEmpty()) {
                list.removeAll(childrenList);
                oneLever.setChildList(childrenList);
                recursiveTree(childrenList, list);
            }
        }
    }

    //tree
    // 数据库
    public void treeList(Map<String, String> map, List<TreeDemo> treeDemoList, String rootName, List<TreeDemo> demoList) {
        List<TreeDemo> demoList1 = demoList;
        List<TreeDemo> list = new ArrayList<>();
        for (TreeDemo treeDemo1 : treeDemoList) {
            if (treeDemo1.getIsFile()) {
                treeDemo1.setIsHidden(true);
            } else {
                treeDemo1.setIsHidden(false);
            }
            String uuid = StringUtils.uuid32len();
            String pathUrl = treeDemo1.getPathUrl();
            pathUrl = pathUrl.substring(0, pathUrl.lastIndexOf("/"));
            String parentName = pathUrl;
            if (StringUtils.isNotEmpty(map.get(parentName))) {
                map.put(treeDemo1.getPathUrl(), uuid);
                treeDemo1.setParentId(map.get(parentName));
                treeDemo1.setRootName(rootName);
                treeDemo1.setGuid(uuid);
                demoList1.add(treeDemo1);
            } else {
                list.add(treeDemo1);
            }
        }
        if (!CollectionUtils.isEmpty(list)) {
            treeList(map, list, rootName, demoList1);
        }
    }


    @RequestMapping(value = "/downLoad", method = RequestMethod.POST)
    @ResponseBody
    public Result exportBrandInfo(TreeDemo treeDemo, HttpServletRequest request) throws Exception {

        logger.info("执行下载");
        try {
            String response = download(treeDemo, request);
            return Result.ok(response, 0);
        } catch (Exception e) {
            return Result.error(MsgConsts.SYSTEM_ERROR_CODE, MsgConsts.SYSTEM_ERROR_MSG, null);
        }
    }

    public String download(TreeDemo treeDemo, HttpServletRequest request) throws Exception {
        String downLoadUrl = "";
        try {
        } catch (Exception e) {
            throw e;
        } finally {

        }
        return downLoadUrl;
    }


    /**
     * 新增tree节点数据
     *
     * @param treeDemo
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Result add(@RequestBody TreeDemo treeDemo, HttpServletRequest request) throws Exception {
        treeDemo.setGuid(StringUtils.uuid32len());
        dragFileMapper.insertSelective(treeDemo);
        return Result.ok(treeDemo);

    }


}
