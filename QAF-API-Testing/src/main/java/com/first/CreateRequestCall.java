package com.first;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.databind.module.SimpleModule;

import io.swagger.models.HttpMethod;
import io.swagger.models.ModelImpl;
import io.swagger.models.Operation;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.Property;
import io.swagger.oas.inflector.examples.ExampleBuilder;
import io.swagger.oas.inflector.examples.models.Example;
import io.swagger.oas.inflector.processors.JsonNodeExampleSerializer;
import io.swagger.parser.SwaggerParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.OpenAPIV3Parser;
import net.minidev.json.JSONArray;

public class CreateRequestCall {

	public static void writePathsToExcel(String swgFilePath, String excelFilePath) throws Exception {
		Swagger swagger = new SwaggerParser().read(swgFilePath);
		// to get paths
		Set<String> allPathvalues = swagger.getPaths().keySet();
		Iterator<String> paths = allPathvalues.iterator();

		while (paths.hasNext()) {

			for (int i = 1; i <= allPathvalues.size(); i++) {

				File src = new File(excelFilePath);
				FileInputStream fileInputStream = new FileInputStream(src);
				XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
				XSSFSheet sheet = workbook.getSheetAt(0);
				String pathValue = paths.next();

				sheet.createRow(i).createCell(0).setCellValue(pathValue);

				FileOutputStream stream = new FileOutputStream(src);
				workbook.write(stream);
				workbook.close();
				// if(swagger.getPaths().get(paths.next()).getOperations().size()>0){
				// System.out.println("path is having more than one operation is
				// = "+paths.next());
				// }

			}
		}

	}

	public static void writeOPerationsAndMethodsToExcel(String swgFilePath, String excelFilePath) throws Exception {
		Swagger swagger = new SwaggerParser().read(swgFilePath);
		
		//swagger.getPath(path)
	
		
	}	
	
	public static void createRequestCall(String swaggerFilePath,String excelFilePath) throws Exception{
		Swagger swagger = new SwaggerParser().read(swaggerFilePath);
		String envURL="http://"+swagger.getHost()+swagger.getBasePath();
		System.out.println(envURL);
		String XMLContent="";
		
		FileInputStream fileInputStream=new FileInputStream(excelFilePath);
		XSSFWorkbook workbook=new XSSFWorkbook(fileInputStream);
		XSSFSheet sheet=workbook.getSheetAt(0);
		int rowCount=(sheet.getLastRowNum()+1);
		
		System.out.println("toralRows="+rowCount);
		for(int i=1;i<rowCount;i++){
			String baseURL=envURL+sheet.getRow(i).getCell(0).getStringCellValue();
			String path=sheet.getRow(i).getCell(0).getStringCellValue();
			//System.out.println(baseURL);
			String operationId=sheet.getRow(i).getCell(1).getStringCellValue();
			//System.out.println(operationId);
			String method=sheet.getRow(i).getCell(2).getStringCellValue();
			//System.out.println(method);
			if(method.equalsIgnoreCase("GET")){
				List<Parameter> params=swagger.getPath(path).getGet().getParameters();
				for(int j=0;j<params.size();j++){
					String IN=params.get(j).getIn();
					//System.out.println(IN);
					String name=params.get(j).getName();
					//System.out.println(name);
					//System.out.println(params.get(j).getRequired());
					if(IN.equalsIgnoreCase("path")){
						//System.out.println(envURL);
						baseURL=baseURL.replace("{"+name+"}", "${"+name+"}");
						//System.out.println(baseURL);
						XMLContent="<requests>\n <"+operationId +"> \n { \n 'baseUrl':'"+baseURL+ "', \n 'headers':{\n 'Content-Type': 'application/JSON' \n },\n 'method':"+method+ ",\n } \n </"+operationId+"> \n </requests>";
					}
					if(IN.equalsIgnoreCase("query")){
						if(params.size()>0){
							switch (j) {
							case 0:
								baseURL=baseURL+"?"+name+"="+"${"+name+"}";
								
								break;
							case 1:
							baseURL=baseURL+"&"+name+"="+"${"+name+"}";
							break;

							default:
								
								break;
							}
							
							
							
							//System.out.println(baseURL);
							XMLContent="<requests>\n <"+operationId +"> \n { \n 'baseUrl':'"+baseURL+ "', \n 'headers':{\n 'Content-Type': 'application/JSON' \n },\n 'method':"+method+ ",\n } \n </"+operationId+"> \n </requests>";	
						}
						
					}
				}
				
				
			}
//			if(method.equalsIgnoreCase("POST")){
//				List<Parameter> params=swagger.getPath(path).getPost().getParameters();
//				
//				for(int j=0;j<params.size();j++){
//					if(params.get(j).getIn().equalsIgnoreCase("body")){
//						XMLContent="<requests>\n <"+operationId +"> \n { \n 'baseUrl':'"+baseURL+ "', \n 'headers':{\n 'Content-Type': 'application/JSON' \n },\n 'method':"+method+ ",\n'body':'file:' \n } \n </"+operationId+"> \n </requests>";
//					}
//				}
//			}
//			if(method.equalsIgnoreCase("PUT")){
//				List<Parameter> params=swagger.getPath(path).getPut().getParameters();
//				for(int j=0;j<params.size();j++){
//					if(params.get(j).getIn().equalsIgnoreCase("body")){
//						XMLContent="<requests>\n <"+operationId +"> \n { \n 'baseUrl':'"+baseURL+ "', \n 'headers':{\n 'Content-Type': 'application/JSON' \n },\n 'method':"+method+ ",\n 'body':'file:' \n } \n </"+operationId+"> \n </requests>";
//					}
//				}
//			}
//			if(method.equalsIgnoreCase("DELETE")){
//				List<Parameter> params=swagger.getPath(path).getDelete().getParameters();
//				for(int j=0;j<params.size();j++){
//					if(params.get(j).getIn().equalsIgnoreCase("body")){
//						XMLContent="<requests>\n <"+operationId +"> \n { \n 'baseUrl':'"+baseURL+ "', \n 'headers':{\n 'Content-Type': 'application/JSON' \n },\n 'method':"+method+ ", \n'body':'file:' \n } \n </"+operationId+"> \n </requests>";
//					}
//				}
//			}
			
			
				if(method.equalsIgnoreCase("GET")){
					
					FileWriter writer= new FileWriter("resources\\SwgGeneratedFiles\\RequestCalls\\"+operationId+".xml");
					writer.write(XMLContent);
					writer.flush();
					writer.close();
					
				}
			
			//String XMLContent="<requests>\n <"+operationId +"> \n { \n 'baseUrl':'"+baseURL+ "', \n 'headers':{\n 'Content-Type': 'application/JSON' \n },\n 'method':"+method+ "\n } \n </"+operationId+"> \n </requests>";
			
		}
		

				
		
		
		
	}
	
	public static void getRefences(String swaggerFilePath,String excelFilePath) throws Exception{
		Swagger swagger = new SwaggerParser().read(swaggerFilePath);
		FileInputStream fileInputStream=new FileInputStream(excelFilePath);
		XSSFWorkbook workbook=new XSSFWorkbook(fileInputStream);
		XSSFSheet sheet=workbook.getSheetAt(0);
		int rowCount=(sheet.getLastRowNum()-sheet.getFirstRowNum())/3;
		for(int i=1;i<=rowCount;i++){
			String path=sheet.getRow(i).getCell(0).getStringCellValue();
			String operationId=sheet.getRow(i).getCell(1).getStringCellValue();
			String method=sheet.getRow(i).getCell(2).getStringCellValue();
			if(method.equalsIgnoreCase("GET")){
				List<Parameter> params=swagger.getPath(path).getGet().getParameters();
				for(int j=0;j<params.size();j++){
					String in=params.get(j).getIn();
					//System.out.println("IN = "+in);
					
				}
				
			}else if(method.equalsIgnoreCase("POST")){
				List<Parameter> params=swagger.getPath(path).getPost().getParameters();	
				for(int j=0;j<params.size();j++){
					
					String in=params.get(j).getIn();
					if(in.equalsIgnoreCase("body")){
						System.out.println("IN = "+in + "for "+operationId);	
					}
					
					
				}
			}else if (method.equalsIgnoreCase("PUT")){
				List<Parameter> params=swagger.getPath(path).getPut().getParameters();
				for(int j=0;j<params.size();j++){
					String in=params.get(j).getIn();
					if(in.equalsIgnoreCase("body")){
						System.out.println("IN = "+in + "for "+operationId);	
					}
					
				}
			}else if (method.equalsIgnoreCase("DELETE")){
				List<Parameter> params=swagger.getPath(path).getDelete().getParameters();
				for(int j=0;j<params.size();j++){
					String in=params.get(j).getIn();
					if(in.equalsIgnoreCase("body")){
						System.out.println("IN = "+in + "for "+operationId);	
					}
					
				}
			}
			
			
			
			
			
			
		}
		
		
		
	}

	public static void generateDefinitions(String swaggerFilePath) throws Exception{
		OpenAPI parser=new OpenAPIV3Parser().read(swaggerFilePath);
		
		
Map<String, Schema> definitions=parser.getComponents().getSchemas();
		
		for (Map.Entry<String, Schema> entry : definitions.entrySet()) {
			Schema model=definitions.get(entry.getKey());
			//System.out.println(entry.getKey());
			//System.out.println(model);
			Example example=ExampleBuilder.fromSchema(model, definitions);
			SimpleModule simpleModule= new SimpleModule().addSerializer(new JsonNodeExampleSerializer());
			io.swagger.v3.core.util.Json.mapper().registerModule(simpleModule);
			String jsonExample=io.swagger.v3.core.util.Json.pretty(example);
			//System.out.println("request for "+entry.getKey()+ " is ="+jsonExample);
			
			FileWriter writer= new FileWriter("resources\\SwgGeneratedFiles\\Definitions\\"+entry.getKey()+".json");
			writer.write(jsonExample);
			writer.flush();
			writer.close();
			
		}
	}

	public static void generateDefReferences(String swaggerFilePath,String excelFilePath) throws Exception{
		
		Swagger swagger = new SwaggerParser().read(swaggerFilePath);
		System.out.println(swagger.getPaths().get("/pet").getPost().getParameters().get(0));
		
				
				
				
					
				
				
			}
			
			
		
	public static void createQmetryScenario(String excelFilePath) throws Exception{
		FileInputStream fileInputStream=new FileInputStream(excelFilePath);
		XSSFWorkbook workbook=new XSSFWorkbook(fileInputStream);
		XSSFSheet sheet=workbook.getSheetAt(0);
		int rowCount=sheet.getLastRowNum()+1;
		for(int i=0;i<rowCount;i++){
			String operationId=sheet.getRow(i).getCell(1).getStringCellValue();
			String method=sheet.getRow(i).getCell(2).getStringCellValue();
			String scenarioContent="SCENARIO: To test happy path scenario for"+operationId+"  API\r\n" + 
					"META-DATA: {'desc':'This is an example of scenario using QAF-BDD','groups':['POSITIVE']}\r\n" + 
					"Given COMMENT: '"+operationId+ " micro service is up and running' \r\n" + 
					"When user requests '"+operationId+"'  \r\n" + 
					"Then response should have status code '200'\r\n" + 
					"END\r\n" + 
					"";
			if(method.equalsIgnoreCase("GET")){
				FileWriter writer= new FileWriter("scenarios\\generatedFromSwg\\"+operationId+".BDD");
				writer.write(scenarioContent);
				writer.flush();
				writer.close();
				
			}
			
		}
		
		
		
	}
		
		
	
	
	public static void main(String[] args) throws Exception {

		String excelSheetPath = "C:\\Users\\Hidayath\\Desktop\\AccountsInfo.xlsx";
		String swaggerFilePath = "C:\\Users\\Hidayath\\Desktop\\yaml\\petStore\\Swagger2.0\\petStoreV3.json";
		//writePathsToExcel(swaggerFilePath, excelSheetPath);
		//writeOPerationsAndMethodsToExcel(swaggerFilePath, excelSheetPath);
		generateDefinitions(swaggerFilePath);
		createRequestCall(swaggerFilePath,excelSheetPath);
		createQmetryScenario(excelSheetPath);
		//getRefences(swaggerFilePath,excelSheetPath);
		//generateDefReferences(swaggerFilePath, excelSheetPath);

		

	}

	
}
