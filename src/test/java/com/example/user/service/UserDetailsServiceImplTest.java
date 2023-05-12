package com.example.user.service;

import com.example.user.entity.User;
import com.example.user.repo.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {
    private static final String USER_NAME = "mockUser";
    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private UserDetailsImpl mockUserDetails;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_shouldReturnUserDetails() {
        User user = new User();
        when(mockUserRepository.findByUsername(eq(USER_NAME))).thenReturn(Optional.of(user));
        try (MockedStatic<UserDetailsImpl> mockUserDetailsImpl = Mockito.mockStatic(UserDetailsImpl.class)) {
            mockUserDetailsImpl.when(() -> UserDetailsImpl.build(any()))
                    .thenReturn(mockUserDetails);
            UserDetails result = userDetailsService.loadUserByUsername(USER_NAME);
            assertEquals(mockUserDetails, result);
        }
    }

    @Test
    void loadUserByUsername_shouldThrowException() {
        when(mockUserRepository.findByUsername(eq(USER_NAME))).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(USER_NAME));
    }
}