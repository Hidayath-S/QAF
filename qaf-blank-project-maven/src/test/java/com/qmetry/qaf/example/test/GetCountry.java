package com.qmetry.qaf.example.test;

import org.testng.annotations.Test;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.WsStep;

public class GetCountry {
	@Test()
	public void getCountry(){
		
		WsStep.userRequests("getAllCountry.call");
		WsStep.responseShouldHaveStatusCode(200);
		WsStep.sayValueAtJsonPath("Country", "$.RestResponse.result[?(@.alpha2_code=='IN')].name");
		//WsStep.responseShouldHaveValueAtJsonpath("India", "$.RestResponse.result[?(@.alpha2_code=='IN')].name");
		String countryName=ConfigurationManager.getBundle().getString("Country");
		System.out.println("countryName="+countryName);
	}

}
