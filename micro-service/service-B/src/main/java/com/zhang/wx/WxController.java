package com.zhang.wx;

import com.alibaba.fastjson.JSONObject;
import com.zhang.study.service.TestService;
import com.zhang.wx.entity.checkSign;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;


/**
 * @Author: Mr.ZHANG
 * @Date: 2019/10/12 上午 09:45
 */
@RestController
@RequestMapping("wx")
public class WxController {
    @Resource
    private TestService service;

    @Value("${appid}")
    private String appid;
    @Value("${AppSecret}")
    private String AppSecret;


    /**
     * 打开开发者模式签名认证
     *
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/service", method = RequestMethod.GET)
    public JSONObject defaultView(HttpServletRequest request, String grant_type, String timestamp, String nonce, String echostr, String token) {
        String params = "grant_type=" + "client_credential" + "&secret=" + AppSecret +
                "&appid=" + appid;
        //发送GET请求
        String result = doGet("https://api.weixin.qq.com/cgi-bin/token" + "?" + params);
        // 解析相应内容（转换成json对象）
        JSONObject jsonObject = JSONObject.parseObject(result);
        return jsonObject;
    }

    public static String doGet(String url) {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String result = "";
        try {
            // 通过址默认配置创建一个httpClient实例
            httpClient = HttpClients.createDefault();
            // 创建httpGet远程连接实例
            HttpGet httpGet = new HttpGet(url);
            // 设置请求头信息，鉴权
            httpGet.setHeader("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 设置配置请求参数
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(35000)// 连接主机服务超时时间
                    .setConnectionRequestTimeout(35000)// 请求超时时间
                    .setSocketTimeout(60000)// 数据读取超时时间
                    .build();
            // 为httpGet实例设置配置
            httpGet.setConfig(requestConfig);
            // 执行get请求得到返回对象
            response = httpClient.execute(httpGet);
            // 通过返回对象获取返回数据
            HttpEntity entity = response.getEntity();
            // 通过EntityUtils中的toString方法将结果转换为字符串
            result = EntityUtils.toString(entity);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != response) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    /**
     * 第三方回复公众平台
     *
     * @param
     */
    @RequestMapping(value = "/checkToken", method = RequestMethod.GET)
    public void checkToken(HttpServletRequest request,
                           String timestamp,
                           String nonce,
                           String signature,
                           HttpServletResponse response) throws Exception {
//        SHA1.getSHA1();
        // 需要加密的明文
        String encodingAesKey = "B3kJhv5ZwGXgCxdNCTKwFxwJXFKf8dSZlEL1Cy1K9at";
        String token = "7213241";
//        String sign = DigestUtils.shaHex(value);
        PrintWriter writer = response.getWriter();
        if (checkSign.checkSignature(signature, timestamp, nonce)) {
            writer.println(signature);
        }
    }
}
