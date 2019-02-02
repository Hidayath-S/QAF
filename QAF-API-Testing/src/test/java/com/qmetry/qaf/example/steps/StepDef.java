package com.qmetry.qaf.example.steps;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.Test;

import com.qmetry.qaf.automation.step.QAFTestStep;
import com.qmetry.qaf.automation.step.QAFTestStepProvider;

import jxl.write.DateFormat;

public class StepDef {
	@QAFTestStep(description="get Todays date")
	public String getTodaysDate(){
		//TODO: remove NotYetImplementedException and call test steps
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String Today=dateFormat.format(date);
		return Today;
	}
		
	}