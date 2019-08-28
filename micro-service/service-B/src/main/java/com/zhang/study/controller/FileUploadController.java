package com.zhang.study.controller;

import com.zhang.study.base.Result;
import com.zhang.study.entity.TreeDemo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 实现文件断点续传的功能
 * Created by Mr.zhang on 2019/8/14
 */
@CrossOrigin
@RestController
@RequestMapping("/fileUpload")
public class FileUploadController {

    @Value("${checkMD5Path}")
    private String checkMD5Path;
    @Value("${saveFilePath}")
    private String saveFilePath;

    /**
     * 简单的单文件上传
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Map<String, String> upload(@RequestParam(value = "file", required = false) MultipartFile file, String guid, String chunkSize, String fileName) throws IOException {
        guid = URLDecoder.decode(guid, "UTF-8");

        BufferedInputStream inputStream = null;
        OutputStream outs = null;
        BufferedOutputStream bouts = null;
        try {
            File dirFile = new File(checkMD5Path + guid, fileName + "_" + chunkSize);
            //以读写的方式打开目标文件
            inputStream = new BufferedInputStream(file.getInputStream());
            outs = new FileOutputStream(dirFile);
            bouts = new BufferedOutputStream(outs);
            byte[] buf = new byte[8192];
            int length = 0;
            while ((length = inputStream.read(buf)) != -1) {
                bouts.write(buf, 0, length);
            }
            // 刷新此缓冲的输出流
            bouts.flush();
            outs.flush();
            inputStream.close();
            outs.close();
            bouts.close();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        } finally {
        }
        try {
            if (null != inputStream) {
                inputStream.close();
            }
            if (null != outs) {
                outs.close();
            }
            if (null != bouts) {
                bouts.close();
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }

        Map<String, String> map = new HashMap<String, String>();
        String result = "";
        int res = -1;
        //返回提示信息
        map.put("result", result);
        return map;
    }


    /**
     * 直接上传,不需要分片
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/uploadNoChunk", method = RequestMethod.POST)
    public Map<String, String> uploadNoChunk(@RequestParam(value = "file", required = false) MultipartFile file, String guid, String filePath) throws IOException {
        String result = "";
        guid = URLDecoder.decode(guid, "UTF-8");
        BufferedInputStream inputStream = null;
        OutputStream outs = null;
        BufferedOutputStream bouts = null;
        try {
            File file1 = new File(saveFilePath + filePath.substring(0,filePath.lastIndexOf("/")));
            if(!file1.exists()){
                file1.mkdirs();
            }
            File dirFile = new File(saveFilePath + filePath.substring(0,filePath.lastIndexOf("/")), file.getOriginalFilename());
            //以读写的方式打开目标文件
            inputStream = new BufferedInputStream(file.getInputStream());
            outs = new FileOutputStream(dirFile);
            bouts = new BufferedOutputStream(outs);
            byte[] buf = new byte[8192];
            int length = 0;
            while ((length = inputStream.read(buf)) != -1) {
                bouts.write(buf, 0, length);
            }
            // 刷新此缓冲的输出流
            bouts.flush();
            outs.flush();
            inputStream.close();
            outs.close();
            bouts.close();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        } finally {
        }
        try {
            if (null != inputStream) {
                inputStream.close();
            }
            if (null != outs) {
                outs.close();
            }
            if (null != bouts) {
                bouts.close();
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }

        Map<String, String> map = new HashMap<String, String>();

        int res = -1;
        //返回提示信息
        map.put("result", result);
        return map;
    }


    /**
     * 校验判断文件是否上传
     *
     * @return
     */
    @RequestMapping(value = "checkFileMd5", method = RequestMethod.POST)
    @ResponseBody
    public Result checkFileMd5(@RequestBody TreeDemo treeDemo) throws IOException {

        File file = new File(checkMD5Path + treeDemo.getGuid());
        if (file.exists()) {
            File[] listfile = file.listFiles();
            if (listfile.length > 0) {
                File file1 = new File(listfile[listfile.length - 1].toString());
                if (file1.isFile()) {
                    file1.delete();
                }
                return Result.ok(listfile.length - 1);
            } else {
                return Result.ok(listfile.length);
            }
        } else {
            file.mkdir();
            return Result.ok("0");
        }
    }


    /**
     * 合并文件
     */
    @RequestMapping(value = "mergeFile", method = RequestMethod.POST)
    @ResponseBody
    public Result merge(@RequestBody TreeDemo treeDemo) {
        InputStream ins = null;
        BufferedInputStream bins = null;
        OutputStream outs = null;
        BufferedOutputStream bouts = null;
        File[] listfile = null;

        try {
            File file = new File(checkMD5Path + treeDemo.getGuid());
            if (file.exists()) {
                listfile = file.listFiles();
            }
            File file1 = new File(saveFilePath + treeDemo.getPathUrl().substring(0, treeDemo.getPathUrl().lastIndexOf("/")));
            if (!file1.exists()) {
                file1.mkdirs();
            }
            //申明随机读取文件RandomAccessFile
            outs = new FileOutputStream(new File(saveFilePath + treeDemo.getPathUrl()));
            bouts = new BufferedOutputStream(outs);
            //开始合并文件，对应切片的二进制文件
            for (int i = 0; i < listfile.length; i++) {
                //读取切片文件
                ins = new FileInputStream(checkMD5Path + treeDemo.getGuid() + "\\" + treeDemo.getName() + "_" + i * 5 * 1024 * 1024);
                bins = new BufferedInputStream(ins);
                byte[] buff = new byte[8192];
                int n = 0;
                //先读后写
                while ((n = bins.read(buff)) != -1) {
                    bouts.write(buff, 0, n);
                }
                ins.close();
                bins.close();
            }
            bouts.flush();// 这里一定要调用flush()方法
            outs.flush();
//            ins.close();
//            bins.close();
            outs.close();
            bouts.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != ins) {
                    ins.close();
                }
                if (null != bins) {
                    bins.close();
                }
                if (null != bouts) {
                    bouts.close();
                }
                if (null != outs) {
                    outs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Result.ok("上传成功!");
    }


    /**
     * 删除缓存文件
     * 将guid 设置成文件唯一性的id
     *
     * @param treeDemoList
     */
    @RequestMapping(value = "delList", method = RequestMethod.POST)
    @ResponseBody
    public Result delList(@RequestBody List<TreeDemo> treeDemoList) {
        if (!CollectionUtils.isEmpty(treeDemoList)) {
            for (TreeDemo treeDemo : treeDemoList) {
                deleteDir(checkMD5Path + treeDemo.getGuid());
            }
        }
        return Result.ok("删除成功");
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

}
