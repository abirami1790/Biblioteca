package com.tw.vapsi.biblioteca.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BookCheckoutTest {

    @Test
    void shouldEquateBookCheckoutWithSameDetails() {
        final BookCheckout bookCheckout = new BookCheckout(1L, 2L, 3L);
        final BookCheckout anotherBookCheckout = new BookCheckout(1L, 2L, 3L);

        assertEquals(bookCheckout, anotherBookCheckout);
    }

    @Test
    void shouldNotEquateBookCheckoutWithDifferentDetails() {
        final BookCheckout bookCheckout = new BookCheckout(1L, 2L, 3L);
        final BookCheckout anotherBookCheckout = new BookCheckout(1L, 2L, 4L);

        assertNotEquals(bookCheckout, anotherBookCheckout);
    }

    @Test
    void shouldNotEquateBookCheckoutWithDifferentClass() {
        final BookCheckout bookCheckout = new BookCheckout(1L, 2L, 3L);

        assertNotEquals("anotherBookCheckout", bookCheckout);
    }

    @Test
    void shouldNotEquateBookCheckoutWithNull() {
        final BookCheckout bookCheckout = new BookCheckout(1L, 2L, 3L);

        assertNotEquals(null, bookCheckout);
    }

    @Test
    void shouldEquateHashcodeOfEqualBookCheckout() {
        final BookCheckout bookCheckout = new BookCheckout(1L, 2L, 3L);
        final BookCheckout anotherBookCheckout = new BookCheckout(1L, 2L, 3L);

        assertEquals(bookCheckout.hashCode(), anotherBookCheckout.hashCode());
    }

    @Test
    void shouldSetProperValuesThroughSetters() {
        final BookCheckout bookCheckout = new BookCheckout();
        bookCheckout.setId(1L);
        bookCheckout.setBookId(2L);
        bookCheckout.setUserId(3L);
        final BookCheckout anotherBookCheckout = new BookCheckout(1L, 2L, 3L);

        assertEquals(bookCheckout, anotherBookCheckout);
    }
}