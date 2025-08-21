package utils;

//import org.testng.IRetryAnalyzer;
//import org.testng.ITestResult;

public class RetryAnalyzer{

	private int count=0;
	private static final int MAX_RETRIES=3;

	public boolean retry() {
		// TODO Auto-generated method stub
		if(count<MAX_RETRIES) {
			count++;
			return true;
		}
		return false;
	}

}
