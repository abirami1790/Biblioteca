package com.tw.vapsi.biblioteca.service;

import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.repository.BookCheckoutRepository;
import com.tw.vapsi.biblioteca.repository.UserRepository;
import com.tw.vapsi.biblioteca.service.dto.UserDetailsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserService userService;
    @Mock
    private BookCheckoutRepository bookCheckoutRepository;


    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, bCryptPasswordEncoder, bookCheckoutRepository);
    }

    @Test
    void shouldLoadUserDetailsFromUserName() {
        User user = new User(1L,
                "User 1 first name",
                "User 1 last name",
                "email@example.com",
                "password");
        UserDetails expectedUserDetails = UserDetailsDTO.create(user);
        when(userRepository.findByEmail("email@example.com")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("email@example.com");

        assertEquals(expectedUserDetails, userDetails);
    }

    @Test
    void shouldThrowUserNotFoundException() {
        when(userRepository.findByEmail("email@example.com")).thenReturn(Optional.empty());

        UsernameNotFoundException actualException = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("email@example.com")
        );

        assertEquals("No user exists with username : email@example.com", actualException.getMessage());
    }

    @Test
    void shouldSaveTheUserInformation() {
        User userToBeCreated = new User(
                "Micky",
                "Mouse",
                "micky-mouse@example.com",
                "encoded-password");


        User expectedUser = new User(
                1L,
                "Micky",
                "Mouse",
                "micky-mouse@example.com",
                "encoded-password");
        when(userRepository.save(userToBeCreated)).thenReturn(expectedUser);
        when(bCryptPasswordEncoder.encode("password")).thenReturn("encoded-password");

        User actualUser = userService.save("Micky", "Mouse", "micky-mouse@example.com", "password");

        assertEquals(expectedUser, actualUser);
        verify(userRepository, times(1)).save(userToBeCreated);
    }

    @Test
    void shouldReturnUserGivenEmail() {
        User user = new User("shelly", "s", "s@gmail.com", "123");
        user.setId(1L);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        User actualUser = userService.findByEmail("s@gmail.com");

        assertEquals(user, actualUser);
    }

    @Test
    void shouldThrowExceptionWhenEmailDoesNotExists() {
        when(userRepository.findByEmail("email@example.com")).thenReturn(Optional.empty());

        UsernameNotFoundException actualException = assertThrows(
                UsernameNotFoundException.class,
                () -> userService.findByEmail("email@example.com")
        );

        assertEquals("No user exists with email : email@example.com", actualException.getMessage());
    }
}