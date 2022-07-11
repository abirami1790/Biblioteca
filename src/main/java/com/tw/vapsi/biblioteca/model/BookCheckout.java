package com.tw.vapsi.biblioteca.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "bookcheckout")
public class BookCheckout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long bookId;

    private long userId;


    private boolean isReturned;

    public BookCheckout() {
    }

    public BookCheckout(long bookId, long userId) {
        this.bookId = bookId;
        this.userId = userId;
    }

    public BookCheckout(long id, long bookId, long userId) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public void setReturned(boolean returned) {
        isReturned = returned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookCheckout that = (BookCheckout) o;
        return id == that.id && bookId == that.bookId && userId == that.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookId, userId);
    }
}
