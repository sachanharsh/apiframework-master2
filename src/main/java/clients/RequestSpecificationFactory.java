package clients;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RequestSpecificationFactory {
	private RequestSpecification spec;
	
	public RequestSpecificationFactory() {
		this.spec=new RequestSpecBuilder().setContentType(ContentType.JSON).build();
	}
	
	public RequestSpecification getSpec() {
		return spec;
	}
}
