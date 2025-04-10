package com.unique.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class SwaggerConfig {

    //--------------------------------------------------
    // Bearer는 자동입력됨 :  토큰값
    //--------------------------------------------------
    @Bean
    public OpenAPI openAPI(){
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                //.bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Collections.singletonList(securityRequirement));
    }
}

//--------------------------------------------------
// Bearer 토큰값
//--------------------------------------------------
//@Configuration
//public class SwaggerConfig {
//
//    @Bean
//    public OpenAPI api() {
//        SecurityScheme apiKey = new SecurityScheme()
//                .type(SecurityScheme.Type.APIKEY)
//                .in(SecurityScheme.In.HEADER)
//                .name("Authorization");
//
//        SecurityRequirement securityRequirement = new SecurityRequirement()
//                .addList("Bearer Token");
//
//        return new OpenAPI()
//                .components(new Components().addSecuritySchemes("Bearer Token", apiKey))
//                .addSecurityItem(securityRequirement);
//    }
//}
