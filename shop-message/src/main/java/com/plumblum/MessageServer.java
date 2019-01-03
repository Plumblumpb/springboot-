package com.plumblum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
//@EnableEurekaClient
public class MessageServer {

	 public static void main(String[] args) {
		 SpringApplication.run(MessageServer.class, args);
	}
	
}
