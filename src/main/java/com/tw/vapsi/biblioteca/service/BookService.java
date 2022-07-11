package com.tw.vapsi.biblioteca.service;

import com.tw.vapsi.biblioteca.exception.BookNotFoundException;
import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.repository.BookRepository;
import com.tw.vapsi.biblioteca.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class BookService {
    public static final String BOOK_NOT_FOUND = "Book not found.";
    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    public BookService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public Book getBookById(long id) throws BookNotFoundException {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND));
    }

    public void populateInitialBookRecords() {

        Book book1 = new Book("Eat that frog", "Brian Tacy");
        Book book2 = new Book("The Secret", "Rhonda Byrne");
        Book book3 = new Book("The Great Secret", "Rhonda Byrne");
        Book book4 = new Book("Harry Potter", "Rowling, J.K.");
        Book book5 = new Book("Angels and Demons", "Brown, Dan");
        Book book6 = new Book("Fifty Shades Darker", "James, E. L.");
        Book book7 = new Book("Lost Symbol,The", "Brown, Dan");
        Book book8 = new Book("Breaking Dawn", "Meyer, Stephenie");
        Book book9 = new Book("One Day", "Nicholls, David");
        Book book10 = new Book("Life of Pi", "Martel, Yann");



        List<Book> books = Arrays.asList(book1, book2, book3, book4, book5, book6, book7, book8, book9, book10);

        for (Book book : books) {
            this.save(book);
        }
    }

    public List<Book> getAvailableBooks() {
        return bookRepository.findAvailableBooks();
    }
}
