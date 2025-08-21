package testSuite;

import org.testng.Assert;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import properties.bookStoreProperties;
import utils.RetryAnalyzer;
import entities.getBooksResponse;
import entities.getBooksResponse.Book;
import entities.getUserResponse;

import static io.restassured.RestAssured.given;

public class getBooksTest extends baseTest {

    @Test(groups = {"testTag"},priority=1)
    public void getAllBooksData() {
        Response resp = given().
                contentType("application/json").
                when().
                get(bookStoreProperties.getAllBooks);
        System.out.println(resp.asString());
        getBooksResponse getBooksResponse = resp.as(getBooksResponse.class);
        Assert.assertTrue(getBooksResponse.getBooks().size()>0);
    }
    
	@Test(priority=2)
	@Description("Fetch book data and verify content")
	public void getBookData() {
	    Response resp = given().
	            contentType("application/json")
	            .spec(spec)
	            .queryParam("ISBN", "9781593277574")
	            .when()
	            .get("/BookStore/v1/Book")
	            .then()
	            .statusCode(200)
	            .extract()
	            .response();
	    Allure.addAttachment("GET /BookStore/v1/Book", resp.asString());
	    System.out.println("DDV:"+resp.asString());
	    Book book = resp.as(Book.class);
	    Assert.assertEquals(book.getTitle(),"Understanding ECMAScript 6");
	}
}
