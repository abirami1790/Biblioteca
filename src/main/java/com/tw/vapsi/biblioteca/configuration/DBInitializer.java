package com.tw.vapsi.biblioteca.configuration;

import com.tw.vapsi.biblioteca.service.BookService;
import com.tw.vapsi.biblioteca.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBInitializer implements CommandLineRunner {

    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        bookService.populateInitialBookRecords();

    }
}
