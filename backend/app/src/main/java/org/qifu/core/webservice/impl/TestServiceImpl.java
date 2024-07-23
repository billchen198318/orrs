package org.qifu.core.webservice.impl;

import org.qifu.core.webservice.ITestService;
import org.springframework.stereotype.Component;

import jakarta.jws.WebService;

@WebService(
		serviceName = "testService"
		, targetNamespace = "http://test.webservice.core.qifu.org"
		, endpointInterface = "org.qifu.core.webservice.ITestService")
@Component(value = "testService")
public class TestServiceImpl implements ITestService {

	@Override
	public String play(String message) {
		return "test:" + message;
	}
	
}
