package com.swagger.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.swagger.models.ArrayModel;
import io.swagger.models.HttpMethod;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.parser.SwaggerParser;

public class ExcelSheetOps {
	
	public List<String> getPaths(String swgFilePath, String excelFilePath) throws Exception {
		Swagger swagger = new SwaggerParser().read(swgFilePath);
		String path="";
		String operationId="";
		String method="";
		List finalList=new ArrayList<>();
		
		// to get paths
		
		Map<String, Path> allPathvalues = swagger.getPaths();
		
		FileInputStream fileInputStream=new FileInputStream(excelFilePath);
		XSSFWorkbook workbook=new XSSFWorkbook(fileInputStream);
		XSSFSheet sheet=workbook.getSheetAt(0);
		for(Map.Entry<String, Path> paths:allPathvalues.entrySet()){
			//System.out.println(paths.getKey());
			//System.out.println("operationMap for path "+paths.getKey()+"=="+paths.getValue().getOperationMap());
			Map<HttpMethod, Operation> operationMap=paths.getValue().getOperationMap();
			
			for(Map.Entry<HttpMethod, Operation> opsMap:operationMap.entrySet()){
					path=paths.getKey();
					operationId=opsMap.getValue().getOperationId();
					method=opsMap.getKey().name();
					
					//System.out.println(path+operationId+method);
					
					//System.out.println("value of i="+i);
				
					String values=path+","+operationId+","+method;
					finalList.add(values);
					
				//System.out.println(paths.getKey()+ "==="+opsMap.getValue().getOperationId()+"=="+opsMap.getKey());
				//System.out.println(opsMap.getValue().getOperationId());
				//System.out.println(opsMap.getKey());
				
				
			
				
			}
//			for(int i=0;i<finalList.size();i++){
//				sheet.createRow(1).createCell(0).setCellValue(path);
//				sheet.createRow(1).createCell(1).setCellValue(operationId);
//				sheet.createRow(1).createCell(2).setCellValue(method);
//				
//				
//			}
			
			
			
		}
//		FileOutputStream fileOutputStream=new FileOutputStream(excelFilePath);
//		workbook.write(fileOutputStream);
//		workbook.close();
		
		return finalList;
		
		
		
		

}

	
	public static void getDefReference(String swgFilePath,String excelFilePath) throws Exception{
		Swagger swagger = new SwaggerParser().read(swgFilePath);
		Map<String, Path> allPathvalues = swagger.getPaths();
		for(Map.Entry<String, Path> paths:allPathvalues.entrySet()){
			FileInputStream inputStream=new FileInputStream(excelFilePath);
			XSSFWorkbook workbook=new XSSFWorkbook(inputStream);
			XSSFSheet sheet=workbook.getSheetAt(0);
//			int rowCount=sheet.getLastRowNum()+1;
//			for(int i=0;i<rowCount;i++){
//				String path=sheet.getRow(i).getCell(0).getStringCellValue();
//				String operationId=sheet.getRow(i).getCell(1).getStringCellValue();
//				String method=sheet.getRow(i).getCell(2).getStringCellValue();
//				
				//to get reference of response
//				Response rsp=allPathvalues.get(paths.getValue()).getGet().getResponses().get("200");
//				io.swagger.models.ArrayModel arrayModel=(ArrayModel) rsp.getResponseSchema();
//				io.swagger.models.properties.RefProperty properties=(RefProperty) arrayModel.getItems();
//				System.out.println(properties.get$ref());
				
				//to get refernce of request body
				List<Parameter> params=allPathvalues.get("/pet").getPost().getParameters();
				for(int i=0;i<params.size();i++){
					io.swagger.models.parameters.BodyParameter bodyParam=(BodyParameter) params.get(i);
					System.out.println(bodyParam.getSchema().getReference());
					
				}
				
			}
			
			
			
		}
		
	
	
	public static void writePathsToExcel(String swgFile,String excelFilePath) throws Exception{
		ExcelSheetOps excelSheetOps=new ExcelSheetOps();
		List<String> values=excelSheetOps.getPaths(swgFile, excelFilePath);
		FileInputStream fileInputStream=new FileInputStream(excelFilePath);
		XSSFWorkbook workbook=new XSSFWorkbook(fileInputStream);
		XSSFSheet sheet=workbook.createSheet("APIDetails");
		for(int i=0;i<values.size();i++){
			String[] value=values.get(i).split(",",3);
			if(sheet.getRow(i)==null){
				sheet.createRow(i);
				sheet.getRow(i).createCell(0).setCellValue(value[0]);
				sheet.getRow(i).createCell(1).setCellValue(value[1]);
				sheet.getRow(i).createCell(2).setCellValue(value[2]);
				
			}
			
			System.out.println(value[0]+value[1]+value[2]);			
		}
		FileOutputStream fileOutputStream=new FileOutputStream(excelFilePath);
		workbook.write(fileOutputStream);
		workbook.close();
		
		
	}

	
	
	public static void main(String[] args) throws Exception {
		String excelSheetPath = "C:\\Users\\Hidayath\\Desktop\\AccountsInfo - Copy.xlsx";
		String swaggerFilePath = "C:\\Users\\Hidayath\\Desktop\\yaml\\petStore\\Swagger2.0\\petStoreV3.json";
		writePathsToExcel(swaggerFilePath, excelSheetPath);
		//getDefReference(swaggerFilePath,excelSheetPath);
		
	}}
