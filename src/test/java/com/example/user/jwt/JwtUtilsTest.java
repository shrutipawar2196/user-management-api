package com.example.user.jwt;

import com.example.user.service.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class JwtUtilsTest {

    private static final String SAMPLE_USERNAME = "mockUsername";
    private static final String SAMPLE_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtb2NrVXNlcm5hbWUiLCJpYXQiOjE2ODM5MTA4NDIsImV4cCI6MTY4Mzk5NzI0Mn0.EJr-9ohH8BPk5pJ7Yj1BcFjX8_yAfGoZ4EdaVp77gZ5jrRQcqJs-1mzs6qU3lHGKWlVU8HFkvrXEhSoTbsnhOw";

    @Mock
    Authentication mockAuthentication;

    @Mock
    UserDetailsImpl mockUserDetailsImpl;

    @InjectMocks
    JwtUtils jwtUtils;

    @BeforeEach
    public void beforeEach() {
        when(mockAuthentication.getPrincipal()).thenReturn(mockUserDetailsImpl);
        when(mockUserDetailsImpl.getUsername()).thenReturn(SAMPLE_USERNAME);

        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "mySecretKeymySecretKeymySecretKeymySecretKeymySecretKeymySecretKeymySecretKeymySecretKey");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 86400000);
    }

    @Test
    void generateJwtToken() {
        String actual = jwtUtils.generateJwtToken(mockAuthentication);
        String expected = new String(actual);

        assertEquals(expected, actual);
    }

    @Test
    void getUserNameFromJwtToken() {
        String actual = jwtUtils.getUserNameFromJwtToken(SAMPLE_TOKEN);

        assertEquals(SAMPLE_USERNAME, actual);
    }

    @Test
    void validateJwtToken_shouldReturnTrue() {
        boolean actual = jwtUtils.validateJwtToken(SAMPLE_TOKEN);

        assertTrue(actual);
    }

    @Test
    void validateJwtToken_shouldReturnFalseOnInvalidToken() {
        boolean actual = jwtUtils.validateJwtToken("sampleInvalidToken");

        assertFalse(actual);
    }
}