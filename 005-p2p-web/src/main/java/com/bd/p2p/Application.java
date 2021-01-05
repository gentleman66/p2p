package com.bd.p2p;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubboConfiguration//开启dubbo配置
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }

}
