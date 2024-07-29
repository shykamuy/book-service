package com.example.book_service.controller;


import com.example.book_service.model.Book;
import com.example.book_service.response.BookResponse;
import com.example.book_service.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookService service;

    @GetMapping("/filter")
    public ResponseEntity<BookResponse> findByAuthorAndName(@RequestParam String author, @RequestParam String name) {
        return ResponseEntity.ok(BookResponse.from(service.findByAuthorAndName(author, name)));
    }


    @GetMapping
    public ResponseEntity<List<BookResponse>> findByCategory(@RequestParam String category) {
        List<BookResponse> bookResponses = new ArrayList<>();
        bookResponses = service.findByCategory(category).stream().map(BookResponse::from).toList();
        return ResponseEntity.ok(bookResponses);
    }

    @PostMapping
    public ResponseEntity<BookResponse> create(@RequestBody Book book) {
        return ResponseEntity.ok(BookResponse.from(service.create(book)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> update(@PathVariable Long id, @RequestBody Book book) {
        return ResponseEntity.ok(BookResponse.from(service.update(id, book)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BookResponse> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
