package com.api.def.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
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
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.parser.OpenAPIV3Parser;

public class ExcelSheetOps {

	@SuppressWarnings({ "unchecked", "rawtypes"})
	public List<String> getPaths(String swgFilePath, String excelFilePath,String defType) throws Exception {
		String baseURL="";
		String path = "";
		String operationId = "";
		String method = "";
		String parameterType="";
		String parameters="";
		String reqFilePath="";
		String resFilePath="";
		String resType="";
		List finalList = new ArrayList<>();

		if(defType.equalsIgnoreCase("Swagger")){
			Swagger swagger = new SwaggerParser().read(swgFilePath);
			baseURL=swagger.getSchemes().get(0)+swagger.getHost()+swagger.getBasePath();
			Map<String, Path> allPathvalues = swagger.getPaths();
			for (Map.Entry<String, Path> paths : allPathvalues.entrySet()) {
				Map<HttpMethod, Operation> operationMap = paths.getValue().getOperationMap();

				for (Map.Entry<HttpMethod, Operation> opsMap : operationMap.entrySet()) {
					path = paths.getKey();
					operationId = opsMap.getValue().getOperationId();
					method = opsMap.getKey().name();
					if(operationId==null){
						operationId=opsMap.getValue().getSummary().replaceAll("\\s", "");
					}
					if(method.equalsIgnoreCase("GET")){
						System.out.println(swagger.getPath(path).getGet().getParameters());
						
					}
					
					
					
					
					
					
					
					

					String values = baseURL+ ","+path + "," + operationId + "," + method;
					System.out.println(values);
					finalList.add(values);

				}

			}
			
		}
		if(defType.equalsIgnoreCase("openapi")){
			OpenAPI parser=new OpenAPIV3Parser().read(swgFilePath);
			Paths allPathsValues=parser.getPaths();
			for (Entry<String, PathItem> paths : allPathsValues.entrySet()) {
				
				PathItem pathInfo=allPathsValues.get(paths.getKey());
				Map<io.swagger.v3.oas.models.PathItem.HttpMethod, io.swagger.v3.oas.models.Operation> opsMap=pathInfo.readOperationsMap();
				for(Entry<io.swagger.v3.oas.models.PathItem.HttpMethod, io.swagger.v3.oas.models.Operation> ops:opsMap.entrySet()){
					path=paths.getKey();
					method=ops.getKey().name();
					operationId=ops.getValue().getOperationId();
					if(operationId==null){
						operationId=ops.getValue().getSummary();
					}
					String values = path + "," + operationId + "," + method;
					//System.out.println(values);
					finalList.add(values);
				}
			
			
			}
			
		}
		
		//System.out.println(finalList.size());
		return finalList;

	}



	public static void writePathsToExcel() throws Exception {
		FileReader reader=new FileReader("swagger.properties");
		Properties properties=new Properties();
		properties.load(reader);
		String excelFilePath=properties.getProperty("swagger.excel.file.path");
		String swgFile=properties.getProperty("swagger.file.path");
		String defType=properties.getProperty("swagger.API.def");
		ExcelSheetOps excelSheetOps = new ExcelSheetOps();
		List<String> values = excelSheetOps.getPaths(swgFile, excelFilePath,defType);
		FileInputStream fileInputStream = new FileInputStream(excelFilePath);
		XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
		XSSFSheet sheet = workbook.getSheetAt(0);
		for (int i = 0; i < values.size(); i++) {
			String[] value = values.get(i).split(",", 3);
			if (sheet.getRow(i+1) == null) {
				sheet.createRow(i+1);
				sheet.getRow(i+1).createCell(1).setCellValue(value[0]);
				sheet.getRow(i+1).createCell(2).setCellValue(value[1]);
				sheet.getRow(i+1).createCell(3).setCellValue(value[2]);
			}

			//System.out.println(value[0] + value[1] + value[2]);
		}
		FileOutputStream fileOutputStream = new FileOutputStream(excelFilePath);
		workbook.write(fileOutputStream);
		workbook.close();

	}

	
}
