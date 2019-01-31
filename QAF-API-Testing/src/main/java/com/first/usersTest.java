package com.first;
import org.testng.annotations.Test;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.WsStep;

public class usersTest {
int port=8081;
	
public usersTest(){
		System.out.println("Default port "+port);
	}
	
public usersTest(int port){
		this.port=port;
        System.out.println("Custom port "+port);
		
	}
	
	public static void main(String[] args) {
		String response= new usersTest().createUser();
	}
	public String createUser(){
		WsStep.userRequests("users.call");
		WsStep.sayValueAtJsonPath("fname", "$.firstName");
		
		String fName=ConfigurationManager.getBundle().getString("fname").toString();
		return fName;
		
	}

}
