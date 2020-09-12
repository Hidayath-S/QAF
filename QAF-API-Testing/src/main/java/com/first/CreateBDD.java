package com.first;



import java.io.FileWriter;

public class CreateBDD {
	
	public static void main(String[] args) throws Exception {
		
		String step1="SCENARIO: To get country details by invoking getCountry API \n META-DATA: {'desc':'This is an example of scenario using QAF-BDD','groups':['SMOKE']} \n END";
		FileWriter writer= new FileWriter("scenarios\\"+"scenario.bdd");
		writer.write(step1);
		writer.flush();
		writer.close();
		
		
	}

}
