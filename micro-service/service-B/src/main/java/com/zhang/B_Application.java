package com.zhang;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = "com.zhang")
@MapperScan("com.zhang.*.mapper")
public class B_Application {

	public static void main(String[] args) {
		new SpringApplicationBuilder(B_Application.class).run(args);
	}

}
