package com.first;



import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class GenerateJSONPaths {
	
	private List<String> pathList;
    private Object json;
	
    public GenerateJSONPaths(Object json) {
        this.json = json;
        this.pathList = new ArrayList<String>();
       
    }
	public static void main(String[] args) throws Exception {
		
		
		//String json = "{  \"code\" : 0,  \"type\" : \"string\",  \"message\" : \"string\"}";
		JSONParser jsonParser=new JSONParser();
		FileReader reader= new FileReader("resources\\SwgGeneratedFiles\\Definitions\\Pet.json");
		String json=jsonParser.parse(reader).toString();
		
		JSONObject jsonObject=(JSONObject) jsonParser.parse(json);
		GenerateJSONPaths bdd=new GenerateJSONPaths(json);
		bdd.readObject(jsonObject, "$");
		
		
		
	}
	
	
	public  List<String> readObject(JSONObject object, String jsonPath) {
        Set<String> keysItr =  object.keySet();
        Iterator<String> it=keysItr.iterator();
        String parentPath = jsonPath;
        while(it.hasNext()) {
            String key = it.next();
            Object value = object.get(key);
            jsonPath = parentPath + "." + key;
            //System.out.println(jsonPath);

            if(value instanceof JSONArray) {            
                readArray((JSONArray) value, jsonPath);
            }
            else if(value instanceof JSONObject) {
                readObject((JSONObject) value, jsonPath);
            } else { // is a value
                this.pathList.add(jsonPath);    
            }          
        }
		return pathList;  
    }
	
	
	private  void readArray(JSONArray array, String jsonPath) {      
        String parentPath = jsonPath;
        for(int i = 0; i < array.size(); i++) {
            Object value = array.get(i);        
            jsonPath = parentPath + "[" + i + "]";

            if(value instanceof JSONArray) {
                readArray((JSONArray) value, jsonPath);
            } else if(value instanceof JSONObject) {                
                readObject((JSONObject) value, jsonPath);
            } else { // is a value
                this.pathList.add(jsonPath);
            }       
        }
    }

}
