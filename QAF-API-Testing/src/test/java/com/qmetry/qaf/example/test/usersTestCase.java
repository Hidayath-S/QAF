package com.qmetry.qaf.example.test;

import java.util.Map;
import sun.security.validator.*;

import org.testng.annotations.Test;
import com.qmetry.qaf.automation.step.WsStep;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider;
public class usersTestCase {
	@QAFDataProvider(key="CreateModifyWire.data")
	@Test()
	public void userDetailsTest(Map<String,Object> data){
		WsStep.userRequests("users.call");
		WsStep.responseShouldHaveValueAtJsonpath("invalid userId=1003and ab Kya kare batao=1003", "$.email");
	}

}
