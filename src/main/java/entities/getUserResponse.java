package entities;

import java.util.List;

import entities.getBooksResponse.Book;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class getUserResponse {
	private String userId;
	private String username;
	//private getBooksResponse books;
	
	private List<Book> books;
}
