package com.tw.vapsi.biblioteca.repository;

import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.model.BookCheckout;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    BookCheckoutRepository bookCheckoutRepository;

    @Test
    void shouldReturnNotCheckedOutBookByUser() {
        Book book1 = new Book("Eat that frog", "Brian Tacy");
        Book book2 = new Book("The Secret", "Rhonda Byrne");

        bookRepository.save(book1);
        bookRepository.save(book2);

        BookCheckout bookCheckout = new BookCheckout(book1.getId(), 1L);
        bookCheckoutRepository.save(bookCheckout);

        Optional<List<Book>> books = Optional.ofNullable(bookRepository.findAvailableBooks());

        assertTrue(books.isPresent());
        assertFalse(books.get().contains(book1));
    }
}