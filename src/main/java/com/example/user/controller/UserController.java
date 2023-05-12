package com.example.user.controller;

import com.example.user.NotFoundException;
import com.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.user.entity.User;

import java.util.List;

@RestController
//@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUser() {
        return userService.getAll();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        try {
            User user = userService.findByUsername(username);
            return ResponseEntity.ok(user);
        }
        catch (Exception ignored){}

        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> updateUserByUsername(@PathVariable String username, @RequestBody User user) {
        User newUser = userService.updateUser(username, user);
        return ResponseEntity.ok(newUser);
    }

    @PatchMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<User> updateCurrentUser(Authentication authentication, @RequestBody User user) {
        String username = authentication.getName();
        User newUser = userService.updateUser(username, user);
        return ResponseEntity.ok(newUser);
    }

    @DeleteMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> deleteUserByUsername(@PathVariable String username) {
        try {
            userService.deleteUser(username);
        } catch (NotFoundException e) {
            ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
}
