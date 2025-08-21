package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

import entities.getBooksResponse.Book;

@Data
@AllArgsConstructor
public class postbookRequest {
    private String userId;
    private List<Isbn> collectionOfIsbns;

    @Data
    @AllArgsConstructor
    public static class Isbn {
        private String isbn;
    }
}