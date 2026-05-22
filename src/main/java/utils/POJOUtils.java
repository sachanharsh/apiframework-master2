package utils;

import io.restassured.response.Response;

public class POJOUtils {
	
	public static <T>T getPOJO(Response res,Class<T> clazz){
		return res.as(clazz);
	}
}
