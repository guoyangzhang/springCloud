package com.lovnx;

import com.lovnx.config.CustomRule;
import com.netflix.loadbalancer.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class RibbonApplication {

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
//    @Bean
//    public IRule ribbonRule() {
//        return new RandomRule();//这里配置策略，和配置文件对应
//    }

	@Bean
	public IRule myRule() {
		// 指定策略：我们自定义的策略
		// 编写的动态切换策略的方法
		return new CustomRule();
	}

	public static void main(String[] args) {
		SpringApplication.run(RibbonApplication.class, args);
	}


//	public static void main(String[] args) {
//		ILoadBalancer balancer=new BaseLoadBalancer();
//
//		List<Server> servers = new ArrayList<Server>();
//		servers.add(new Server("http://localhost",7071));
//		servers.add(new Server("http://localhost",7072));
//		balancer.addServers(servers);
//
//		for(int i=0;i<10;i++) {
//			Server choosedServer = balancer.chooseServer(null);
//			System.out.println(choosedServer);
//		}
//	}


}
