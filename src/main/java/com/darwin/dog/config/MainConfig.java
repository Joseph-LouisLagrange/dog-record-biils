package com.darwin.dog.config;

import com.darwin.dog.base.file.ResourceMapper;
import com.darwin.dog.po.sys.FilePlan;
import com.darwin.dog.repository.sys.FilePlanRepository;
import com.darwin.dog.service.impl.sys.LocalFileSaver;
import com.darwin.dog.service.inf.sys.FileSaver;
import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;

@Configuration
public class MainConfig {

    @Bean
    public ResourceMapper resourceMapper() {
        return new ResourceMapper() {
            @Override
            public String downloadMap(FilePlan filePlan) {
                return String.format("http://47.98.209.134:8080/file/download?ID=%d", filePlan.getID());
            }

            @Override
            public String previewMap(FilePlan filePlan) {
                return String.format("http://47.98.209.134:8080/file/preview?ID=%d", filePlan.getID());
            }
        };
    }

    @Bean
    public FileSaver fileSaver(@Autowired FilePlanRepository filePlanRepository, @Autowired ResourceMapper resourceMapper) throws IOException {
        LocalFileSaver fileSaver = new LocalFileSaver(filePlanRepository, "E:/files", resourceMapper);
        fileSaver.start();
        return fileSaver;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(31);
    }
}
