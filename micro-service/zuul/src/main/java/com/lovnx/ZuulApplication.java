package com.lovnx;

import com.lovnx.filter.FirstFilter;
import com.lovnx.filter.SecondFilter;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

//import com.lovnx.filter.AccessFilter;
import com.lovnx.filter.ErrorFilter;
//import com.lovnx.filter.RateLimitFilter;
import com.lovnx.filter.ResultFilter;
//import com.lovnx.filter.UuidFilter;
//import com.lovnx.filter.ValidateFilter;

@EnableZuulProxy
@SpringCloudApplication
public class ZuulApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(ZuulApplication.class).run(args);
    }

//	@Bean
//	public AccessFilter accessFilter() {
//		return new AccessFilter();
//	}
//
//	@Bean
//	public RateLimitFilter rateLimiterFilter() {
//		return new RateLimitFilter();
//	}

    @Bean
    public ResultFilter resultFilter() {
        return new ResultFilter();
    }

//    @Bean
//    public SecondFilter secondFilter() {
//        return new SecondFilter();
//    }
//
//	@Bean
//	public FirstFilter firstFilter() {
//		return new FirstFilter();
//	}

    @Bean
    public ErrorFilter errorFilter() {
        return new ErrorFilter();
    }

}
