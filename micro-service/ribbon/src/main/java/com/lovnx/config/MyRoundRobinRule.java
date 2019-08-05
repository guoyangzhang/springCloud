package com.lovnx.config;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

/**
 * Created by Mr.zhang on 2019/8/2
 */
public class MyRoundRobinRule {
    private static Logger log = LoggerFactory.getLogger(MyRoundRobinRule.class);

    public MyRoundRobinRule() {
    }

    public Server choose(ILoadBalancer lb, Object key) {

        if (lb == null) {
            log.warn("no load balancer");
            return null;
        }
        Server server = null;
        int count = 0;
        while (server == null && count++ < 10) {
            List<Server> reachableServers = lb.getReachableServers();
            log.info("reachableServers:{}", reachableServers);
            List<Server> allServers = lb.getAllServers();
            log.info("allServers:{}", allServers);
            int upCount = reachableServers.size();
            int serverCount = allServers.size();

            if ((upCount == 0) || (serverCount == 0)) {
                log.warn("No up servers available from load balancer: " + lb);
                return null;
            }
            Random random = new Random();
//防止服务突然下线，集合里面保存的大于实际获取到的，进行去除多余的节点，等下次有节点进来的时候，进行增加
            for (String service : Consts.serviceList) {
                if (!reachableServers.contains(new Server(service))) {
                    Consts.serviceList.remove(service);
                }
            }
            log.info("Consts.serviceList:{}", Consts.serviceList);
            final List<String> weight = Consts.serviceList;
            //应该随机输的个数字概率基本上相等，集合的概率在存入是已经确定，可以由此来根据随机数去出节点，来对应的近似表示节点的倍权关系
            final int nextServerCyclicCounter = random.nextInt(weight.size());

            log.info("weight{}:nextServerCyclicCounter{}", weight.size(), nextServerCyclicCounter);
            for (Server se : reachableServers) {
                log.info(se.getId());
                if (se.getId().equals(weight.get(nextServerCyclicCounter))) {
                    server = se;
                    break;
                }
            }

            if (server == null) {
                /* Transient. */
                Thread.yield();
                continue;
            }
            if (server.isAlive() && (server.isReadyToServe())) {
                log.info("本次调用的服务为{},地址{}", server.getMetaInfo().getAppName(), server.getId());
                return (server);
            }

            // Next.
            server = null;
        }

        if (count >= 10) {
            log.warn("No available alive servers after 10 tries from load balancer: "
                    + lb);
        }
        return server;
    }
}
