package com.example.user.service;

import com.example.user.entity.Role;
import com.example.user.entity.User;
import com.example.user.userenum.ERole;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserDetailsImplTest {

    private static final Long SAMPLE_ID = 0L;
    private static final String SAMPLE_USERNAME = "mockUsername";
    private static final String SAMPLE_EMAIL = "mockEmail";
    private static final String SAMPLE_PASSWORD = "mockPassword";

    @Test
    void build() {
        Role role = new Role();
        role.setName(ERole.ROLE_ADMIN);
        User user = new User(SAMPLE_USERNAME, SAMPLE_EMAIL, SAMPLE_PASSWORD);
        user.setRoles(Set.of(role));
        user.setId(SAMPLE_ID);

        UserDetailsImpl result = UserDetailsImpl.build(user);

        assertEquals(SAMPLE_ID, result.getId());
        assertEquals(SAMPLE_USERNAME, result.getUsername());
        assertEquals(SAMPLE_EMAIL, result.getEmail());
        assertEquals(SAMPLE_PASSWORD, result.getPassword());
    }
}