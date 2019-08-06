package com.lovnx.config;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by Mr.zhang on 2019/8/2
 * @author Mr.zhang
 */
public class MyProbabilityRandomRule {
    private static Logger log = LoggerFactory.getLogger(MyProbabilityRandomRule.class);

    public MyProbabilityRandomRule() {
    }

    public Server choose(ILoadBalancer lb, Object key) {
        // 获取负载均衡器lb
        if (lb == null) {
            return null;
        }
        Boolean flag = false;
        Server server = null;
        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            // 获取可用服务列表
            List<Server> upList = lb.getReachableServers();
            // 获取所有服务列表
            List<Server> allList = lb.getAllServers();
            if (!CollectionUtils.isEmpty(upList)) {
                for (Server server1 : upList) {
                    if (server1.getHostPort().equals(String.valueOf(key))) {
                        server = server1;
                        flag = true;
                    }
                }
//                flag = false;
                if (flag == false) {
                    server = allList.get(0);
//                    //根据key获取端口
//                    String port = key.toString().substring(key.toString().indexOf(":")+1,key.toString().length());
//                    for(Server server1:allList){
//                        if(server1.getPort() == Integer.parseInt(port)){
//                            continue;
//                        }else{
//                            server = server1;
//                            break;
//                        }
//                    }

                }
            }

            int serverCount = upList.size();
            if (serverCount == 0) {
                return null;
            }
            if (server == null) {
                Thread.yield();
                continue;
            }
            if (server.isAlive()) {
                return (server);
            }
            server = null;
            Thread.yield();
        }
        return server;
    }
}
