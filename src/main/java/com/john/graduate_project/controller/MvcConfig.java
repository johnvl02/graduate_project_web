package com.john.graduate_project.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;


@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path UploadDir = Paths.get("./src/main/resources/static/cars-photo");
        String UploadPath = UploadDir.toFile().getAbsolutePath();
        registry.addResourceHandler("/src/main/resources/static/cars-photo/**").addResourceLocations("file:/" + UploadPath + "/");

    }
}
