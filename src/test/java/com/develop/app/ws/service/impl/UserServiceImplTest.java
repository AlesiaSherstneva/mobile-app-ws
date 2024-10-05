package com.develop.app.ws.service.impl;

import com.develop.app.ws.io.entity.UserEntity;
import com.develop.app.ws.repository.UserRepository;
import com.develop.app.ws.shared.dto.UserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = openMocks(this);
    }

    @Test
    void createUser() {
    }

    @Test
    void loadUserByUsername() {
    }

    @Test
    void getUserTest() {
        UserEntity userEntity = UserEntity.builder()
                .id(1L)
                .firstName("Firstname")
                .lastName("Lastname")
                .userId("someRandomUserId123")
                .encryptedPassword("someEncryptedPassword321")
                .build();

        when(userRepository.findUserEntityByEmail(anyString())).thenReturn(userEntity);

        UserDto userDtoResponse = userService.getUser("test@test.com");

        assertNotNull(userDtoResponse);
        assertEquals(userEntity.getFirstName(), userDtoResponse.getFirstName());
    }

    @Test
    void getUserWhenUsernameNotFoundInDbTest() {
        when(userRepository.findUserEntityByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> userService.getUser("test@test.com")
        );
    }

    @Test
    void getUserByUserId() {
    }

    @Test
    void getUsers() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }
}