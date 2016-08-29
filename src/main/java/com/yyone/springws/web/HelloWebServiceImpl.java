package com.yyone.springws.web;

import javax.jws.WebService;

@WebService(endpointInterface = "com.yyone.springws.web.HelloWebService")
public class HelloWebServiceImpl implements HelloWebService {

	@Override
	public String sayHello(String username) {
		return "Hello Kitty! and Hello " + username;
	}

}
