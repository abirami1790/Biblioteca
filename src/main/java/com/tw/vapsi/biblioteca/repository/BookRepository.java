package com.tw.vapsi.biblioteca.repository;

import com.tw.vapsi.biblioteca.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(value = "SELECT * FROM Book where Id\n" +
            "NOT IN (Select BOOK_ID from BookCheckout\n" +
            "Where BookCheckout.IS_RETURNED = false)", nativeQuery = true)
    List<Book> findAvailableBooks();
}
