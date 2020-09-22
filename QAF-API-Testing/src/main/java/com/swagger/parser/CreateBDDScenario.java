package com.swagger.parser;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.parser.JSONParser;

import io.swagger.models.ArrayModel;
import io.swagger.models.HttpMethod;
import io.swagger.models.Model;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.RefModel;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.parser.SwaggerParser;

public class CreateBDDScenario {
	public static void createQmetryScenario() throws Exception {
		FileReader reader = new FileReader("swagger.properties");
		Properties properties = new Properties();
		properties.load(reader);
		String excelFilePath = properties.getProperty("swagger.excel.file.path");
		String swgFile = properties.getProperty("swagger.file.path");
		String defPath = properties.getProperty("swagger.definition.directory.path");
		String bddFilePath = properties.getProperty("swagger.bdd.path");
		ParseSwagger parseSwagger = new ParseSwagger();
		String jsonPathValidation = "";
		String scenarioContent = "";
		CreateBDDScenario bddScenario = new CreateBDDScenario();
		FileInputStream fileInputStream = new FileInputStream(excelFilePath);
		XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
		XSSFSheet sheet = workbook.getSheetAt(0);
		int rowCount = sheet.getLastRowNum() + 1;
		for (int i = 1; i < rowCount; i++) {
			String path = sheet.getRow(i).getCell(0).getStringCellValue();
			String operationId = sheet.getRow(i).getCell(1).getStringCellValue();
			String method = sheet.getRow(i).getCell(2).getStringCellValue();
			String resDefName = bddScenario.getResponseFileName(swgFile, excelFilePath, method, path);
			System.out.println(resDefName);
			if (resDefName.equalsIgnoreCase("")) {
				System.out.println("There is no success response defined for operationId " + operationId);
			} else {
				JSONParser jsonParser = new JSONParser();
				FileReader fileReader = new FileReader(defPath + "\\" + resDefName + ".json");
				String json = jsonParser.parse(fileReader).toString();
				List<String> jsonPaths = parseSwagger.generateJSONPaths(json);
				List pathValidationList=new ArrayList<>();
				String pathValidationMethods="";
				for (int j = 0; j < jsonPaths.size(); j++) {
					String jsonPath = jsonPaths.get(j).toString();
					String value = jsonPath.replace("$", "").replace(".", "").replace("[", "").replace("]", "");

					jsonPathValidation = "AND response has value '${" + value + "}' at jsonpath '" + jsonPath + "'\r\n";
					
					pathValidationList.add(jsonPathValidation);
					pathValidationMethods=pathValidationList.toString().replace(",", "").substring(1);

					
				}
				pathValidationMethods=pathValidationMethods.substring(0, pathValidationMethods.lastIndexOf("]"));
				String str1= "SCENARIO: To test happy path scenario for " + operationId + "  API\r\n"
						+ "META-DATA: {'desc':'This is an example of scenario using QAF-BDD','groups':['POSITIVE']}\r\n"
						+ "Given COMMENT: '" + operationId + " micro service is up and running' \r\n"
						+ "When user requests '" + operationId + "'  \r\n"
						+ "Then response should have status code '200'\r\n";
				String str2="END\r\n";
				StringBuilder builder=new StringBuilder();
				scenarioContent=builder.append(str1).append(pathValidationMethods).append(str2).toString();
				
				
				
				FileWriter writer = new FileWriter(bddFilePath + "\\" + operationId + ".BDD");
				writer.write(scenarioContent);
				writer.flush();
				writer.close();

			}

		}

		workbook.close();

	}

	public String getResponseFileName(String swgFilePath, String excelsheetPath, String method, String path)
			throws Exception {
		Swagger swagger = new SwaggerParser().read(swgFilePath);
		String resDefName = "";
		Response rsp;
		Map<String, Path> allPaths = swagger.getPaths();
		if (method.equalsIgnoreCase("GET")) {
			Collection<String> response = allPaths.get(path).getGet().getResponses().keySet();
			Iterator<String> rspIt = response.iterator();
			while (rspIt.hasNext()) {
				String b = rspIt.next();

				if (b.equals("200")) {
					// System.out.println(path);

					rsp = swagger.getPath(path).getGet().getResponses().get(b);
					if (rsp.getResponseSchema().getClass().getName().equalsIgnoreCase("io.swagger.models.ArrayModel")) {
						ArrayModel arrayModel = (ArrayModel) rsp.getResponseSchema();
						RefProperty properties = (RefProperty) arrayModel.getItems();
						resDefName = properties.getSimpleRef();
					}
					if (rsp.getResponseSchema().getClass().getName().equalsIgnoreCase("io.swagger.models.RefModel")) {
						RefModel arrayModel = (RefModel) rsp.getResponseSchema();
						String properties = arrayModel.getSimpleRef();
						resDefName = properties;
					}

				}

			}
		}
		if (method.equalsIgnoreCase("POST")) {
			Collection<String> response = allPaths.get(path).getPost().getResponses().keySet();
			Iterator<String> rspIt = response.iterator();
			while (rspIt.hasNext()) {
				String b = rspIt.next();

				if (b.equals("200")) {
					// System.out.println(path);

					rsp = swagger.getPath(path).getPost().getResponses().get(b);
					if (rsp.getResponseSchema().getClass().getName().equalsIgnoreCase("io.swagger.models.ArrayModel")) {
						ArrayModel arrayModel = (ArrayModel) rsp.getResponseSchema();
						RefProperty properties = (RefProperty) arrayModel.getItems();
						resDefName = properties.getSimpleRef();
					}
					if (rsp.getResponseSchema().getClass().getName().equalsIgnoreCase("io.swagger.models.RefModel")) {
						RefModel arrayModel = (RefModel) rsp.getResponseSchema();
						String properties = arrayModel.getSimpleRef();
						resDefName = properties;
					}

				}

			}
		}
		if (method.equalsIgnoreCase("PUT")) {
			Collection<String> response = allPaths.get(path).getPut().getResponses().keySet();
			Iterator<String> rspIt = response.iterator();
			while (rspIt.hasNext()) {
				String b = rspIt.next();

				if (b.equals("200")) {
					// System.out.println(path);

					rsp = swagger.getPath(path).getPut().getResponses().get(b);
					if (rsp.getResponseSchema().getClass().getName().equalsIgnoreCase("io.swagger.models.ArrayModel")) {
						ArrayModel arrayModel = (ArrayModel) rsp.getResponseSchema();
						RefProperty properties = (RefProperty) arrayModel.getItems();
						resDefName = properties.getSimpleRef();
					}
					if (rsp.getResponseSchema().getClass().getName().equalsIgnoreCase("io.swagger.models.RefModel")) {
						RefModel arrayModel = (RefModel) rsp.getResponseSchema();
						String properties = arrayModel.getSimpleRef();
						resDefName = properties;
					}

				}

			}
		}
		if (method.equalsIgnoreCase("DELETE")) {
			Collection<String> response = allPaths.get(path).getDelete().getResponses().keySet();
			Iterator<String> rspIt = response.iterator();
			while (rspIt.hasNext()) {
				String b = rspIt.next();

				if (b.equals("200")) {
					// System.out.println(path);

					rsp = swagger.getPath(path).getDelete().getResponses().get(b);
					if (rsp.getResponseSchema().getClass().getName().equalsIgnoreCase("io.swagger.models.ArrayModel")) {
						ArrayModel arrayModel = (ArrayModel) rsp.getResponseSchema();
						RefProperty properties = (RefProperty) arrayModel.getItems();
						resDefName = properties.getSimpleRef();
					}
					if (rsp.getResponseSchema().getClass().getName().equalsIgnoreCase("io.swagger.models.RefModel")) {
						RefModel arrayModel = (RefModel) rsp.getResponseSchema();
						String properties = arrayModel.getSimpleRef();
						resDefName = properties;
					}

				}

			}
		}

		return resDefName;
	}
}
