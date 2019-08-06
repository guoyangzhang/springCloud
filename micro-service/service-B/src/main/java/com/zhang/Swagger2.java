package com.zhang;

/**
 * @Author: Mr.ZHANG
 * @Date: 2018/12/12 0012 下午 4:45
 */

import com.zhang.config.WebMvcConfig;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Swagger2配置类
 * 在与spring boot集成时，放在与Application.java同级的目录下。
 * 通过@Configuration注解，让Spring来加载该类配置。
 * 再通过@EnableSwagger2注解来启用Swagger2。
 * 失败原因未知??????????????????????????????????????????????????????????
 */
@Configuration
@EnableSwagger2//声明Swagger的可用性，
public class Swagger2  {

    /**
     * 创建API应用
     * apiInfo() 增加API相关信息
     * 通过select()函数返回一个ApiSelectorBuilder实例,用来控制哪些接口暴露给Swagger来展现，
     * 本例采用指定扫描的包路径来定义指定要建立API的目录。
     *
     * @return
     */
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()           // 选择那些路径和api会生成document
//                .apis(RequestHandlerSelectors.basePackage("com.zhang")) //swagger扫描目录
//                .paths(PathSelectors.any())// 对所有路径进行监控
//                .build();
//    }
//
//    /**
//     * 创建该API的基本信息（这些基本信息会展现在文档页面中）
//     * 访问地址：http://项目实际地址/swagger-ui.html
//     *
//     * @return
//     */
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                .title("SpringCloud中使用Swagger2构建RESTful APIs")
//                .description("")
//                .termsOfServiceUrl("")
//                .contact("")
//                .version("")
//                .build();
//    }

//    方法2
//public class Swagger2 {
//    private String groupName;
//    private String basePackage;
//    private String title;
//    private String host;
//    private String desc;
//    private String serviceUrl;
//    private String version;
//
//    @Bean
//    public Docket createRestApi() {
//        List<Parameter> pars = new ArrayList<Parameter>() {
//            public static final long serialVersionUID = 1L;
//
//            {
//                this.add((new ParameterBuilder()).name("gsid").description("全局会话ID(必要时使用)").modelRef(new ModelRef("string")).parameterType("header").required(false).build());
//            }
//        };
//        return (new Docket(DocumentationType.SWAGGER_2)).groupName(this.groupName).apiInfo(this.apiInfo()).host(this.host).select().apis(RequestHandlerSelectors.basePackage(this.basePackage)).paths(PathSelectors.any()).build().globalOperationParameters(pars);
//    }
//
//    public ApiInfo apiInfo() {
//        return (new ApiInfoBuilder()).title(this.title).description(this.desc).termsOfServiceUrl(this.serviceUrl).version(this.version + LocalDateTime.now()).build();
//    }


//    方法3
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //为controller包路径
                .apis(RequestHandlerSelectors.basePackage("com.common.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    //构建 api文档的详细信息函数
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("Spring Boot使用 Swagger2 构建RestFul API")
                //创建人
                .contact(new Contact("小川", "http://localhost:8764/swagger-ui.html", "1598078574@qq.com"))
                //版本号
                .version("1.0")
                //描述
                .description("跑圈模块接口文档")
                .build();
    }
}




