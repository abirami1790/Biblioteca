package com.tw.vapsi.biblioteca.controller;

import com.tw.vapsi.biblioteca.exception.BookAlreadyReturnedException;
import com.tw.vapsi.biblioteca.exception.BookNotAvailableException;
import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.model.BookCheckout;
import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.service.BookCheckoutService;
import com.tw.vapsi.biblioteca.service.BookService;
import com.tw.vapsi.biblioteca.service.UserService;
import com.tw.vapsi.biblioteca.service.dto.UserDetailsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private UserService userService;

    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @MockBean
    private BookCheckoutService bookCheckoutService;

    @Test
    void shouldReturnAllAvailableBooksForCheckout() throws Exception {
        List<Book> books = Arrays.asList(new Book(1, "Harry Potter", "J.K Rowling"));

        when(bookService.getAvailableBooks()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"))
                .andExpect(model().attribute("books", books))
                .andExpect(model().attribute("books", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("name", is("Harry Potter")),
                                hasProperty("authorName", is("J.K Rowling"))
                        )
                )));
        verify(bookService, times(1)).getAvailableBooks();
    }

    @Test
    void shouldReturnBooksNotAvailableWhenNoBooksInLibrary() throws Exception {
        List<Book> books = new ArrayList<>();
        when(bookService.getAvailableBooks()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(view().name("books"));

        verify(bookService, times(1)).getAvailableBooks();
    }

    @Test
    void shouldReturnLoginFormWhileCheckingOutABook() throws Exception {
        mockMvc.perform(get("/checkout"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void shouldUpdateReturnStatusOfCheckoutBook() throws Exception, BookAlreadyReturnedException {

        mockMvc.perform(post("/returnCheckOutBooks/{id}", 1l)
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(redirectedUrl("/returnBook")).andExpect(status().isFound());

        verify(bookCheckoutService, times(1)).updateCheckoutBook(1l, 1l);
    }

    @Test
    void shouldCreateBookCheckoutForUser() throws Exception {
        Book book = new Book("Think like a monk", "Jay Shetty");
        book.setId(1L);

        when(bookService.getBookById(anyLong())).thenReturn(book);

        User user = new User("Shelly", "Suri", "ss@gmail.com", "123");
        user.setId(2L);

        when(userService.findByEmail(anyString())).thenReturn(user);

        BookCheckout bookCheckoutToBeCreated = new BookCheckout(1L, 2L);
        BookCheckout bookCheckoutCreated = new BookCheckout(1L, 2L);
        bookCheckoutCreated.setId(1L);

        when(bookCheckoutService.save(bookCheckoutToBeCreated)).thenReturn(bookCheckoutCreated);

        mockMvc.perform(get("/checkoutBook/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/books"));

        verify(userService, times(1)).findByEmail(anyString());
        verify(bookService, times(1)).getBookById(anyLong());
        verify(bookCheckoutService, times(1)).save(bookCheckoutToBeCreated);
    }

    @Test
    void shouldReturnCheckedOutBooksAndTheReturnBooksViewWhenCalledWithReturnApi() throws Exception {
        User user = new User(1L, "poonam","s" , "p@gmail.com","p4assword");
        when(userService.findByEmail("p@gmail.com")).thenReturn(user);

        final Book book = new Book(2L, "Wings of fire", "Abdul kalam");
        final Book anotherBook = new Book(3L, "Wings of fire", "Abdul kalam");
        final List<Book> books = Arrays.asList(book, anotherBook);
        when(bookCheckoutService.getCheckoutBooksOfUser(1L)).thenReturn(books);
        mockMvc.perform(get("/returnBook")
                        .with(user("p@gmail.com")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", books))
                .andExpect(view().name("book-return"));

    }

    @Test
    void shouldHaveErrorMessageWhenThereAreNoBooksToReturn() throws Exception {
        User user = new User(12L, "poonam","s" , "p@gmail.com","p4assword");
        when(userService.findByEmail("p@gmail.com")).thenReturn(user);

        final List<Book> books = new ArrayList<>();
        when(bookCheckoutService.getCheckoutBooksOfUser(1L)).thenReturn(books);
        mockMvc.perform(get("/returnBook")
                        .with(user("p@gmail.com")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("books", books))
                .andExpect(model().attribute("message", "No books to return."))
                .andExpect(view().name("book-return"));
    }

    @Test
    void shouldHaveUserIdWhenBookListApiIsAccessedByLoggedInUser() throws Exception {
        User user = new User(12L, "poonam","s" , "p@gmail.com","p4assword");
        when(userService.findByEmail("p@gmail.com")).thenReturn(user);

        final List<Book> books = new ArrayList<>();
        when(bookCheckoutService.getCheckoutBooksOfUser(1L)).thenReturn(books);
        mockMvc.perform(get("/books")
                        .with(user("p@gmail.com")))
                .andExpect(status().isOk())
                .andExpect(model().attribute("userId", 12L))
                .andExpect(view().name("books"));
    }
    @Test
    void shouldHaveErrorMessageWhenCheckoutIsUnsuccessful() throws Exception {
        UserDetailsDTO user = new UserDetailsDTO(12L, "poonam","p4assword", new ArrayList<>());
        when(userService.loadUserByUsername("poonam")).thenReturn(user);

        final Book book = new Book(1L, "Wings of fire", "Abdul kalam");
        final String failure_message = "book not available";
        when(bookService.getBookById(1L)).thenThrow(new BookNotAvailableException(failure_message));
        when(bookCheckoutService.save(new BookCheckout(1L,12L))).thenThrow(new BookNotAvailableException(failure_message));

        mockMvc.perform(get("/checkoutBook/{id}", 1L)
                        .with(user(user)))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("failure",failure_message))
                .andExpect(redirectedUrl("/books"))
        ;
    }

    @Test
    void shouldHaveErrorMessageWhenReturnIsUnsuccessful() throws Exception, BookAlreadyReturnedException {
        doThrow(new BookAlreadyReturnedException(BookCheckoutService.BOOK_ALREADY_RETURNED)).when(bookCheckoutService).updateCheckoutBook(1L, 1L);

        mockMvc.perform(post("/returnCheckOutBooks/{id}", 1L)
                        .param("userId", "1"))
                .andExpect(flash().attribute("failure", BookCheckoutService.BOOK_ALREADY_RETURNED))
                .andExpect(redirectedUrl("/returnBook"));
    }
}