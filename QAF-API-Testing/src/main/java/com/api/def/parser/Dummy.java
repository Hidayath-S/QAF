package com.api.def.parser;

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
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;

public class Dummy {

	public static void main(String[] args){
		try{
			OpenAPI parser=new OpenAPIV3Parser().read("C:/Users/Hidayath/Desktop/yaml/petStore/swagger3.0/petStoreV2.json");
			System.out.println(parser.getPaths());
			
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
