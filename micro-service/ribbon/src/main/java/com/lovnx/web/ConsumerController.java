package com.lovnx.web;

import com.lovnx.config.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("test")
public class ConsumerController {

    @Autowired
    private RestTemplate restTemplate;

//    @Autowired
//    private CustomRule customRule;

    @Autowired
    private DiscoveryClient client;
    /**
     * @param a
     * @param b
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(@RequestParam Integer a, @RequestParam Integer b) {

//    	 this.loadBalancerClient.choose("ribbon");//随机访问策略
//       this.customRule.choose("ribbon");
//       this.customRule.choose("ribbon");

        Consts.serviceList.add("ribbon");//放入服务器
        Consts.ruleType.set(1);//设置策略类型
//        System.out.println("输出结果集:" + (a + b));
        return restTemplate.getForEntity("http://ribbon/test/update?a=" + a + "&b=" + b, String.class).getBody();
    }
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    @ResponseBody
    public String update(@RequestParam Integer a, @RequestParam Integer b) {
        ServiceInstance instance = client.getLocalServiceInstance();
        return "From ribbon, Result is " + String.valueOf(a + b) +"\nPort:"+instance.getPort();
    }
}