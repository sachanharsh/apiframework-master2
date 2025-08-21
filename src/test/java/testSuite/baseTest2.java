package testSuite;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

import io.qameta.allure.testng.AllureTestNg;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import utils.AuthManager;
import utils.ConfigManager;
import properties.bookStoreProperties2;

@Listeners(AllureTestNg.class)
public abstract class baseTest2 {
    protected RequestSpecification spec;

    @BeforeClass
    public void setup() {
        String uri  = ConfigManager.getInstance().get("host");
        
        String token = AuthManager.getInstance().getToken();

        spec = new RequestSpecBuilder()
            .setBaseUri(bookStoreProperties2.host)
            .setContentType("application/json")
            .addHeader("Authorization", "Bearer "+token)
            .build();
    }
}