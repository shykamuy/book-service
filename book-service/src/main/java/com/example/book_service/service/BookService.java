package com.example.book_service.service;

import com.example.book_service.configuration.properties.AppCacheProperties;
import com.example.book_service.model.Book;
import com.example.book_service.model.Category;
import com.example.book_service.repository.BookRepository;
import com.example.book_service.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Cacheable(value = AppCacheProperties.CacheNames.DB_BOOK_BY_AUTHOR_AND_NAME, key = "#author + #name")
    public Book findByAuthorAndName(String author, String name) {
        Book book = new Book();
        book.setAuthorName(author);
        book.setName(name);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "category");

        Example<Book> example = Example.of(book, matcher);

        return repository.findOne(example).orElseThrow();
    }

    @Cacheable(value = AppCacheProperties.CacheNames.DB_BOOK_BY_CATEGORY, key = "#category")
    public List<Book> findByCategory(String category) {
        Category probe = new Category();
        probe.setName(category);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("id", "bookList");

        Example<Category> example = Example.of(probe, matcher);

        probe = categoryRepository.findOne(example).orElseThrow();

        List<Book> bookList = new ArrayList<>();
        bookList = repository.findAllByCategoryId(probe.getId());
        return bookList;
    }


    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "BookByAuthorAndName", key = "#author + #name", allEntries = true),
                    @CacheEvict(cacheNames = "BookByCategory", key = "#category", allEntries = true)
            }
    )
    public Book create(Book book) {
        book.setCategory(categoryRepository.findByName(book.getCategory().getName()));
        return repository.save(book);
    }


    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "BookByAuthorAndName", key = "#author + #name", allEntries = true),
                    @CacheEvict(cacheNames = "BookByCategory", key = "#category", allEntries = true)
            }
    )
    public Book update(Long id, Book book) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
            book.setCategory(categoryRepository.findByName(book.getCategory().getName()));
            return repository.save(book);

        }
        return null;
    }

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "BookByAuthorAndName", key = "#author + #name", allEntries = true),
                    @CacheEvict(cacheNames = "BookByCategory", key = "#category", allEntries = true)
            }
    )
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
