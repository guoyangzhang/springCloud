package com.lovnx.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;

/**
 * 自定义策略
 * Created by Mr.zhang on 2019/8/1
 * <p>
 * 获取多个ip,只调用特定ip
 */

public class CustomRule extends AbstractLoadBalancerRule {

    @Autowired
    private DiscoveryClient client;

    private final MyProbabilityRandomRule myProbabilityRandomRule = new MyProbabilityRandomRule(); //随机策略
    private final MyRoundRobinRule myRoundRobinRule = new MyRoundRobinRule(); //轮询策略
    private final MyTagRandomRule myTagRandomRule = new MyTagRandomRule(); //服务标签策略
    private static Logger log = LoggerFactory.getLogger(CustomRule.class);

    public CustomRule() {
    }

    public CustomRule(ILoadBalancer lb) {
        this();
        setLoadBalancer(lb);
    }

    public Server choose(ILoadBalancer lb, Object key) {

        //查询eureka服务里面的服务application相同的,取第一个
        List<Server> reachableServers = lb.getReachableServers();
        System.out.println("获取当前所有服务-------------------");
        if (!CollectionUtils.isEmpty(reachableServers)) {
            for (Server server : reachableServers) {
                System.out.println("IP地址:" + server.getHostPort());
            }
            System.out.println("获取服务结束-------------------");
        } else {
            System.out.println("no service");
        }

        //获取当前服务IP
        ServiceInstance instance = client.getLocalServiceInstance();
        key = instance.getHost() + ":" + instance.getPort();
        System.out.println("获取当前服务>>>>>>>>>>>>>>>>>>>>>>>>>>  " +key);
        switch (Consts.ruleType.get()) {
            case 1:
                log.info("进入随机轮询中");
                return myProbabilityRandomRule.choose(lb, key);
            case 2:
                log.info("进入倍权轮询中");
                return myRoundRobinRule.choose(lb, key);
            case 3:   //服务标签策略
                log.info("进入tag轮询中");
                return myTagRandomRule.choose(lb, key);
            default:
                log.info("没有找到轮询机制,默认使用随机机制");
                return myProbabilityRandomRule.choose(lb, key);
        }
    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
    }

}





