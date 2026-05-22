package utils;

import java.util.*;

import org.testng.Assert;

import entities.Pet;
import io.restassured.response.Response;

public class Assertion {
	public static void assertGetBookListApi(Response res) {
		//@SuppressWarnings("unchecked")
		List<Map<String,Object>> list=res.jsonPath().getList("books");
		Assert.assertTrue(list.size()>0);
		for(Map<String,Object> i:list) {
			Object isbn = i.get("isbn");
			Assert.assertTrue(isbn != null && isbn instanceof String, "ISBN must be a non-null string");
		}
	
			
	}
	
	public static void assertCategoryName(Response res,Class clazz,String categoryName) {
		Assert.assertEquals(POJOUtils.getPOJO(res, Pet.class).getCategory().getName(),categoryName);
	}
}
