package com.esen.bookstore.service;

import com.esen.bookstore.model.Book;
import com.esen.bookstore.model.BookStore;
import com.esen.bookstore.repository.BookstoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BookstoreService {

    private final BookstoreRepository bookstoreRepository;

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
                .orElseThrow(() -> new RuntimeException("Cannot find book"));
        bookstoreRepository.delete(bookstore);
    }

    public void updateBook(Long id, String location, Double priceModifier, Double moneyInCashRegister) {
        if (Stream.of(location, priceModifier, moneyInCashRegister).allMatch(Objects::isNull)) {
            throw new UnsupportedOperationException("There's nothing to update");
        }

        var bookstore = bookstoreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cannot find book"));

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
}
