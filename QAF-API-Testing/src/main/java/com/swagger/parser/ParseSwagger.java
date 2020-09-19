package com.swagger.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import io.swagger.models.HttpMethod;
import io.swagger.models.ModelImpl;
import io.swagger.models.Operation;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
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

public class ParseSwagger {
	

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

	

	public static void generateDefReferences(String swaggerFilePath,String excelFilePath) throws Exception{
		
		Swagger swagger = new SwaggerParser().read(swaggerFilePath);
		System.out.println(swagger.getPaths().get("/pet").getPost().getParameters().get(0));
		
				
				
				
					
				
				
			}
			
			
		
	

	public List<String> generateJSONPaths(String JSON) throws Exception{
				GenerateJSONPaths generateJSONPaths=new GenerateJSONPaths(JSON);
				JSONParser jsonParser=new JSONParser();
				JSONObject jsonObject=(JSONObject) jsonParser.parse(JSON);
				List<String> pathList=generateJSONPaths.readObject(jsonObject, "$");
		
		
		
		return pathList;
	}
		
	
	
	public static void main(String[] args) throws Exception {

		String excelSheetPath = "C:\\Users\\Hidayath\\Desktop\\AccountsInfo.xlsx";
		String swaggerFilePath = "C:\\Users\\Hidayath\\Desktop\\yaml\\petStore\\Swagger2.0\\petStoreV3.json";
		//writePathsToExcel(swaggerFilePath, excelSheetPath);
		//writeOPerationsAndMethodsToExcel(swaggerFilePath, excelSheetPath);
		//generateDefinitions(swaggerFilePath);
		//createRequestCall(swaggerFilePath,excelSheetPath);
		//createQmetryScenario(excelSheetPath);
		

	}

	
}
