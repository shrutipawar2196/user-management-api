package com.example.user.controller;

import com.example.user.dto.SignupRequest;
import com.example.user.entity.Role;
import com.example.user.entity.User;
import com.example.user.jwt.JwtUtils;
import com.example.user.repo.RoleRepository;
import com.example.user.repo.UserRepository;
import com.example.user.userenum.ERole;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserRepository mockUserRepo;

    @Mock
    private AuthenticationManager mockAuthenticationManager;

    @Mock
    private RoleRepository mockRoleRepo;

    @Mock
    private PasswordEncoder mockEncoder;

    @Mock
    private JwtUtils mockJwtUtils;

    @InjectMocks
    private AuthController authController;

    private final Map<String, String> postMethodMappings = Map.of(
            "authenticateUser", "/signin",
            "registerUser", "/signup");

    @Test
    void testClassUrlMappings() {
        Class<AuthController> resourceClass = AuthController.class;
        assertTrue(resourceClass.isAnnotationPresent(RequestMapping.class));

        RequestMapping annotation = resourceClass.getDeclaredAnnotation(RequestMapping.class);
        assertTrue(Arrays.asList(annotation.value()).contains("/api/auth"));
        System.out.println(Arrays.toString(annotation.value()));
    }

    @Test
    void testMethodPostMappings(){
        Class<AuthController> resourceClass = AuthController.class;
        Set<Method> methods = Arrays.stream(resourceClass.getMethods()).filter(method -> method.isAnnotationPresent(PostMapping.class)).collect(Collectors.toSet());
        for(Method method: methods){
            String name = method.getName();
            String actualValue = Arrays.stream(method.getAnnotation(PostMapping.class).value()).findFirst().orElseThrow();
            assertEquals(postMethodMappings.get(name), actualValue);
        }
    }

    @Test
    void registerUser_shouldReturnBadRequestWhenUsernameExists() {
        when(mockUserRepo.existsByUsername(anyString())).thenReturn(true);

        SignupRequest request = new SignupRequest();
        request.setEmail("some@email.com");
        request.setPassword("password");
        request.setUsername("sampleUsername");
        ResponseEntity<?> actual = authController.registerUser(request);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, actual.getStatusCode().value());
    }

    @Test
    void registerUser_shouldReturnBadRequestWhenEmail() {
        when(mockUserRepo.existsByUsername(anyString())).thenReturn(false);
        when(mockUserRepo.existsByEmail(anyString())).thenReturn(true);

        SignupRequest request = new SignupRequest();
        request.setEmail("some@email.com");
        request.setPassword("password");
        request.setUsername("sampleUsername");
        ResponseEntity<?> actual = authController.registerUser(request);
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, actual.getStatusCode().value());
    }

    @Test
    void registerUser_shouldReturnOkOnSuccess() {
        when(mockUserRepo.existsByUsername(anyString())).thenReturn(false);
        when(mockUserRepo.existsByEmail(anyString())).thenReturn(false);
        when(mockEncoder.encode(anyString())).thenReturn("encodeString");

        Role userRole = new Role();
        when(mockRoleRepo.findByName(eq(ERole.ROLE_USER))).thenReturn(Optional.of(userRole));

        when(mockUserRepo.save(any(User.class))).thenReturn(mock(User.class));

        SignupRequest request = new SignupRequest();
        request.setEmail("some@email.com");
        request.setPassword("password");
        request.setUsername("sampleUsername");
        ResponseEntity<?> actual = authController.registerUser(request);

        assertEquals(HttpServletResponse.SC_OK, actual.getStatusCode().value());
        verify(mockUserRepo, times(1)).save(any(User.class));
    }
}