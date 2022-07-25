package com.capgemini.sesp.ast.android.module.communication;

import com.skvader.rsp.cft.webservice_framework.WebServicePort;

import java.util.Arrays;
import java.util.List;

public class AndroidWebserviceClientBase {

	@SuppressWarnings("unchecked")
	protected <T> T call(String methodName, Class<T> returnType, Class<? extends WebServicePort> portClass, Object... parameters) throws Exception {
		//return (T) CommunicationHelper.callWebservice(portClass, methodName,returnType, parameters);
		CommunicationHelper communicationHelper = new CommunicationHelper();
		return (T) communicationHelper.callWebservice(portClass, methodName,returnType, parameters);
	}
	
	@SuppressWarnings("unchecked")
	protected <T> List<T> callList(String methodName, Class<T[]> returnType, Class<? extends WebServicePort> portClass, Object... parameters) throws Exception {
		//return (List<T>) Arrays.asList((T[])CommunicationHelper.callWebservice(portClass, methodName,returnType, parameters));
		CommunicationHelper communicationHelper = new CommunicationHelper();
		return Arrays.asList((T[]) communicationHelper.callWebservice(portClass, methodName, returnType, parameters));
	}
	
}
