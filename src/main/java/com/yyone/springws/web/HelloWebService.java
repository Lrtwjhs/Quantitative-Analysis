package com.yyone.springws.web;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService
public interface HelloWebService {
	@WebMethod
	public String sayHello(@WebParam(name = "username") String username);
}
