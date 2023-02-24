package com.deshin.springsecurityjwt.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String SWAGGER_TITLE = "Spring Security JWT REST API";


  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Spring Security JWT REST API")
        .description("Spring Security JWT REST API Document")
        .build();
  }


  @Bean
  public Docket authApi() {
    return new Docket(DocumentationType.OAS_30)
        .apiInfo(new ApiInfoBuilder()
            .title(SWAGGER_TITLE)
            .description("Authorize 에 'Bearer [jwt_token]' 형식으로 추가해서 사용하세요.")
            .build())
        .useDefaultResponseMessages(false)
        .securityContexts(List.of(securityContext()))
        .securitySchemes(List.of(new ApiKey(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER, "header")))
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.deshin.springsecurityjwt.controller"))
        .paths(PathSelectors.any())
        .build();
  }

  // AUTHORIZATION_HEADER 버튼 추가
  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(defaultAuth())
        .build();
  }

  // AUTHORIZATION_HEADER 버튼 추가
  private List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return List.of(new SecurityReference(AUTHORIZATION_HEADER, authorizationScopes));
  }



}
