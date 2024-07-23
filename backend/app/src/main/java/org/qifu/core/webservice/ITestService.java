package org.qifu.core.webservice;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(targetNamespace = "http://test.webservice.core.qifu.org")
public interface ITestService {
	
	@WebMethod
	String play(@WebParam(name = "message") String message);	
	
}
