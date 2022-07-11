package com.tw.vapsi.biblioteca.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class BookTest {

    @Test
    void shouldEquateBooksWithSameDetails() {
        final Book book = new Book(1L, "famous book", "famous author");
        final Book anotherBook = new Book(1L, "famous book", "famous author");

        assertEquals(book, anotherBook);
    }

    @Test
    void shouldNotEquateBooksWithDifferentDetails() {
        final Book book = new Book(1L, "famous book", "famous author");
        final Book anotherBook = new Book(2L, "famous book", "famous author");

        assertNotEquals(book, anotherBook);
    }

    @Test
    void shouldNotEquateBookWithNull() {
        final Book book = new Book(1L, "famous book", "famous author");

        assertNotEquals(null, book);

    }

    @Test
    void shouldNotEquateBookWithOtherClassObject() {
        final Book book = new Book(1L, "famous book", "famous author");

        //noinspection AssertBetweenInconvertibleTypes
        assertNotEquals(book, "null");

    }

    @Test
    void shouldProperlySetValuesOfBookThroughSetters() {
        final Book book = new Book();
        book.setId(1L);
        book.setAuthorName("I am famous");
        book.setName("I am bestselling");
        final Book anotherBook = new Book(1L, "I am bestselling", "I am famous");

        assertEquals(book, anotherBook);
    }

    @Test
    void shouldProperlyGetValuesOfBookThroughGetters() {
        final long id = 1L;
        final String name = "I am bestselling";
        final String authorName = "I am famous";
        final Book book = new Book(id, name, authorName);

        assertEquals(book.getId(), id);
        assertEquals(book.getName(), name);
        assertEquals(book.getAuthorName(), authorName);
    }

    @Test
    void shouldEquateHashcodeOfEqualBooks() {
        final Book book = new Book(1L, "famous book", "famous author");
        final Book anotherBook = new Book(1L, "famous book", "famous author");

        assertEquals(book.hashCode(), anotherBook.hashCode());
    }
}