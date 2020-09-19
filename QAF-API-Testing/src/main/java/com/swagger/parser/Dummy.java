package com.swagger.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.swagger.models.ArrayModel;
import io.swagger.models.Model;
import io.swagger.models.RefModel;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.parser.SwaggerParser;

public class Dummy {

	public static void main(String[] args){
		try{
			Swagger swagger = new SwaggerParser().read("C:/Users/Hidayath/Desktop/yaml/petStore/Swagger2.0/petStoreV2.json");
			FileInputStream inputStream = new FileInputStream("C:/Users/Hidayath/Desktop/APIDetails.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = workbook.getSheetAt(0);
			int rownum = sheet.getLastRowNum() + 1;
			for (int i = 0; i < rownum; i++) {
				String path=sheet.getRow(i).getCell(0).getStringCellValue();
				String httpMethod = sheet.getRow(i).getCell(2).getStringCellValue();
				if(httpMethod.equalsIgnoreCase("GET")){
					Collection<String> response=swagger.getPath(path).getGet().getResponses().keySet();
					Iterator<String> rspIt=response.iterator();
					while(rspIt.hasNext()){
						String b=rspIt.next();
						
						if(b.equals("200")){
							//System.out.println(path);
							
							Response rsp = swagger.getPath(path).getGet().getResponses().get(b);
							if(rsp.getResponseSchema().getClass().getName().equalsIgnoreCase("io.swagger.models.ArrayModel")){
								ArrayModel arrayModel=(ArrayModel) rsp.getResponseSchema();
								RefProperty properties=(RefProperty) arrayModel.getItems();
								System.out.println(properties.getSimpleRef());
							}
							if(rsp.getResponseSchema().getClass().getName().equalsIgnoreCase("io.swagger.models.RefModel")){
								RefModel arrayModel= (RefModel) rsp.getResponseSchema();
								String properties=arrayModel.getSimpleRef();
								System.out.println(properties);
							}
							
							
							
							
						}
						
					}	
				}
				
				
				
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
