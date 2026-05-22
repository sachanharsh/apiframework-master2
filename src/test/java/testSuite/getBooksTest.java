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

	@Test(priority=3, groups = {"regression"})
	@Description("Verify book fields like author, publisher and pages for a known ISBN")
	public void getBookAndVerifyAllFields() {
	    Response resp = given()
	            .contentType("application/json")
	            .spec(spec)
	            .queryParam("ISBN", "9781593277574")
	            .when()
	            .get("/BookStore/v1/Book")
	            .then()
	            .statusCode(200)
	            .extract()
	            .response();
	    Allure.addAttachment("GET /BookStore/v1/Book fields", resp.asString());
	    Book book = resp.as(Book.class);
	    Assert.assertNotNull(book.getIsbn(), "ISBN should not be null");
	    Assert.assertNotNull(book.getAuthor(), "Author should not be null");
	    Assert.assertNotNull(book.getPublisher(), "Publisher should not be null");
	    Assert.assertTrue(book.getPages() > 0, "Pages should be greater than 0");
	    Assert.assertNotNull(book.getWebsite(), "Website should not be null");
	}

	@Test(priority=4, groups = {"regression"})
	@Description("Get book with invalid ISBN should return 400")
	public void getBookWithInvalidISBN() {
	    Response resp = given()
	            .contentType("application/json")
	            .spec(spec)
	            .queryParam("ISBN", "0000000000000")
	            .when()
	            .get("/BookStore/v1/Book")
	            .then()
	            .statusCode(400)
	            .extract()
	            .response();
	    Allure.addAttachment("GET /BookStore/v1/Book invalid ISBN", resp.asString());
	    System.out.println("Invalid ISBN response: " + resp.asString());
	}

	@Test(priority=5, groups = {"smoke"})
	@Description("Verify all books have non-null title, author and ISBN")
	public void getAllBooksAndVerifyFields() {
	    Response resp = given()
	            .contentType("application/json")
	            .when()
	            .get(bookStoreProperties.getAllBooks);
	    getBooksResponse booksResp = resp.as(getBooksResponse.class);
	    for (Book book : booksResp.getBooks()) {
	        Assert.assertNotNull(book.getIsbn(), "ISBN is null for a book");
	        Assert.assertNotNull(book.getTitle(), "Title is null for a book");
	        Assert.assertNotNull(book.getAuthor(), "Author is null for a book");
	    }
	}
}
