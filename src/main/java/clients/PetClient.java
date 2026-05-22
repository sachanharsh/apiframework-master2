package clients;

import static io.restassured.RestAssured.given;

import entities.Pet;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class PetClient {
	
	//private RequestSpecification spec;
	
	public static Response postPet(RequestSpecification spec,String payload) {
		return given().contentType(ContentType.JSON).body(payload).log().all()
				.when()
				.post("https://petstore.swagger.io/v2/pet");
	}
}
