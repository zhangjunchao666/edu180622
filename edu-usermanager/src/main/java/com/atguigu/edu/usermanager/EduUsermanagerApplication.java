package com.atguigu.edu.usermanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.atguigu.edu.usermanager.mapper")
@ComponentScan(basePackages = "com.atguigu.edu")
public class EduUsermanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EduUsermanagerApplication.class, args);
	}
}
