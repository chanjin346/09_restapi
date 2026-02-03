package com.ohgiraffers.restapi.section03.valid;

/* 사용자 정의 예외 */
public class UserNotFoundException extends Exception {

	public UserNotFoundException(String msg) {
		super(msg);
	}
}