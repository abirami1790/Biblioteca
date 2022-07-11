package com.tw.vapsi.biblioteca.service;

import com.tw.vapsi.biblioteca.exception.BookAlreadyReturnedException;
import com.tw.vapsi.biblioteca.exception.BookNotAvailableException;
import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.model.BookCheckout;
import com.tw.vapsi.biblioteca.repository.BookCheckoutRepository;
import com.tw.vapsi.biblioteca.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookCheckoutService {
    public static final String BOOK_NOT_AVAILABLE_FOR_CHECKOUT = "Book not available for checkout.";

    public static final String BOOK_IS_ALREADY_CHECKEDOUT= "The book is already checked out by you.";
    public static final String BOOK_ALREADY_RETURNED = "Book is already returned.";
    private final BookCheckoutRepository bookCheckoutRepository;
    private final BookRepository bookRepository;

    public BookCheckoutService(BookCheckoutRepository bookCheckoutDetailsRepository, BookRepository bookRepository) {
        this.bookCheckoutRepository = bookCheckoutDetailsRepository;
        this.bookRepository = bookRepository;
    }

    public BookCheckout save(BookCheckout bookCheckout) throws BookNotAvailableException {
        BookCheckout bookCheckoutByGivenUser = bookCheckoutRepository.findByBookIdAndUserId(bookCheckout.getBookId(), bookCheckout.getUserId());

        if(bookCheckoutByGivenUser != null && bookCheckoutByGivenUser.isReturned()) {
            bookCheckoutByGivenUser.setReturned(false);
            return bookCheckoutRepository.save(bookCheckoutByGivenUser);
        }

        if (bookCheckoutByGivenUser != null && !bookCheckoutByGivenUser.isReturned())  throw  new BookNotAvailableException(BOOK_IS_ALREADY_CHECKEDOUT);

        BookCheckout bookCheckoutExists = bookCheckoutRepository.findByBookIdAndIsReturned(bookCheckout.getBookId(), false);
        if (bookCheckoutExists != null)
            throw new BookNotAvailableException(BOOK_NOT_AVAILABLE_FOR_CHECKOUT);

        return bookCheckoutRepository.save(bookCheckout);
    }


    public List<Book> getCheckoutBooksOfUser(long userId) {
        List<Book> bookList = new ArrayList<Book>();
        List<BookCheckout> bookCheckouts = bookCheckoutRepository.findByUserIdAndIsReturnedFalse(userId);

        bookCheckouts.forEach(bookCheckout -> {
            bookList.add(bookRepository.findById(bookCheckout.getBookId()).get());
        });
        return bookList;
    }

    public void updateCheckoutBook(long bookId, long userId) throws BookAlreadyReturnedException {
        BookCheckout bookCheckout = bookCheckoutRepository.findByBookIdAndUserId(bookId, userId);
        if (bookCheckout.isReturned()) throw new BookAlreadyReturnedException(BOOK_ALREADY_RETURNED);

        bookCheckout.setReturned(true);
        bookCheckoutRepository.save(bookCheckout);
    }
}
