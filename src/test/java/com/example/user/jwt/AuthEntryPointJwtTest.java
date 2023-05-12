package com.example.user.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthEntryPointJwtTest {

    @Mock
    HttpServletRequest mockRequest;

    @Mock
    HttpServletResponse mockResponse;

    @Mock
    AuthenticationException mockException;

    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    @Test
    void loadUserByUsername_shouldReturnUserDetails() throws IOException {
        authEntryPointJwt.commence(mockRequest, mockResponse, mockException);

        verify(mockResponse, times(1)).sendError(eq(HttpServletResponse.SC_UNAUTHORIZED), eq("Error: Unauthorized"));
    }

}