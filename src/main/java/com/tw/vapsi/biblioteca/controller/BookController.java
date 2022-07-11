package com.tw.vapsi.biblioteca.controller;

import com.tw.vapsi.biblioteca.configuration.RefererAuthenticationSuccessHandler;
import com.tw.vapsi.biblioteca.exception.BookAlreadyReturnedException;
import com.tw.vapsi.biblioteca.model.Book;
import com.tw.vapsi.biblioteca.model.BookCheckout;
import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.service.BookCheckoutService;
import com.tw.vapsi.biblioteca.service.BookService;
import com.tw.vapsi.biblioteca.service.UserService;
import com.tw.vapsi.biblioteca.service.dto.UserDetailsDTO;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Controller
@RequestMapping
public class BookController {
    public static final String USER_ID = "userId";
    public static final String BOOKS = "books";
    private final BookService bookService;
    private final BookCheckoutService bookCheckoutService;
    private final UserService userService;

    public BookController(BookService bookService, BookCheckoutService bookCheckoutService, UserService userService) {
        this.bookService = bookService;
        this.bookCheckoutService = bookCheckoutService;
        this.userService = userService;
    }

    @GetMapping("/books")
    public String home(Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String email = loggedInUser instanceof AnonymousAuthenticationToken ? "" : loggedInUser.getName();

        List<Book> books;

        if (!email.isEmpty()) {
            User user = userService.findByEmail(email);
            model.addAttribute(USER_ID, user.getId());
            model.addAttribute("fullName", user.getFirstName() + " " + user.getLastName());
        } else {
            model.addAttribute(USER_ID, null);
        }

        books = bookService.getAvailableBooks();

        if (books.isEmpty()) {
            model.addAttribute("message", "Sorry! No books available in the library.");
        }

        model.addAttribute(BOOKS, books);
        return BOOKS;
    }

    @GetMapping(path = "/checkout")
    public String showBookCheckoutForm(Principal principal, HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        request.getSession().setAttribute(RefererAuthenticationSuccessHandler.REDIRECT_URL_SESSION_ATTRIBUTE_NAME, referer);

        return principal == null ? "login" : "redirect:/";
    }

    @GetMapping(path = "/checkoutBook/{id}")
    public String createBookCheckout(@PathVariable("id") long bookId, RedirectAttributes redirectAttributes) {
        try {
            Book book = bookService.getBookById(bookId);

            long userId = getCurrentUser().getId();

            BookCheckout bookCheckout = new BookCheckout(book.getId(), userId);
            bookCheckoutService.save(bookCheckout);

            redirectAttributes.addFlashAttribute("success", "Thank you! Enjoy the book.");
            return "redirect:/books";
        } catch (Exception exception) {
            redirectAttributes.addFlashAttribute("failure", exception.getMessage());
            return "redirect:/books";
        }
    }

    private User getCurrentUser() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String email = loggedInUser instanceof AnonymousAuthenticationToken ? "" : loggedInUser.getName();

        return userService.findByEmail(email);
    }

    @GetMapping("/returnBook")
    public String bookReturn(Model model) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String email = loggedInUser instanceof AnonymousAuthenticationToken ? "" : loggedInUser.getName();

        List<Book> books = new ArrayList<>();

        if(email.isEmpty()) return "login";

        User user = userService.findByEmail(email);
        books = bookCheckoutService.getCheckoutBooksOfUser(user.getId());

        model.addAttribute(USER_ID, user.getId());
        model.addAttribute("fullName", user.getFirstName() + " " + user.getLastName());

        if (books.isEmpty()) {
            model.addAttribute("message", "No books to return.");
        }
        model.addAttribute(BOOKS, books);
        return "book-return";
    }

    @PostMapping("/returnCheckOutBooks/{id}")
    public String returnCheckoutBooksByUser(@PathVariable("id") long bookId, @RequestParam long userId, RedirectAttributes redirectAttributes) {
        try {
            bookCheckoutService.updateCheckoutBook(bookId, userId);
            redirectAttributes.addFlashAttribute("success", "Book is returned successfully.");
        } catch (BookAlreadyReturnedException e) {
            redirectAttributes.addFlashAttribute("failure", e.getMessage());
        }
        return "redirect:/returnBook";
    }
}
