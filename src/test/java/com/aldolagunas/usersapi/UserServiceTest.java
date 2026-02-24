package com.aldolagunas.usersapi;

import com.aldolagunas.usersapi.model.User;
import com.aldolagunas.usersapi.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    void testGetAllUsers_withSorting() {
        List<User> users = userService.getAllUsers("name", null);
        assertNotNull(users);
        assertEquals(3, users.size());
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
}