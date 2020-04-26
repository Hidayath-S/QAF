package com.qmetry.qaf.example.test;

import java.util.Map;

import org.testng.annotations.Test;

import com.qmetry.qaf.automation.core.ConfigurationManager;
import com.qmetry.qaf.automation.step.WsStep;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider;

public class GetCountry {
	//@QAFDataProvider(key="getCountry.data")
	@Test()
	public void getCountry( ){
		
		WsStep.userRequests("groupkt.call");
		
		WsStep.responseShouldHaveStatusCode(200);
		WsStep.sayValueAtJsonPath("Country", "$.RestResponse.result[?(@.alpha2_code=='IN')].name");
		String countryName=ConfigurationManager.getBundle().getString("Country");
		System.out.println("countryName="+countryName);
		WsStep.userRequests("getAllCountry.call");
	}

//	
//	@Test()
//	//@QAFDataProvider(dataFile="C://Users//Hidayath//Desktop//POM.txt")
//	public void testFile(){
//		WsStep.userRequests("getAllCountry.call");
//		
//		
//		
//	}
}
