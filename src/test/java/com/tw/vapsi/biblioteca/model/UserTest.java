package com.tw.vapsi.biblioteca.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserTest {

    @Test
    void shouldEquateUserWithSameDetails() {
        final User user = new User(1L, "poonam", "shelke", "p.s@gmail.com", "P4ssword");
        final User anotherUser = new User(1L, "poonam", "shelke", "p.s@gmail.com", "P4ssword");

        assertEquals(user, anotherUser);
    }

    @Test
    void shouldEquateUserHashcodeWhenUsersAreEqual() {
        final User user = new User(1L, "poonam", "shelke", "p.s@gmail.com", "P4ssword");
        final User anotherUser = new User(1L, "poonam", "shelke", "p.s@gmail.com", "P4ssword");

        assertEquals(user.hashCode(), anotherUser.hashCode());
    }

    @Test
    void shouldNotEquateUserWhenUsersAreDifferent() {
        final User user = new User(1L, "poonam", "shelke", "p.s@gmail.com", "P4ssword");
        final User anotherUser = new User(2L, "poonam", "shelke", "p.s@gmail.com", "P4ssword");

        assertNotEquals(user, anotherUser);
    }

    @Test
    void shouldNotEquateUserWithNull() {
        final User user = new User(1L, "poonam", "shelke", "p.s@gmail.com", "P4ssword");

        assertNotEquals(null, user);
    }

    @Test
    void shouldNotEquateUserWithDifferntClass() {
        final User user = new User(1L, "poonam", "shelke", "p.s@gmail.com", "P4ssword");

        //noinspection AssertBetweenInconvertibleTypes
        assertNotEquals("null", user);
    }

    @Test
    void shouldSetProperValuesOfUserThroughSetters() {
        final User user = new User();
        user.setId(1L);
        user.setEmail("p.s@gmail.com");
        user.setFirstName("poonam");
        user.setLastName("shelke");
        user.setPassword("P4ssword");

        final User anotherUser = new User(1L, "poonam", "shelke", "p.s@gmail.com", "P4ssword");

        assertEquals(user, anotherUser);
    }
}