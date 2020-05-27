package com.DB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;



public class JSONParser {
	
	private List<String> pathList;
	private HashMap<String, Object> pathListandValue;
	private String json;
	
	public List<String> getListPath() {
		return pathList;
	}
	public void setListPath(List<String> listPath) {
		this.pathList = listPath;
	}
	public HashMap<String, Object> getPathListandValue() {
		return pathListandValue;
	}
	public void setPathListandValue(HashMap<String, Object> pathListandValue) {
		this.pathListandValue = pathListandValue;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	
	public void setJSONPath(String json){
		this.pathList=new ArrayList<String>();
		this.pathListandValue=new HashMap<>();
		String jsonPath="$";
		
		
		
	}
	
	public void readObject(JSONObject object,String jsonPath){
		Iterator<String> itr=object.keys();
		String basePath=jsonPath;
		while(itr.hasNext()){
			String key=itr.next();
			Object value=object.get(key);
			jsonPath=basePath+"."+key;
		}
		
	}
	
	public void readArray(JSONArray array, String jsonPath){
		
		String basePath=jsonPath;
		for(int i=0;i<array.length();i++){
			Object value=array.get(i);
			jsonPath=basePath+"["+i+"]";
			if(value instanceof JSONArray){
				readArray((JSONArray) value, jsonPath);
			}
		}
		
	}
	
	
	

}
