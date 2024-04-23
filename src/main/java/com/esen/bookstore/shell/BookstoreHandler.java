package com.esen.bookstore.shell;

import com.esen.bookstore.model.BookStore;
import com.esen.bookstore.service.BookstoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.stream.Collectors;

@ShellComponent
@ShellCommandGroup("Bookstore related commands")
@RequiredArgsConstructor
public class BookstoreHandler {

    private final BookstoreService bookstoreService;

    @ShellMethod(key = "create_bookstore", value = "Create a bookstore")
    public void createBookstore(String location, Double priceModifier, Double moneyInCashRegister) {
        bookstoreService.save(BookStore.builder()
                .location(location)
                .priceModifier(priceModifier)
                .moneyInCashRegister(moneyInCashRegister)
                .build());
    }

    @ShellMethod(key = "list_bookstores", value = "List bookstores")
    public String listBookstores() {
        return bookstoreService.findAll().stream()
                .map(bookstore -> "ID: %d, Location: %s, PriceModifier %fx, MoneyInCashRegister %f Ft".formatted(
                        bookstore.getId(), bookstore.getLocation(), bookstore.getPriceModifier(), bookstore.getMoneyInCashRegister()
                )).collect(Collectors.joining(System.lineSeparator()));
    }

    @ShellMethod(key = "delete_bookstore", value = "Delete a bookstore")
    public void deleteBookstore(Long id) {
        bookstoreService.deleteBook(id);
    }

    @ShellMethod(key = "update_bookstore", value = "Update a bookstore")
    public void updateBookstore(Long id,
                           @ShellOption(defaultValue = ShellOption.NULL) String location,
                           @ShellOption(defaultValue = ShellOption.NULL) Double priceModifier,
                           @ShellOption(defaultValue = ShellOption.NULL) Double moneyInCashRegister) {
        bookstoreService.updateBook(id, location, priceModifier, moneyInCashRegister);
    }

}
