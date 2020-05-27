package com.DB;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestDB {
	
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException {
		DBConnectionData connectionData=new DBConnectionData();
		DBJSONUtils dbjsonUtils=new DBJSONUtils();
		connectionData.setConnectionURL("jdbc:mysql://127.0.0.1:3306/company");
		connectionData.setDriverClass("com.mysql.cj.jdbc.Driver");
		connectionData.setUserId("root");
		connectionData.setPassword("hiddu@24");
		
		
	
		String actualResult=dbjsonUtils.getSQLResultsForSelect(connectionData, "select * from users.user;");
		dbjsonUtils.compareJSONs("C:\\Users\\Hidayath\\Desktop\\SelectUsers_baselineResponse.json", actualResult);
		
	}
	
		
		
		
	

}
