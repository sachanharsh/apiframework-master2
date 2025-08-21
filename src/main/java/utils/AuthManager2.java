
package utils;

import entities.baseEndPoint;
//import com.gojek.entities.TokenResponse;
import utils.ConfigManager;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;

import java.time.Instant;
import java.time.format.DateTimeParseException;

import static io.restassured.RestAssured.*;

public class AuthManager2 {
    private static AuthManager2 instance;
    private String token;
    private Instant expiresAt;

    // buffer so we never race an edge-of-expiry token
    private static final long SAFETY_BUFFER_SEC = 30;

    private AuthManager2() {
        refreshToken();
    }

    public static synchronized AuthManager2 getInstance() {
    	System.out.println("harshd:"+(instance == null));
        if (instance == null) {
            instance = new AuthManager2();
        }
        return instance;
    }

    public synchronized String getToken() {
        Instant now = Instant.now();
        if (token == null || now.isAfter(expiresAt. minusSeconds(SAFETY_BUFFER_SEC))) {
            refreshToken();
        }
        return token;
    }
    
    
    
    private void refreshToken() {
        // read credentials from your config.properties
        String userName = ConfigManager.getInstance().get("user.name");
        String password = ConfigManager.getInstance().get("user.password");
        
        baseEndPoint authReq = new baseEndPoint(userName, password);
        JsonPath resp = given().contentType(ContentType.JSON)
                .body(authReq).log().all()
            .when()
                .post("https://demoqa.com/Account/v1/GenerateToken")
            .then().log().all()
                .statusCode(200)
                .extract().jsonPath();

        token = resp.getString("token");
        try {
            // expires is an ISO-8601 string, e.g. "2025-08-03T14:25:30Z"
            expiresAt = Instant.parse(resp.getString("expires"));
            
        } catch (DateTimeParseException e) {
            // fallback to 1 hour from now
            expiresAt = Instant.now().plusSeconds(3600);
        }
    }
}