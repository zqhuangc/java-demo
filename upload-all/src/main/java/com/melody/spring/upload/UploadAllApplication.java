package com.melody.spring.upload;

import com.melody.spring.upload.resolver.UploadResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.web.multipart.MultipartResolver;

@SpringBootApplication
public class UploadAllApplication {

    public static void main(String[] args) {
        SpringApplication.run(UploadAllApplication.class, args);
    }

    @Bean
    @Primary
    public MultipartResolver multipartResolver(){
        UploadResolver uploadResolver = new UploadResolver();
        uploadResolver.setDefaultEncoding("UTF-8");
        uploadResolver.setMaxUploadSize(99199023255552L);
        return uploadResolver;
    }
}
