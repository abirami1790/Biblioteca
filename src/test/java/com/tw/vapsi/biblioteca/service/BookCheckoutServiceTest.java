package com.tw.vapsi.biblioteca.service;

import com.tw.vapsi.biblioteca.exception.BookAlreadyReturnedException;
import com.tw.vapsi.biblioteca.exception.BookNotAvailableException;
import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.model.BookCheckout;
import com.tw.vapsi.biblioteca.repository.BookCheckoutRepository;
import com.tw.vapsi.biblioteca.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.tw.vapsi.biblioteca.service.BookCheckoutService.BOOK_IS_ALREADY_CHECKEDOUT;
import static com.tw.vapsi.biblioteca.service.BookCheckoutService.BOOK_NOT_AVAILABLE_FOR_CHECKOUT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class BookCheckoutServiceTest {

    @Autowired
    BookCheckoutService bookCheckoutService;

    @MockBean
    private BookCheckoutRepository bookCheckoutRepository;

    @MockBean
    private BookRepository bookRepository;

    @Test
    void shouldCreateBookCheckout() throws Exception {
        BookCheckout bookCheckoutToBeCreated = new BookCheckout(1L, 2L);
        BookCheckout bookCheckoutCreated = new BookCheckout(1L, 2L);
        bookCheckoutCreated.setId(1L);

        when(bookCheckoutRepository.findByBookIdAndUserId(1L, 1L)).thenReturn(null);
        when(bookCheckoutRepository.findByBookIdAndIsReturned(1L, false)).thenReturn(null);
        when(bookCheckoutRepository.save(bookCheckoutToBeCreated)).thenReturn(bookCheckoutCreated);

        BookCheckout actualBookCheckout = bookCheckoutService.save(bookCheckoutToBeCreated);

        assertEquals(bookCheckoutCreated, actualBookCheckout);
        verify(bookCheckoutRepository, times(1)).findByBookIdAndIsReturned(1L, false);
        verify(bookCheckoutRepository, times(1)).findByBookIdAndUserId(anyLong(), anyLong());
        verify(bookCheckoutRepository, times(1)).save(any(BookCheckout.class));
    }

    @Test
    void shouldThrowErrorWhenBookIsAlreadyCheckedOutByGivenUser() throws Exception {
        BookCheckout bookCheckoutExists = new BookCheckout(1L, 1L, 2L);
        BookCheckout bookCheckoutCreated = new BookCheckout(1L, 2L);

        when(bookCheckoutRepository.findByBookIdAndUserId(anyLong(), anyLong())).thenReturn(bookCheckoutExists);

        Throwable exception = assertThrows(BookNotAvailableException.class, () -> {
            bookCheckoutService.save(bookCheckoutCreated);
        });

        assertEquals(BOOK_IS_ALREADY_CHECKEDOUT, exception.getMessage());
        verify(bookCheckoutRepository, times(1)).findByBookIdAndUserId(anyLong(), anyLong());
    }

    @Test
    void shouldThrowExceptionWhenBookIsAlreadyCheckedOutByOtherUser() throws Exception {
        BookCheckout bookCheckoutToBeCreated = new BookCheckout(1L, 2L);
        BookCheckout bookCheckoutCreated = new BookCheckout(1L, 2L);
        bookCheckoutCreated.setId(1L);

        when(bookCheckoutRepository.findByBookIdAndIsReturned(1L, false)).thenReturn(new BookCheckout());
        when(bookCheckoutRepository.save(bookCheckoutToBeCreated)).thenReturn(bookCheckoutCreated);

        Throwable exception = assertThrows(BookNotAvailableException.class, () -> {
            bookCheckoutService.save(bookCheckoutToBeCreated);
        });

        assertEquals(BOOK_NOT_AVAILABLE_FOR_CHECKOUT, exception.getMessage());
        verify(bookCheckoutRepository, times(1)).findByBookIdAndIsReturned(1L, false);
    }

    @Test
    void shouldReturnCheckedOutBooksByUser() throws Exception {
        BookCheckout bookCheckout1 = new BookCheckout(1L, 1L, 1L);
        BookCheckout bookCheckout2 = new BookCheckout(1L, 2L, 2L);
        List<BookCheckout> bookCheckouts = Arrays.asList(bookCheckout1, bookCheckout2);

        when(bookCheckoutRepository.findByUserIdAndIsReturnedFalse(1L)).thenReturn(Arrays.asList(bookCheckout1));

        Book book1 = new Book(1L, "The Secret", "Rhonda Byrne");
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book1));

        List<Book> books = bookCheckoutService.getCheckoutBooksOfUser(1L);

        assertTrue(books.contains(book1));

        verify(bookCheckoutRepository, times(1)).findByUserIdAndIsReturnedFalse(1L);
    }

    @Test
    void shouldUpdateBookCheckoutReturnFlag() throws Exception, BookAlreadyReturnedException {
        BookCheckout bookCheckoutToBeUpdated = new BookCheckout(1L, 1L, 1L);
        BookCheckout bookCheckoutUpdated = new BookCheckout(1L, 1L, 1L);

        when(bookCheckoutRepository.findByBookIdAndUserId(anyLong(), anyLong())).thenReturn(bookCheckoutToBeUpdated);

        bookCheckoutUpdated.setReturned(true);

        when(bookCheckoutRepository.save(any(BookCheckout.class))).thenReturn(bookCheckoutUpdated);

        bookCheckoutService.updateCheckoutBook(1L, 1L);

        verify(bookCheckoutRepository, times(1)).findByBookIdAndUserId(anyLong(), anyLong());
        verify(bookCheckoutRepository, times(1)).save(bookCheckoutToBeUpdated);
    }

    @Test
    void shouldThrowExceptionWhenBookIsAlreadyReturned() throws Exception {
        BookCheckout bookCheckout = new BookCheckout(1l, 1L, 1L);
        bookCheckout.setReturned(true);

        when(bookCheckoutRepository.findByBookIdAndUserId(anyLong(), anyLong())).thenReturn(bookCheckout);

        Throwable exception = assertThrows(BookAlreadyReturnedException.class, () -> {
            bookCheckoutService.updateCheckoutBook(1L, 1L);
        });

        assertTrue(exception instanceof BookAlreadyReturnedException);
        verify(bookCheckoutRepository, times(1)).findByBookIdAndUserId(anyLong(), anyLong());
    }


}