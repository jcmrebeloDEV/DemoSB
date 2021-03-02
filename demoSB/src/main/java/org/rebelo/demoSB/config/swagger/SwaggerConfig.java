package org.rebelo.demoSB.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
//@ComponentScan("org.rebelo.demoSB.controladores")
public class SwaggerConfig {                                    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .select()                                  
          //.apis(RequestHandlerSelectors.any())
          .apis( RequestHandlerSelectors.basePackage("org.rebelo.demoSB.controladores"))
          .paths(PathSelectors.any())                          
          .build()
          .apiInfo(getApiInfo());                                           
    }
    
    private ApiInfo getApiInfo() {
    	
    	 ApiInfo apiInfo = new ApiInfoBuilder()
                 .title ("API de Teste")
                 .description ("Essa é uma API desenvolvida para testes.")
                 .license("Apache License Version 2.0")
                 .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                 .version("1.0.0")
                 .contact(new Contact("Rebelo","", "https://github.com/jcmrebeloDEV/DemoSB"))
                 .build();
    	 
    	 return apiInfo;
}
    
}