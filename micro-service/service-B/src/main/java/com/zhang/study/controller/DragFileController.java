package com.zhang.study.controller;


import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zhang.study.base.BaseController;
import com.zhang.study.base.Result;
import com.zhang.study.common.MsgConsts;
import com.zhang.study.entity.TreeDemo;
import com.zhang.study.mapper.DragFileMapper;
import com.zhang.utils.StringUtils;
import org.apache.logging.log4j.core.util.KeyValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;


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
        List<TreeDemo> demoList = dealTree(list, "-1");
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
            treeDemo.setPathUrl("/" + rootName);
            map.put("/" + rootName, uuid);
            treeDemo.setParentId("-1");
            demoList.add(treeDemo);
            treeList(map, treeDemoList, rootName, demoList);
            dragFileMapper.insertListSelective(demoList);
        }
        //处理数据成tree
        demoList = dealTree(demoList, "-1");
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


    public List<TreeDemo> dealTree(List<TreeDemo> demoList, String parentId) {
        List<TreeDemo> oneLeverList = new ArrayList<>();
        for (TreeDemo treeDemo : demoList) {
            if (parentId.equals(treeDemo.getParentId())) {
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


    /**
     * 新增tree节点数据
     * <p>
     * 存在相同文件及文件夹怎么处理,数据库则怎么处理,文件怎么处理
     *
     * @param treeDemoList
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Result add(@RequestBody List<TreeDemo> treeDemoList, HttpServletRequest request) throws Exception {
        int count = 0;
        Map<String, String> stringMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(treeDemoList)) {
            for (TreeDemo treeDemo : treeDemoList) {
                stringMap.put(treeDemo.getParentPathUrl(), treeDemo.getParentId());
                // 清除数据库数据
                String pathUrl = "";
                //1.可能文件夹,文件夹删除
                treeDemo.getPathUrl();
                if (treeDemo.getPathUrl().indexOf("/") > -1) {
                    pathUrl = treeDemo.getParentPathUrl() + "/" + treeDemo.getPathUrl().substring(0, treeDemo.getPathUrl().indexOf("/"));
                } else {
                    pathUrl = treeDemo.getParentPathUrl() + "/" + treeDemo.getPathUrl();
                }
                TreeDemo treeDemo1 = new TreeDemo();
                treeDemo1.setRootName(treeDemo.getRootName());
                treeDemo1.setPathUrl(pathUrl);
                TreeDemo treeDemo2 = dragFileMapper.queryTree(treeDemo1);
                if (treeDemo2 != null) {  //清空原始数据
                    List<TreeDemo> oldList = dragFileMapper.getCategory(treeDemo2.getGuid());
                    oldList.add(treeDemo2);
                    if (!CollectionUtils.isEmpty(oldList)) {
                        dragFileMapper.deleteList(oldList);
                    }
                    File file = new File(pathUrl);
                    if (file.exists()) {
                        deleteDir(pathUrl);
                    }
                }

            }
//            录入数据
            List<TreeDemo> list = queryList(treeDemoList);
            if (!CollectionUtils.isEmpty(list)) {
                count = dragFileMapper.insertListSelective(list);
            }
        }
        if (count == 0) {
            return Result.error("添加失败");
        } else {
            return Result.ok("添加成功");
        }
    }

    public List<TreeDemo> queryList(List<TreeDemo> treeDemoList) {
        Map<String, String> map = new HashMap<>();
        List<TreeDemo> demoList = new ArrayList<>();
        String rootName = treeDemoList.get(0).getRootName();
        for (TreeDemo treeDemo : treeDemoList) {
            map.put(treeDemo.getParentPathUrl(), treeDemo.getParentId());
            String pathUrl = treeDemo.getPathUrl();
            List<String> stringList = Arrays.asList(pathUrl.split("/"));
            String param = treeDemo.getParentPathUrl();
            for (int i = 0; i < stringList.size(); i++) {
                if (StringUtils.isEmpty(stringList.get(i))) {
                    continue;
                }
                TreeDemo treeDemo1 = new TreeDemo();
                treeDemo1.setRootName(rootName);
                treeDemo1.setParentId(map.get(param));
                if (i == stringList.size() - 1) {
                    treeDemo1.setIsHidden(true);
                } else {
                    treeDemo1.setIsHidden(false);
                }
                param = param + "/" + stringList.get(i);
                String uuid = StringUtils.uuid32len();
                treeDemo1.setGuid(uuid);
                treeDemo1.setName(stringList.get(i));
                treeDemo1.setPathUrl(param);
                if (StringUtils.isEmpty(map.get(param))) {
                    map.put(param, uuid);
                    demoList.add(treeDemo1);
                }
            }
        }
        return demoList;
    }


    public static void deleteDir(String dirPath) {
        File file = new File(dirPath);
        if (file.isFile()) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            if (files == null) {
                file.delete();
            } else {
                for (int i = 0; i < files.length; i++) {
                    deleteDir(files[i].getAbsolutePath());
                }
                file.delete();
            }
        }
    }


    /**
     * 修改tree节点
     */
    @RequestMapping(value = "/updateTree", method = RequestMethod.POST)
    @ResponseBody
    public Result updateTree(@RequestBody TreeDemo treeDemo, HttpServletRequest request) {
        int index = dragFileMapper.updateByPrimaryKeySelective(treeDemo);
        if (index > 0) {
            return Result.ok("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    /**
     * 新增tree节点
     */
    @RequestMapping(value = "/addNode", method = RequestMethod.POST)
    @ResponseBody
    public Result addNode(@RequestBody TreeDemo treeDemo, HttpServletRequest request) {
        String uuid = StringUtils.uuid32len();
        treeDemo.setGuid(uuid);
        treeDemo.setIsFile(false);
        treeDemo.setIsHidden(false);
        int index = dragFileMapper.insertSelective(treeDemo);
        if (index > 0) {
            return Result.ok(uuid);
        } else {
            return Result.error("更新失败");
        }
    }

}
