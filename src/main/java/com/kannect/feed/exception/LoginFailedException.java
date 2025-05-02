package com.kannect.feed.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class LoginFailedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoginFailedException(){
		super();
	}
	
	public LoginFailedException(String message){
		super(message);
	}
}
