package com.example.book_service.response;

import com.example.book_service.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {

    private Long id;

    private String name;

    private String author;

    private String category;

    public static BookResponse from(Book book) {
        return new BookResponse(book.getId(), book.getName(), book.getAuthorName(), book.getCategory().getName());
    }

}
