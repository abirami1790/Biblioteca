package com.tw.vapsi.biblioteca.service;

import com.tw.vapsi.biblioteca.exception.UserException;
import com.tw.vapsi.biblioteca.model.User;
import com.tw.vapsi.biblioteca.repository.BookCheckoutRepository;
import com.tw.vapsi.biblioteca.repository.UserRepository;
import com.tw.vapsi.biblioteca.service.dto.UserDetailsDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    public final String EMAIL_ALREADY_EXISTS = "User with same email address already exists.";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final BookCheckoutRepository bookCheckoutRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, BookCheckoutRepository bookCheckoutRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.bookCheckoutRepository = bookCheckoutRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(UserDetailsDTO::create)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user exists with username : %s", username)));
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user exists with email : %s", email)));
    }


    public User save(String firstName, String lastName, String email, String password) {
        Optional<User> userExists = userRepository.findByEmail(email);
        if(userExists.isPresent()) throw new UserException(EMAIL_ALREADY_EXISTS);

        String encodePassword = bCryptPasswordEncoder.encode(password);
        User user = new User(firstName, lastName, email, encodePassword);
        return userRepository.save(user);
    }

}
