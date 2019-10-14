package com.zhang.study.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhang.study.base.Result;
import com.zhang.study.entity.TreeDemo;
import com.zhang.study.mapper.DragFileMapper;
import com.zhang.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;


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

    @Autowired
    private DragFileMapper dragFileMapper;


    /**
     * 秒传,断点续传
     * 根据md5查询是否已经存在上传完成的文件
     * 根据MD5查询是否存在分片文件
     *
     * @param identifier
     * @return
     * @throws IOException
     */
    @GetMapping("/upload")
    public Result upload(String identifier,
                         String relativePath,
                         String filename,
                         String totalChunks) {
        Map<String, Object> resultMap = new HashMap<>();
        File file = null;
        resultMap.put("needMerge", false);
        resultMap.put("skipUpload", false);
        //校验文件是否上传
        file = new File(saveFilePath + relativePath, filename);
        if (file.exists()) {
            if (identifier.equals(MD5Utils(file))) {
                resultMap.put("skipUpload", true);
                return Result.ok(resultMap);
            }
        }
        file = new File(checkMD5Path + identifier);
        //校验文件分片大于1,是否存在分片
        if (Integer.parseInt(totalChunks) > 1 && file.exists()) {
            int count = checkFileMd5(identifier);
            List<Integer> list = new ArrayList<>();
            if (count > 0) {
                for (int i = 1; i <= count; i++) {
                    list.add(i);
                }
                resultMap.put("chunkList", list);
            }
        }
        return Result.ok(resultMap);

    }


    /**
     * @param file       文件流
     * @param identifier MD5 校验码
     * @return 结果集
     * @throws IOException
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result simpleUpload(MultipartFile file,
                               String identifier,
                               String relativePath,
                               String filename,
                               String totalChunks,
                               String chunkNumber) throws IOException {
        BufferedInputStream inputStream = null;
        OutputStream outs = null;
        BufferedOutputStream bouts = null;
        Map<String, Object> resultMap = new HashMap<>();
        System.out.println(identifier);
        // 只有一个分片直传
        try {
            relativePath = "/" + relativePath;
            if (totalChunks.equals("1")) {
                File dirFile = new File(saveFilePath + relativePath.substring(0, relativePath.lastIndexOf("/")));
                if (!dirFile.exists()) {
                    dirFile.mkdirs();
                }

                //以读写的方式打开目标文件
                inputStream = new BufferedInputStream(file.getInputStream());
                outs = new FileOutputStream(new File(saveFilePath + relativePath.substring(0, relativePath.lastIndexOf("/")), filename));
                bouts = new BufferedOutputStream(outs);
                byte[] buf = new byte[8192];
                int length = 0;
                while ((length = inputStream.read(buf)) != -1) {
                    bouts.write(buf, 0, length);
                }

                dealFileData(relativePath, filename);


            } else {
                return uploadChunk(file, identifier, relativePath, filename, totalChunks, chunkNumber, inputStream, outs, bouts, resultMap);
            }
            // 刷新此缓冲的输出流
            bouts.flush();
            outs.flush();
            inputStream.close();
            outs.close();
            bouts.close();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传失败");
        } finally {
            closeStream(inputStream, outs, bouts);
        }
        //返回提示信息
        resultMap.put("message", "上传成功");
        return Result.ok(resultMap);
    }


    public Result uploadChunk(MultipartFile file,
                              String identifier,
                              String filePath,
                              String filename,
                              String totalChunks,
                              String chunkNumber,
                              BufferedInputStream inputStream,
                              OutputStream outs,
                              BufferedOutputStream bouts,
                              Map<String, Object> resultMap) throws IOException {
        filename = filename + chunkNumber;
        String result = "";
        try {
            File dirFile = new File(checkMD5Path + identifier);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            //以读写的方式打开目标文件
            inputStream = new BufferedInputStream(file.getInputStream());
            outs = new FileOutputStream(new File(checkMD5Path + identifier, filename));
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

            if (chunkNumber.equals(totalChunks)) {
                resultMap.put("needMerge", true);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("message", "上传失败");
            return Result.error("resultMap");
        }
        resultMap.put("message", "上传成功");
        return Result.ok(resultMap);
    }


    public int checkFileMd5(@RequestBody String MD5) {
        int count = 0;
        File file = new File(checkMD5Path + MD5);
        if (file.exists()) {
            File[] listfile = file.listFiles();
            if (listfile.length > 0) {
                String fileName = listfile[0].toString();
                File lastFile = new File(fileName.substring(0, fileName.length() - 1) + String.valueOf(listfile.length));
                if (lastFile.isFile()) {
                    lastFile.delete();
                }
                count = listfile.length - 1;
            }
        } else {
            file.mkdir();
        }
        return count;
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
        Boolean check = true;
        try {
            String filePath = treeDemo.getPathUrl();
            File file = new File(checkMD5Path + treeDemo.getGuid());
            if (file.exists()) {
                listfile = file.listFiles();
            }
            File outFile = new File(saveFilePath + filePath.substring(0, filePath.lastIndexOf("/")));
            if (!outFile.exists()) {
                outFile.mkdirs();
            }
            outs = new FileOutputStream(new File(saveFilePath + treeDemo.getPathUrl()));
            bouts = new BufferedOutputStream(outs);
            //开始合并文件，对应切片的二进制文件
            for (int i = 1; i <= listfile.length; i++) {
                //读取切片文件
                String pathUrl = checkMD5Path + treeDemo.getGuid() + "\\" + treeDemo.getName() + i;
                ins = new FileInputStream(pathUrl);
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
            //
            bouts.flush();// 这里一定要调用flush()方法
            outs.flush();
            outs.close();
            bouts.close();
            delList(treeDemo.getGuid());
            dealFileData(treeDemo.getPathUrl(), treeDemo.getName());
            System.out.println("合并完成");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传失败!");
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
        if (check) {
            return Result.ok("上传成功!");

        } else {
            return Result.error("上传失败!");

        }
    }

    /**
     * 设置文件流md5校验,并重新分片
     */
//    public Boolean saveDataSouce(String filePath) throws Exception {
//        InputStream is = null;
//        BufferedInputStream bis = null;
//        File file = new File(filePath);
//        String fileId = "";
//        Boolean check = true;
//        try {
//            fileId = MD5Utils.getFileMD5String(file);
//            //查询库中是否存在,存在则不需要录入
//            int count = fileSyncTransMapper.selectFileIdCount(fileId);
//            if (count == 0) {
//                is = new FileInputStream(file);
//                long fileSize = is.available();
//                // 分割大小500M
//                long splitSize = 500 * 1024 * 1024;
//                // 计算分割数量
//                int fileTotal = 0;
//                // 判断大小
//                if (fileSize > splitSize) {
//                    if (fileSize % splitSize == 0) {
//                        fileTotal = (int) (fileSize / splitSize);
//                    } else {
//                        fileTotal = (int) (fileSize / splitSize) + 1;
//                    }
//                } else {
//                    fileTotal = 1;
//                    splitSize = fileSize;
//                }
//                // 创建字节数组
//                byte[] bytes;
//                bis = new BufferedInputStream(is);
//                for (int i = 0; i < fileTotal; i++) {
//                    long availableSize = is.available();
//                    if (availableSize < splitSize) {
//                        bytes = new byte[Integer.parseInt(String.valueOf(availableSize).substring(i * (int) splitSize, (int) splitSize))];
//                    } else {
//                        bytes = new byte[(int) splitSize];
//                    }
//                    int len = bis.read(bytes);
//                    //循环保存3次，容错
////                    check = saveFileTransPart(fileId, bytes, i);
////                    重新分片文件
//
//                    if (!check) {
//                        break;
//                    }
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (null != is) {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return check;
//    }
    public void delList(String MD5) {
        File file = new File(checkMD5Path + MD5);
        if (file.exists()) {
            deleteDir(checkMD5Path + MD5);
        }
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

    public void closeStream(BufferedInputStream inputStream, OutputStream outs, BufferedOutputStream bouts) {
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
            e.printStackTrace();
        }
    }

    public String MD5Utils(File file) {
        String md5 = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
            BigInteger bigInt = new BigInteger(1, md.digest());
            md5 = bigInt.toString(16);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return md5;
    }

    public void dealFileData(String relativePath, String filename) {
        Map<String, String> map = new HashMap<>();
        List<String> stringList = Arrays.asList(relativePath.split("/"));
        TreeDemo treeDemo = new TreeDemo();
        treeDemo.setParentId("-1");
        treeDemo.setIsFile(false);
        treeDemo.setIsHidden(false);
        String pathUrl = "";
        for (int i = 0; i < stringList.size(); i++) {
            String path = stringList.get(i);
            if (StringUtils.isEmpty(path)) {
                continue;
            }
            String parentPath = pathUrl;
            String uuid = StringUtils.uuid32len();
            pathUrl = pathUrl + "/" + path;
            treeDemo.setPathUrl(pathUrl);
            TreeDemo treeDemo1 = dragFileMapper.queryTree(treeDemo);
            if (treeDemo1 != null) {
                map.put(pathUrl, treeDemo1.getGuid());
            } else {
                if (StringUtils.isEmpty(parentPath)) {
                    treeDemo.setParentId("-1");
                } else {
                    treeDemo.setParentId(map.get(parentPath));
                }
                treeDemo.setGuid(uuid);
                treeDemo.setName(path);
                // 存在后缀校验,不知道是文件还是文件夹
                if (filename.equals(path) && i == stringList.size() - 1 && filename.indexOf(".") > 0) {
                    treeDemo.setIsFile(true);
                    treeDemo.setIsHidden(true);
                }
                map.put(pathUrl, uuid);
                treeDemo.setGuid(uuid);
                dragFileMapper.insertSelective(treeDemo);
            }

        }
    }
}