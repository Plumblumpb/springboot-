package com.plumblum.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTest {

	private String userName;

	public static void main(String[] args) {
		UserTest userTest = new UserTest();
		userTest.setUserName("123456");
		System.out.println(userTest.getUserName());

	}

}
