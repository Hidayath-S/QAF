package com.api.def.parser;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.swagger.models.ArrayModel;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.RefProperty;
import io.swagger.parser.SwaggerParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;


public class CreateRequestCall {
	public static void createRequestCall() throws Exception{
		FileReader reader=new FileReader("swagger.properties");
		Properties properties=new Properties();
		properties.load(reader);
		String excelFilePath=properties.getProperty("swagger.excel.file.path");
		String swgFile=properties.getProperty("swagger.file.path");
		String defPath=properties.getProperty("swagger.definition.directory.path");
		String reqCallPath=properties.getProperty("swagger.reqCall.path");
		String defType=properties.getProperty("swagger.API.def");
		Swagger swagger = new SwaggerParser().read(swgFile);
		OpenAPI parser=new OpenAPIV3Parser().read(swgFile);
		String envURL="";
		String baseURL="";
		List<Parameter> params=null;
		
		//System.out.println(envURL);
		String XMLContent="";
		//String positive="";
		//String negative="";
		
		FileInputStream fileInputStream=new FileInputStream(excelFilePath);
		XSSFWorkbook workbook=new XSSFWorkbook(fileInputStream);
		XSSFSheet sheet=workbook.getSheetAt(0);
		int rowCount=(sheet.getLastRowNum()+1);
		
		//System.out.println("toralRows="+rowCount);
		for(int i=0;i<rowCount;i++){
			if(defType.equalsIgnoreCase("swagger")){
				envURL="http://"+swagger.getHost()+swagger.getBasePath();
				baseURL=envURL+sheet.getRow(i).getCell(0).getStringCellValue();
				
			}if(defType.equalsIgnoreCase("openapi")){
				envURL="http://"+parser.getServers().get(0);
				baseURL=envURL+sheet.getRow(i).getCell(0).getStringCellValue();
			}
			
			String path=sheet.getRow(i).getCell(0).getStringCellValue();
			//System.out.println(baseURL);
			String operationId=sheet.getRow(i).getCell(1).getStringCellValue();
			//System.out.println(operationId);
			String method=sheet.getRow(i).getCell(2).getStringCellValue();
			//System.out.println(method);
			String requestFilePath="";
			if(method.equalsIgnoreCase("GET")){
				if(defType.equalsIgnoreCase("swagger")){
					params=swagger.getPath(path).getGet().getParameters();
					
				}
//				if(defType.equalsIgnoreCase("openapi")){
//					params=parser.getPaths().get(path).getGet().getParameters();
//					
//				}
				
				
				
				if(params.isEmpty()){
					
					
					XMLContent="<requests>\n <"+operationId +"> \n { \n 'baseUrl':'"+baseURL+ "', \n 'headers':{\n 'Content-Type': 'application/JSON' \n },\n 'method':"+method+ ",\n } \n </"+operationId+"> \n </requests>";
				}
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
							baseURL=baseURL+"&amp;"+name+"="+"${"+name+"}";
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
			if(method.equalsIgnoreCase("POST")){
				
				
				params=swagger.getPath(path).getPost().getParameters();
				
				for(int j=0;j<params.size();j++){
					String IN=params.get(j).getIn();
					String name=params.get(j).getName();
					if(IN.equalsIgnoreCase("body")){
						io.swagger.models.parameters.BodyParameter bodyParam=(BodyParameter) params.get(j);
						if(bodyParam.getSchema().getClass().getName().equalsIgnoreCase("io.swagger.models.RefModel")){
							String[] reqRef=bodyParam.getSchema().getReference().split("/",3);
							requestFilePath=defPath+"/"+reqRef[2]+".json";	
						}
						if(bodyParam.getSchema().getClass().getName().equalsIgnoreCase("io.swagger.models.ArrayModel")){
							io.swagger.models.ArrayModel arrayModel= (ArrayModel) bodyParam.getSchema();
							RefProperty items=(RefProperty) arrayModel.getItems();
							requestFilePath=items.getSimpleRef();
							
								
						}
						
						
						
						XMLContent="<requests>\n <"+operationId +"> \n { \n 'baseUrl':'"+baseURL+ "', \n 'headers':{\n 'Content-Type': 'application/JSON' \n },\n 'method':"+method+ ",\n'body':'file:"+requestFilePath+"', \n } \n </"+operationId+"> \n </requests>";
					}
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
							baseURL=baseURL+"&amp;"+name+"="+"${"+name+"}";
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
			if(method.equalsIgnoreCase("PUT")){
				
				
				params=swagger.getPath(path).getPut().getParameters();
				for(int j=0;j<params.size();j++){
					String IN=params.get(j).getIn();
					String name=params.get(j).getName();
					if(IN.equalsIgnoreCase("body")){
						io.swagger.models.parameters.BodyParameter bodyParam=(BodyParameter) params.get(j);
						if(bodyParam.getSchema().getClass().getName().equalsIgnoreCase("io.swagger.models.RefModel")){
							String[] reqRef=bodyParam.getSchema().getReference().split("/",3);
							requestFilePath=defPath+"/"+reqRef[2]+".json";	
						}
						if(bodyParam.getSchema().getClass().getName().equalsIgnoreCase("io.swagger.models.ArrayModel")){
							io.swagger.models.ArrayModel arrayModel= (ArrayModel) bodyParam.getSchema();
							RefProperty items=(RefProperty) arrayModel.getItems();
							requestFilePath=items.getSimpleRef();
							
								
						}
						XMLContent="<requests>\n <"+operationId +"> \n { \n 'baseUrl':'"+baseURL+ "', \n 'headers':{\n 'Content-Type': 'application/JSON' \n },\n 'method':"+method+ ",\n 'body':'file:"+requestFilePath+"', \n } \n </"+operationId+"> \n </requests>";
					}
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
							baseURL=baseURL+"&amp;"+name+"="+"${"+name+"}";
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
			if(method.equalsIgnoreCase("DELETE")){
				
				
				params=swagger.getPath(path).getDelete().getParameters();
				for(int j=0;j<params.size();j++){
					String IN=params.get(j).getIn();
					String name=params.get(j).getName();
					if(IN.equalsIgnoreCase("body")){
						io.swagger.models.parameters.BodyParameter bodyParam=(BodyParameter) params.get(j);
						if(bodyParam.getSchema().getClass().getName().equalsIgnoreCase("io.swagger.models.RefModel")){
							String[] reqRef=bodyParam.getSchema().getReference().split("/",3);
							requestFilePath=defPath+"/"+reqRef[2]+".json";	
						}
						if(bodyParam.getSchema().getClass().getName().equalsIgnoreCase("io.swagger.models.ArrayModel")){
							io.swagger.models.ArrayModel arrayModel= (ArrayModel) bodyParam.getSchema();
							RefProperty items=(RefProperty) arrayModel.getItems();
							requestFilePath=items.getSimpleRef();
							
								
						}
						XMLContent="<requests>\n <"+operationId +"> \n { \n 'baseUrl':'"+baseURL+ "', \n 'headers':{\n 'Content-Type': 'application/JSON' \n },\n 'method':"+method+ ", \n'body':'file:"+requestFilePath+"', \n } \n </"+operationId+"> \n </requests>";
					}
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
							baseURL=baseURL+"&amp;"+name+"="+"${"+name+"}";
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
			
			
				
					
					FileWriter writer= new FileWriter(reqCallPath+"\\"+operationId+".xml");
					writer.write(XMLContent);
					writer.flush();
					writer.close();
					
				
			
			//String XMLContent="<requests>\n <"+operationId +"> \n { \n 'baseUrl':'"+baseURL+ "', \n 'headers':{\n 'Content-Type': 'application/JSON' \n },\n 'method':"+method+ "\n } \n </"+operationId+"> \n </requests>";
			
		}
		

				
		
		
	workbook.close();	
	}
	

}
