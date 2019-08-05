package com.lovnx.config;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mr.zhang on 2019/8/2
 */
public class MyTagRandomRule {

    private AtomicInteger nextServerCyclicCounter;
    private static Logger log = LoggerFactory.getLogger(MyTagRandomRule.class);

    public MyTagRandomRule() {
        nextServerCyclicCounter = new AtomicInteger(0);
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
            List<Server> allServers = lb.getAllServers();
            int upCount = reachableServers.size();
            int serverCount = allServers.size();

            if ((upCount == 0) || (serverCount == 0)) {
                log.warn("No up servers available from load balancer: " + lb);
                return null;
            }
            List<String> tags = Consts.tagList.get(Consts.tag);
            int nextServerIndex = incrementAndGetModulo(tags.size());

            String service = tags.get(nextServerIndex);
            for (Server s : reachableServers) {
                if (service.equals(s.getId())) {
                    server = s;
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

    /**
     * @param modulo The modulo to bound the value of the counter.
     * @return The next value.
     */
    private int incrementAndGetModulo(int modulo) {
        for (; ; ) {
            int current = nextServerCyclicCounter.get();
            int next = (current + 1) % modulo;
            if (nextServerCyclicCounter.compareAndSet(current, next))
                return next;
        }
    }
}