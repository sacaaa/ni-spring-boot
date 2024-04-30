package com.esen.bookstore.service;

import com.esen.bookstore.model.Book;
import com.esen.bookstore.model.BookStore;
import com.esen.bookstore.repository.BookRepository;
import com.esen.bookstore.repository.BookstoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookstoreService {

    private final BookstoreRepository bookstoreRepository;
    private final BookRepository bookRepository;

    public void save(BookStore bookstore) {
        bookstoreRepository.save(bookstore);
    }

    public List<BookStore> findAll() {
        return bookstoreRepository.findAll();
    }

    @Transactional
    public void removeBookFrom(Book book) {
        bookstoreRepository.findAll()
                .forEach(bookstore -> {
                    bookstore.getInventory().remove(book);
                    bookstoreRepository.save(bookstore);
                });
    }

    public void deleteBook(Long id) {
        var bookstore = bookstoreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find bookstore"));
        bookstoreRepository.delete(bookstore);
    }

    public void updateBookstore(Long id, String location, Double priceModifier, Double moneyInCashRegister) {
        if (Stream.of(location, priceModifier, moneyInCashRegister).allMatch(Objects::isNull)) {
            throw new UnsupportedOperationException("There's nothing to update");
        }

        var bookstore = bookstoreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find bookstore"));

        if (location != null) {
            bookstore.setLocation(location);
        }

        if (priceModifier != null) {
            bookstore.setPriceModifier(priceModifier);
        }

        if (moneyInCashRegister != null) {
            bookstore.setMoneyInCashRegister(moneyInCashRegister);
        }

        bookstoreRepository.save(bookstore);
    }

    public Map<Book, Integer> getStock(Long id) {
        var bookstore = bookstoreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find bookstore"));

        return bookstore.getInventory();
    }

    public void addStock(Long bookstoreId, Long bookId, Long quantity) {
        if (quantity < 1) {
            throw new RuntimeException("Cannot add minus or zero quantity.");
        }

        var bookstore = bookstoreRepository.findById(bookstoreId)
                .orElseThrow(() -> new RuntimeException("Cannot find bookstore"));

        var book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Cannot find book"));

        boolean containsBook = bookstore.getInventory().containsKey(book);

        if (containsBook) {
            bookstore.getInventory().replace(
                    book, (int) (bookstore.getInventory().get(book) + quantity)
            );
        } else {
            bookstore.getInventory().put(book, Math.toIntExact(quantity));
        }

        bookstoreRepository.save(bookstore);
    }

}
