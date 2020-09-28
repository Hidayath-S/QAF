package com.swagger.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

public class ExcelSheetOps {

	@SuppressWarnings({ "unchecked", "rawtypes"})
	public List<String> getPaths(String swgFilePath, String excelFilePath) throws Exception {
		Swagger swagger = new SwaggerParser().read(swgFilePath);
		String path = "";
		String operationId = "";
		String method = "";
		List finalList = new ArrayList<>();

		// to get paths

		Map<String, Path> allPathvalues = swagger.getPaths();

		//FileInputStream fileInputStream = new FileInputStream(excelFilePath);
		//XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
		//XSSFSheet sheet = workbook.getSheetAt(0);
		for (Map.Entry<String, Path> paths : allPathvalues.entrySet()) {
			Map<HttpMethod, Operation> operationMap = paths.getValue().getOperationMap();

			for (Map.Entry<HttpMethod, Operation> opsMap : operationMap.entrySet()) {
				path = paths.getKey();
				operationId = opsMap.getValue().getOperationId();
				method = opsMap.getKey().name();
				if(operationId==null&&method.equalsIgnoreCase("GET")){
					operationId=paths.getValue().getGet().getSummary().replaceAll("\\s", "");
				}
				if(operationId==null&&method.equalsIgnoreCase("POST")){
					operationId=paths.getValue().getPost().getSummary().replaceAll("\\s", "");
				}
				if(operationId==null&&method.equalsIgnoreCase("PUT")){
					operationId=paths.getValue().getPut().getSummary().replaceAll("\\s", "");
				}
				if(operationId==null&&method.equalsIgnoreCase("DELETE")){
					operationId=paths.getValue().getDelete().getSummary().replaceAll("\\s", "");
				}
				

				String values = path + "," + operationId + "," + method;
				//System.out.println(values);
				finalList.add(values);

			}

		}

		return finalList;

	}



	public static void writePathsToExcel() throws Exception {
		FileReader reader=new FileReader("swagger.properties");
		Properties properties=new Properties();
		properties.load(reader);
		String excelFilePath=properties.getProperty("swagger.excel.file.path");
		String swgFile=properties.getProperty("swagger.file.path");
		ExcelSheetOps excelSheetOps = new ExcelSheetOps();
		List<String> values = excelSheetOps.getPaths(swgFile, excelFilePath);
		FileInputStream fileInputStream = new FileInputStream(excelFilePath);
		XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
		XSSFSheet sheet = workbook.getSheetAt(0);
		for (int i = 0; i < values.size(); i++) {
			String[] value = values.get(i).split(",", 3);
			if (sheet.getRow(i) == null) {
				sheet.createRow(i);
				sheet.getRow(i).createCell(0).setCellValue(value[0]);
				sheet.getRow(i).createCell(1).setCellValue(value[1]);
				sheet.getRow(i).createCell(2).setCellValue(value[2]);

			}

			//System.out.println(value[0] + value[1] + value[2]);
		}
		FileOutputStream fileOutputStream = new FileOutputStream(excelFilePath);
		workbook.write(fileOutputStream);
		workbook.close();

	}

	
}
