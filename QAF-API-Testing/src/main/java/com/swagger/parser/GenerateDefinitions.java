package com.swagger.parser;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.Properties;

import com.fasterxml.jackson.databind.module.SimpleModule;

import io.swagger.oas.inflector.examples.ExampleBuilder;
import io.swagger.oas.inflector.examples.models.Example;
import io.swagger.oas.inflector.processors.JsonNodeExampleSerializer;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.OpenAPIV3Parser;

public class GenerateDefinitions {
	@SuppressWarnings({"rawtypes" })
	public static void generateDefinitions() throws Exception{
		FileReader reader=new FileReader("swagger.properties");
		Properties properties=new Properties();
		properties.load(reader);
		String defDirectory=properties.getProperty("swagger.definition.directory.path");
		String swaggerFilePath=properties.getProperty("swagger.file.path");
		String jsonExample="";
		//GenerateJSONPaths jsonPaths=new GenerateJSONPaths(jsonExample);
		OpenAPI parser=new OpenAPIV3Parser().read(swaggerFilePath);
		Map<String, Schema> definitions=parser.getComponents().getSchemas();		
		for (Map.Entry<String, Schema> entry : definitions.entrySet()) {
			Schema model=definitions.get(entry.getKey());
			Example example=ExampleBuilder.fromSchema(model, definitions);
			SimpleModule simpleModule= new SimpleModule().addSerializer(new JsonNodeExampleSerializer());
			io.swagger.v3.core.util.Json.mapper().registerModule(simpleModule);
			jsonExample=io.swagger.v3.core.util.Json.pretty(example);
			FileWriter writer= new FileWriter(defDirectory+"\\"+entry.getKey()+".json");
			writer.write(jsonExample);
			writer.flush();
			writer.close();
			
		}
		
	}

}
