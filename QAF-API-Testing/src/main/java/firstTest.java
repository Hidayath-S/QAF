import org.testng.annotations.Test;
import com.qmetry.qaf.automation.step.WsStep;
import com.qmetry.qaf.automation.testng.dataprovider.QAFDataProvider;
import com.qmetry.qaf.automation.step.QAFTestStep;

public class firstTest {
	//@QAFDataProvider(key="CreateModifyWire.data")
	@Test()
	public void test(){
		//WsStep.userRequests("groupkt.call");
		//WsStep.responseShouldHaveStatusCode(200);
		WsStep.userRequests("users.call");
	
		
	}

}
