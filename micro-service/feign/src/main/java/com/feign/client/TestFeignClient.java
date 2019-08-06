package com.feign.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Mr.zhang
 */
@FeignClient(name = "SERVICE-B")
public interface TestFeignClient {

    /**
     * @param a
     * @param b
     * @return
     */
    @RequestMapping("/add")
    String add(@RequestParam("a") Integer a, @RequestParam("b") Integer b);
}