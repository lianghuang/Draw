package com.sectong.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger 配置文件
 *
 * @author jiekechoo
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("v1").select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/v1/**")).build().apiInfo(apiInfo());
    }

    @Bean
    public Docket api2() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("v2").select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/api/v2/**")).build().apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("shayne", "", "543306745@qq.com");
        ApiInfo apiInfo = new ApiInfo("你画我猜 API 手册", "此在线API手册为客户端用户提供开发参考", "1.0", "Terms of service", contact,
                "MIT", "https://opensource.org/licenses/MIT");
        return apiInfo;
    }

}
