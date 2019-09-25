package com.zhang.study.controller;

import com.zhang.out.entity.FileSyncTransPart;
import com.zhang.out.mapper.FileSyncTransMapper;
import com.zhang.study.base.Result;
import com.zhang.study.entity.TreeDemo;
import com.zhang.utils.MD5Utils;
import com.zhang.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.*;


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

    @Resource
    private FileSyncTransMapper fileSyncTransMapper;

    /**
     * 简单的单文件上传
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result upload(@RequestParam(value = "file", required = false) MultipartFile file, String guid, String chunkSize, String fileName) throws IOException {
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
            e.printStackTrace();
            return Result.error("上传失败");

        } finally {
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
                return Result.error("上传失败");
            }
        }
        //返回提示信息
        return Result.ok("上传成功");
    }


    /**
     * 直接上传,不需要分片
     *
     * @param file
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/uploadNoChunk", method = RequestMethod.POST)
    public Result uploadNoChunk(@RequestParam(value = "file", required = false) MultipartFile file, String guid, String filePath, String fileName) throws IOException {
        String result = "";
        guid = URLDecoder.decode(guid, "UTF-8");
        BufferedInputStream inputStream = null;
        OutputStream outs = null;
        BufferedOutputStream bouts = null;
        try {
            File file1 = new File(saveFilePath + filePath.substring(0, filePath.lastIndexOf("/")));
            if (!file1.exists()) {
                file1.mkdirs();
            }
            File dirFile = new File(saveFilePath + filePath.substring(0, filePath.lastIndexOf("/")), fileName);
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
//            saveDataSouce(saveFilePath + filePath.substring(0, filePath.lastIndexOf("/")) + "/" + file.getOriginalFilename());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传失败");
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
            e.printStackTrace();
            return Result.error("上传失败");
        }
        return Result.ok("上传成功");
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
     * 在拆分存放
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
            File file = new File(checkMD5Path + treeDemo.getGuid());
            if (file.exists()) {
                listfile = file.listFiles();
            }
            File file1 = new File(saveFilePath + treeDemo.getPathUrl().substring(0, treeDemo.getPathUrl().lastIndexOf("/")));
            if (!file1.exists()) {
                file1.mkdirs();
            }
            outs = new FileOutputStream(new File(saveFilePath + treeDemo.getPathUrl()));
            bouts = new BufferedOutputStream(outs);
            //开始合并文件，对应切片的二进制文件
            for (int i = 0; i < listfile.length; i++) {
                if (i == 99) {
                    System.out.println(i);
                }
                //读取切片文件
                String pathUrl = checkMD5Path + treeDemo.getGuid() + "\\" + treeDemo.getName() + "_" + i;
                System.out.println(pathUrl);
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
            System.out.println("合并完成");
//-------------------------------------------------------------------------------------------------//
//            check = saveDataSouce(saveFilePath + treeDemo.getPathUrl());
//-------------------------------------------------------------------------------------------------//


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
    public Boolean saveDataSouce(String filePath) throws Exception {
        InputStream is = null;
        BufferedInputStream bis = null;
        File file = new File(filePath);
        String fileId = "";
        Boolean check = true;
        try {
            fileId = MD5Utils.getFileMD5String(file);
            //查询库中是否存在,存在则不需要录入
            int count = fileSyncTransMapper.selectFileIdCount(fileId);
            if (count == 0) {
                is = new FileInputStream(file);
                long fileSize = is.available();
                // 分割大小500M
                long splitSize = 500 * 1024 * 1024;
                // 计算分割数量
                int fileTotal = 0;
                // 判断大小
                if (fileSize > splitSize) {
                    if (fileSize % splitSize == 0) {
                        fileTotal = (int) (fileSize / splitSize);
                    } else {
                        fileTotal = (int) (fileSize / splitSize) + 1;
                    }
                } else {
                    fileTotal = 1;
                    splitSize = fileSize;
                }
                // 创建字节数组
                byte[] bytes;
                bis = new BufferedInputStream(is);
                for (int i = 0; i < fileTotal; i++) {
                    long availableSize = is.available();
                    if (availableSize < splitSize) {
                        bytes = new byte[Integer.parseInt(String.valueOf(availableSize).substring(i * (int) splitSize, (int) splitSize))];
                    } else {
                        bytes = new byte[(int) splitSize];
                    }
                    int len = bis.read(bytes);
                    //循环保存3次，容错
//                    check = saveFileTransPart(fileId, bytes, i);
//                    重新分片文件

                    if (!check) {
                        break;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return check;
    }

    private boolean saveFileTransPart(String fileId, byte[] bytes, int i) {
        FileSyncTransPart part = new FileSyncTransPart();
        part.setFileId(fileId);
        part.setPartFile(bytes);
        part.setPartSort(i);
        try {
            fileSyncTransMapper.saveFileTransPart(part);
        } catch (Exception e) {
            return false;
        }
        return true;
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
                File file = new File(checkMD5Path + treeDemo.getGuid());
                if (file.exists()) {
                    deleteDir(checkMD5Path + treeDemo.getGuid());
                }
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


    @RequestMapping(value = "tree", method = RequestMethod.GET)
    @ResponseBody
    public Result treadCallable() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Map<String, Future<String>> uploadMap = new HashMap<>();
        //存放异常的数据
        Map<String, String> resultMap = new HashMap<>();
        List<String> exceptionList = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            Callable callable1 = new CallableImpl(i);
            Future<String> future = executorService.submit(callable1);
            uploadMap.put(String.valueOf(i), future);
        }
        uploadMap.forEach((key, value) -> {
            Future<String> future = value;
            try {
                resultMap.put(key, future.get(1, TimeUnit.MINUTES));
            } catch (InterruptedException e) {
                exceptionList.add(key);
                e.printStackTrace();
            } catch (ExecutionException e) {
                exceptionList.add(key);
                e.printStackTrace();
            } catch (TimeoutException e) {
                exceptionList.add(key);
                e.printStackTrace();
            }

        });

        executorService.shutdown();
        //对异常数据重新请求三次
        if (CollectionUtils.isEmpty(exceptionList)) {
            System.out.println("多线程结束!");
            return Result.ok(resultMap);
        } else {
            return Result.error("失败");
        }


    }


    public class CallableImpl implements Callable {
        private int count;

        CallableImpl(int count) {
            this.count = count;
        }

        @Override
        public Object call() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String uuid = uuid();
            System.out.println("count>>>>>>>>>>>>>>>>>>>>>>>" + count);
            System.out.println("count>>>>>>>>>>>>>>>>>>>>>>>" + uuid);
            return uuid;
        }
    }

    public String uuid() {

        return StringUtils.uuid32len();
    }


}
