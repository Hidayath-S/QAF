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
			Swagger swagger = new SwaggerParser().read("C:\\Users\\Hidayath\\Desktop\\yaml\\petStore\\swagger3.0\\petStore.json");
			System.out.println(swagger.getSwagger());
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
