package com.natiuk.userservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.natiuk.userservice.dto.RegisterRequest;
import com.natiuk.userservice.dto.UserResponse;
import com.natiuk.userservice.model.User;
import com.natiuk.userservice.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUserBackfillsKeycloakIdForExistingEmail() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("user@example.com");
        request.setKeycloakId("keycloak-subject");

        User existingUser = new User();
        existingUser.setId("database-id");
        existingUser.setEmail(request.getEmail());

        when(repository.existsByEmail(request.getEmail())).thenReturn(true);
        when(repository.findByEmail(request.getEmail())).thenReturn(existingUser);
        when(repository.save(existingUser)).thenReturn(existingUser);

        UserResponse response = userService.registerUser(request);

        assertEquals("keycloak-subject", existingUser.getKeycloakId());
        assertEquals("keycloak-subject", response.getKeycloakId());
        verify(repository).save(existingUser);
    }
}
