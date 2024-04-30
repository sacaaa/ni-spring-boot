package com.esen.bookstore.service;

import com.esen.bookstore.model.Book;
import com.esen.bookstore.model.BookStore;
import com.esen.bookstore.repository.BookRepository;
import com.esen.bookstore.repository.BookstoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookstoreService bookstoreService;
    private final BookstoreRepository bookstoreRepository;

    public void save(Book book) {
        bookRepository.save(book);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public void deleteBook(Long id) {
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find book"));
        bookstoreService.removeBookFrom(book);
        bookRepository.delete(book);
    }

    public void updateBook(Long id, String title, String author, String publisher, Double price) {
        if (Stream.of(title, author, publisher, price).allMatch(Objects::isNull)) {
            throw new UnsupportedOperationException("There's nothing to update");
        }

        var book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find book"));

        if (title != null) {
            book.setTitle(title);
        }

        if (author != null) {
            book.setAuthor(author);
        }

        if (publisher != null) {
            book.setPublisher(publisher);
        }

        if (price != null) {
            book.setPrice(price);
        }

        bookRepository.save(book);
    }

    public List<BookStore> findBookstoresWithBookInInventory(Book book) {
        return bookstoreRepository.findAll().stream()
                .filter(bookstore -> bookstore.getInventory().containsKey(book))
                .toList();
    }

}
