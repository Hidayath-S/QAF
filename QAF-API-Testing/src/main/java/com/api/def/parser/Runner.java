package com.api.def.parser;

public class Runner {
	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
//		GenerateDefinitions definitions=new GenerateDefinitions();
//		definitions.generateDefinitions();
		ExcelSheetOps ops=new ExcelSheetOps();
		ops.writePathsToExcel();
//		CreateRequestCall requestCall=new CreateRequestCall();
//		requestCall.createRequestCall();
//		CreateBDDScenario bddScenario=new CreateBDDScenario();
//		bddScenario.createQmetryScenario();
	}

}
