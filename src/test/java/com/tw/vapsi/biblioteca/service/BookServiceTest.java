package com.tw.vapsi.biblioteca.service;

import com.tw.vapsi.biblioteca.exception.BookNotFoundException;
import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.repository.BookRepository;
import com.tw.vapsi.biblioteca.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.tw.vapsi.biblioteca.service.BookService.BOOK_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookServiceTest {

    @Autowired
    BookService bookService;
    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void returnAllBooks_Success() {
        List<Book> books = Arrays.asList(new Book(1, "abcd", "aaaabbb"), new Book(2, "eeef", "fgfgfg"));
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> allbooks = bookService.getBooks();

        assertEquals(books, allbooks);
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void saveBook_success() {
        Book BookTobeCreated = new Book("Book1", "Book1author");
        Book BookCreated = new Book("Book1", "Book1author");
        BookCreated.setId(1);
        when(bookRepository.save(any())).thenReturn(BookCreated);

        Book Book = bookService.save(BookTobeCreated);
        assertEquals(1, Book.getId());

        verify(bookRepository, times(1)).save(BookTobeCreated);
    }

    @Test
    void shouldGetBookWhenIdIsGiven() {
        final Book expectedBook = new Book(1L, "title", "author");
        when(bookRepository.findById(1L)).thenReturn(Optional.of(expectedBook));

        final Book book = bookService.getBookById(1L);

        assertEquals(book, expectedBook);
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenIdGivenIsNotValid() {
        when(bookRepository.findById(1L)).thenThrow(new BookNotFoundException(BOOK_NOT_FOUND));

        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(1L));
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void shouldReturnNotCheckedOutBookByUser() {
        List<Book> books = Arrays.asList(new Book(1, "Psychology Of Money", "Morgan Housel"), new Book(2, "Atomic Habits", "James clear"));
        when(bookRepository.findAvailableBooks()).thenReturn(books);

        User user = new User("Shelly", "S", "ss@gmail.com", "123");
        user.setId(1L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        final List<Book> booksReturned = bookService.getAvailableBooks();
        assertEquals(books, booksReturned);
    }

}