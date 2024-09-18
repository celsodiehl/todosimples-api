package com.celsodiehl.todosimples.configs;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class Webconfig implements WebMvcConfigurer {

    public void addCorsMapping(CorsRegistry registry){
        registry.addMapping("/**");
    }
    
}
