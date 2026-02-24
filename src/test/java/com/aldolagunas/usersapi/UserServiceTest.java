package com.aldolagunas.usersapi;

import com.aldolagunas.usersapi.exception.DuplicateTaxIdException;
import com.aldolagunas.usersapi.model.User;
import com.aldolagunas.usersapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void testGetAllUsers_withSorting() {
        List<User> users = userService.getAllUsers("name", null);
        assertNotNull(users);
        assertTrue(users.size() >= 3);
    }

    @Test
    void testGetAllUsers_withFilter() {
        List<User> users = userService.getAllUsers(null, "name+co+user");
        assertTrue(users.size() > 0);
    }

    @Test
    void testAuthenticate_success() {
        assertTrue(userService.authenticate("AARR990101XXX", "7c4a8d09ca3762af61e59520943dc26494f8941b"));
    }

    @Test
    void testAuthenticate_failure() {
        assertFalse(userService.authenticate("AARR990101XXX", "wrongpassword"));
    }

    @Test
    void testCreateUser_duplicateTaxId() {
        User user = User.builder()
                .email("dup@mail.com")
                .name("dup")
                .phone("+1 55 999 999 99")
                .password("secret")
                .taxId("AARR990101XXX")
                .addresses(List.of())
                .build();
        assertThrows(DuplicateTaxIdException.class, () -> userService.createUser(user));
    }

    @Test
    void testUpdateUser_duplicateTaxId() {
        User user = User.builder()
                .email("new@mail.com")
                .name("new")
                .phone("+1 55 888 888 88")
                .password("secret")
                .taxId("ZZZZ990101AAA")
                .addresses(List.of())
                .build();
        User created = userService.createUser(user);
        assertThrows(DuplicateTaxIdException.class, () ->
                userService.updateUser(created.getId(), Map.of("tax_id", "AARR990101XXX")));
    }

    @Test
    void testFilter_invalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> userService.getAllUsers(null, "invalid-filter"));
    }

    @Test
    void testDeleteUser() {
        User user = User.builder()
                .email("del@mail.com")
                .name("del")
                .phone("+1 55 777 777 77")
                .password("secret")
                .taxId("ZXXX990101BBB")
                .addresses(List.of())
                .build();
        User created = userService.createUser(user);
        userService.deleteUser(created.getId());
        assertFalse(userService.getAllUsers(null, null).stream().anyMatch(u -> u.getId().equals(created.getId())));
    }
}