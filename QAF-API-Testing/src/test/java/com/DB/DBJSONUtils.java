package com.DB;

import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.qmetry.qaf.automation.step.QAFTestStep;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class DBJSONUtils {
	
	private static Logger logger=Logger.getLogger(DBJSONUtils.class);
	static String result=null;
	
	public static String getSQLResultsForSelect(DBConnectionData connectionData,String Sql){
		java.sql.Connection connection=null;
		ResultSet resultSet=null;
		Statement statement=null;
		
		try {
			connection=getDBConnection(connectionData);
			statement=connection.createStatement();
			logger.info("[SQL ]"+Sql );
			resultSet=statement.executeQuery(Sql);
			result=convertToJSONForSelect(resultSet);
			logger.info(result);
			System.out.println(result);
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
		
		
		}
	
	public static String convertToJSONForSelect(ResultSet resultSet) throws Exception{
		
		JSONArray jsonArray=new JSONArray();
		int totalRows=0;
		while(resultSet.next()){
			int total_cols=resultSet.getMetaData().getColumnCount();
			JSONObject obj=new JSONObject();
			for(int i=0;i<total_cols;i++){
				obj.put(resultSet.getMetaData().getColumnLabel(i+1).toLowerCase(), resultSet.getObject(i+1));
			}
			totalRows++;
			jsonArray.put(obj);
			
		}
		JSONObject rootEle=new JSONObject();
		rootEle.put("rowData", jsonArray);
		rootEle.put("totalRows",totalRows);
		logger.info("Converted JSON [ "+rootEle.toString());
		return rootEle.toString();
		
	}
	
	public static void compareJSONs(String excpectedJsonFile,String actualJSONFile) throws FileNotFoundException, IOException, ParseException  {
			JSONParser parser=new JSONParser();
			
			org.json.simple.JSONObject expectedJson;
		
				expectedJson = (org.json.simple.JSONObject) parser.parse(new FileReader(excpectedJsonFile));
				String expectedJsonBody=expectedJson.toString();
				
				JSONAssert.assertEquals(expectedJsonBody, result, JSONCompareMode.STRICT);
				
				
			
			
			
			
			
		
}
	
	
	
public static Connection getDBConnection(DBConnectionData connectionData){
	java.sql.Connection connection=null;
	
	try {
		Class.forName(connectionData.getDriverClass()).newInstance();
		System.out.println(connectionData.toString());
		logger.info("Connecting to DB [ "+connectionData.getConnectionURL()+" using userId [ "+connectionData.getUserId());
		connection=DriverManager.getConnection(connectionData.getConnectionURL(), connectionData.getUserId(), connectionData.getPassword());
		
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return connection;
}





}
