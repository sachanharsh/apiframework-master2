package testSuite;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.Test;

import entities.getUserResponse;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import java.util.*;
import entities.postbookRequest;
import entities.postbookRequest.Isbn;

public class getUserTest extends baseTest {
	@Test(groups = {"smoke"})
	@Description("Fetch user data and verify initial book count is 1")
	public void getUserData() {
	    Response resp = given().
	            contentType("application/json")
	            .spec(spec)
	            .when()
	            .get("/Account/v1/User/4ae39f14-c763-4600-9590-a69a1c2afd1b")
	            .then()
	            .statusCode(200)
	            .extract()
	            .response();
	    Allure.addAttachment("Initial GET /Account/v1/User", resp.asString());
	    System.out.println(resp.asString());
	    getUserResponse user = resp.as(getUserResponse.class);
	    AssertJUnit.assertEquals(user.getBooks().size(),1);
	}
	
	@Test(groups = {"regression"})
	@Description("Add two books to the user's collection via POST /BookStore/v1/Books")
	public void postBookToUserData() {
		List<Isbn> isbn=new ArrayList<>();
		isbn.add(new Isbn("9781593277574"));
		isbn.add(new Isbn("9781593275846"));
		postbookRequest body=new postbookRequest("4ae39f14-c763-4600-9590-a69a1c2afd1b",isbn);
	    Response resp = given().
	            contentType("application/json")
	            .spec(spec)
	            .body(body).log().all()
	            .when()
	            .post("/BookStore/v1/Books")
	            .then().log().all()
	            .statusCode(201)
	            .extract()
	            .response();
	    Allure.addAttachment("POST /BookStore/v1/Books Response", resp.asString());
	    System.out.println(resp.asString());
	    getUserResponse user2=given().
        contentType("application/json")
        .spec(spec)
        .body(body).log().all()
        .when()
        .post("/BookStore/v1/Books")
        .then().log().all()
        .statusCode(201)
        .extract().as(getUserResponse.class);
	    //getUserResponse user = resp.as(getUserResponse.class);
	}
	
	@Test(groups = {"smoke"})
	@Description("Fetch user data again and verify book count is 3 after additions")
	public void getUserData2() {
	    Response resp = given().
	            contentType("application/json")
	            .spec(spec)
	            .when()
	            .get("/Account/v1/User/4ae39f14-c763-4600-9590-a69a1c2afd1b")
	            .then()
	            .statusCode(200)
	            .extract()
	            .response();
	    Allure.addAttachment("Second GET /Account/v1/User", resp.asString());
	    System.out.println(resp.asString());
	    getUserResponse user = resp.as(getUserResponse.class);
	    AssertJUnit.assertEquals(user.getBooks().size(),3);
	}
	
	@DataProvider(name="isbnIds")
	public Object[][] isbnIds() {
		return new Object[][] {{"4ae39f14-c763-4600-9590-a69a1c2afd1b","9781593277574"},
			{"4ae39f14-c763-4600-9590-a69a1c2afd1b","9781593275846"}
		};
	}
	@Test(groups = {"regression"},dataProvider="isbnIds",priority=4)
	@Description("Remove each book from the user's collection via DELETE /BookStore/v1/Book")
	public void deleteBookUserData1(String userId,String isbn) {
		Map<String,String> hmap=new HashMap<String,String>();
		hmap.put("userId", userId);
		hmap.put("isbn", isbn);
	    Response resp = given().
	            contentType("application/json")
	            .spec(spec)
	            .body(hmap).log().all()
	            .when()
	            .delete("/BookStore/v1/Book")
	            .then()
	            .statusCode(204)
	            .extract()
	            .response();
	    Allure.addAttachment("DELETE /BookStore/v1/Book for ISBN ", resp.asString());
	    System.out.println(resp.asString());
	}
	
	@Test(groups = {"smoke"})
	@Description("Fetch book data and verify content")
	public void getBookData() {
	    Response resp = given().
	            contentType("application/json")
	            .spec(spec)
	            .when()
	            .get("/Account/v1/User/4ae39f14-c763-4600-9590-a69a1c2afd1b")
	            .then()
	            .statusCode(200)
	            .extract()
	            .response();
	    Allure.addAttachment("Initial GET /Account/v1/User", resp.asString());
	    System.out.println(resp.asString());
	    getUserResponse user = resp.as(getUserResponse.class);
	    AssertJUnit.assertEquals(user.getBooks().size(),3);
	}
}
