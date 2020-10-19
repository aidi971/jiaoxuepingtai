package com.langheng.modules.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName SwaggerConfig
 * @Description TODO
 * @Author xiaoxie
 * @Date 2019-12-17 16:32
 * @Version 1.0
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket sysApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("1.sys系统功能模块相关接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.langheng.modules.base.web"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket edApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("2.ed课程模块相关接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.langheng.modules.ed.web"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket assApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("3.ass课堂工具和辅助教学模块相关接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.langheng.modules.ass.web"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket msgApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("4.msg消息通知模块相关接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.langheng.modules.msg.web"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket sybApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("5.商业计划书相关接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.langheng.modules.bussiness.web"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public Docket teachApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("6.teach配套教学相关接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.langheng.modules.teach.web"))
                .paths(PathSelectors.any())
                .build();
    }
    @Bean
    public Docket toolApi() {
        return (new Docket(DocumentationType.SWAGGER_2)).groupName("7.课堂工具相关接口").apiInfo(this.apiInfo()).select().apis(RequestHandlerSelectors.basePackage("com.langheng.modules.tool")).paths(PathSelectors.any()).build();
    }
    @Bean
    public Docket chatApi() {
        return (new Docket(DocumentationType.SWAGGER_2)).groupName("8.聊天相关接口").apiInfo(this.apiInfo()).select().apis(RequestHandlerSelectors.basePackage("com.langheng.modules.chat")).paths(PathSelectors.any()).build();
    }
    @Bean
    public Docket teamRoomApi() {
        return (new Docket(DocumentationType.SWAGGER_2)).groupName("9.团队空间相关接口").apiInfo(this.apiInfo()).select().apis(RequestHandlerSelectors.basePackage("com.langheng.modules.tr")).paths(PathSelectors.any()).build();
    }
    @Bean
    public Docket poolApi() {
        return (new Docket(DocumentationType.SWAGGER_2)).groupName("10.资源及试题模块相关接口").apiInfo(this.apiInfo()).select().apis(RequestHandlerSelectors.basePackage("com.langheng.modules.pool")).paths(PathSelectors.any()).build();
    }

    @Bean
    public Docket statApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("last.stat数据统计相关接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.jeesite.modules.stat.web"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("团队名", "朗恒集团院校开发团队", "my@my.com");
        return new ApiInfoBuilder()
                .title("朗恒教学平台后台接口文档")
                .description("朗恒教学平台后台接口文档")
                .contact(contact)   // 联系方式
                .version("1.1.0")  // 版本
                .build();
    }
}