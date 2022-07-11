package com.tw.vapsi.biblioteca.exception;

public class BookAlreadyReturnedException extends Throwable {
    public BookAlreadyReturnedException(String message) {
        super(message);
    }
}
