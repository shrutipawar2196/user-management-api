package com.example.user.config;

import com.example.user.jwt.AuthEntryPointJwt;
import com.example.user.jwt.AuthTokenFilter;
import com.example.user.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WebSecurityConfigTest {

    @Mock
    UserDetailsServiceImpl mockUserDetailsService;

    @Mock
    AuthEntryPointJwt mockAuthEntryPointJwt;

    @InjectMocks
    WebSecurityConfig webSecurityConfig;

    @Test
    void authenticationJwtTokenFilter() {
        AuthTokenFilter actual = webSecurityConfig.authenticationJwtTokenFilter();
        assertInstanceOf(AuthTokenFilter.class, actual);
    }

    @Test
    void authenticationProvider() {
        DaoAuthenticationProvider actual = webSecurityConfig.authenticationProvider();

        assertInstanceOf(DaoAuthenticationProvider.class, actual);
    }

    @Test
    void authenticationManager() throws Exception {
        AuthenticationManager mockAuthManager = mock(AuthenticationManager.class);
        AuthenticationConfiguration mockAuthConfig = mock(AuthenticationConfiguration.class);
        when(mockAuthConfig.getAuthenticationManager()).thenReturn(mockAuthManager);
        AuthenticationManager actual = webSecurityConfig.authenticationManager(mockAuthConfig);

        assertEquals(mockAuthManager, actual);
    }

    @Test
    void passwordEncoder() {
        PasswordEncoder actual = webSecurityConfig.passwordEncoder();

        assertInstanceOf(BCryptPasswordEncoder.class, actual);
    }
}