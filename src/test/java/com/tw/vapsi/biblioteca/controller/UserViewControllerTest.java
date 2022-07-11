package com.tw.vapsi.biblioteca.controller;

import com.tw.vapsi.biblioteca.exception.UserException;
import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserViewController.class)
class UserViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    void shouldReturnSignupFormWhenSignupUrlIsHit() throws Exception {
        mockMvc.perform(get("/signUp"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup-user"));
    }

    @Test
    void shouldCreateUser() throws Exception {
        String encodePassword = bCryptPasswordEncoder.encode("123");
        User userCreated = new User("Radha", "Singh", "rs@gmail.com", encodePassword);
        userCreated.setId(5L);

        when(userService.save(anyString(), anyString(), anyString(), anyString())).thenReturn(userCreated);

        mockMvc.perform(post("/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("firstName", "Radha")
                        .param("lastName", "Singh")
                        .param("email", "rs@gmail.com")
                        .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

        verify(userService, times(1)).save("Radha", "Singh", "rs@gmail.com", "123");
    }

    @Test
    void shouldReturnErrorWhenSignUpWithEmailAreadyExists() throws Exception {
        when(userService.save(anyString(), anyString(), anyString(), anyString())).thenThrow(new UserException(userService.EMAIL_ALREADY_EXISTS));

        mockMvc.perform(post("/createUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("firstName", "Radha")
                        .param("lastName", "Singh")
                        .param("email", "rs@gmail.com")
                        .param("password", "123"))
                .andExpect(view().name("signup-user"))
                .andExpect(model().attribute("failure", userService.EMAIL_ALREADY_EXISTS));

        verify(userService, times(1)).save(anyString(), anyString(), anyString(), anyString());
    }
}