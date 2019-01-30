package com.qmetry.qaf.example.test;

import org.testng.annotations.Test;
import com.qmetry.qaf.automation.step.WsStep;

public class GetCountry {
	@Test()
	public void getCountry(){
		
		WsStep.userRequests("groupkt.call");
		WsStep.responseShouldHaveStatusCode(200);
		
	}

}
