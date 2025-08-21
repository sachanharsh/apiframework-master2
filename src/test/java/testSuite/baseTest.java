
package testSuite;

import org.testng.annotations.BeforeClass;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import properties.PropertiesReader;
import utils.AuthManager;

public abstract class baseTest {
	protected RequestSpecification spec;
	private static final PropertiesReader propertiesReader = new PropertiesReader();
	
	@BeforeClass(groups = { "regression","smoke" })
	public void setup() {
		String HOST=propertiesReader.getHost();
		
		
		String token=AuthManager.getInstance().getToken();
		
		spec=new RequestSpecBuilder()
				.setBaseUri(HOST)
				.setContentType(ContentType.JSON)
				.addHeader("Authorization", "Bearer "+token)
				.build();
	}
}

