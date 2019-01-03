package com.plumblum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;



//@EnableEurekaClient
@SpringBootApplication
public class MemberApp {

	public static void main(String[] args) {
		SpringApplication.run(MemberApp.class, args);
	}

}
