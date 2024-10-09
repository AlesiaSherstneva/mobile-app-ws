package com.develop.app.ws.service.impl;

import com.develop.app.ws.io.entity.UserEntity;
import com.develop.app.ws.repository.UserRepository;
import com.develop.app.ws.shared.Utils;
import com.develop.app.ws.shared.dto.AddressDto;
import com.develop.app.ws.shared.dto.UserDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private Utils utils;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private AutoCloseable autoCloseable;

    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        autoCloseable = openMocks(this);

        userEntity = UserEntity.builder()
                .id(1L)
                .firstName("Firstname")
                .lastName("Lastname")
                .userId("someRandomUserId123")
                .encryptedPassword("someEncryptedPassword321")
                .build();
    }

    @Test
    void createUserTest() {
        when(userRepository.findUserEntityByEmail(anyString())).thenReturn(null);
        when(utils.generateEntitiesPublicId(anyInt())).thenReturn("someRandomId456");
        when(passwordEncoder.encode(anyString())).thenReturn("someEncryptedPassword321");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        AddressDto shippingAddressDto = new AddressDto();
        shippingAddressDto.setCity("Vancouver");
        shippingAddressDto.setCountry("Canada");
        shippingAddressDto.setPostalCode("ABC123");
        shippingAddressDto.setStreetName("123 Street name");
        shippingAddressDto.setType("shipping");

        AddressDto billingAddressDto = new AddressDto();
        billingAddressDto.setCity("Vancouver");
        billingAddressDto.setCountry("Canada");
        billingAddressDto.setPostalCode("ABC123");
        billingAddressDto.setStreetName("123 Street name");
        billingAddressDto.setType("billing");

        List<AddressDto> addresses = new ArrayList<>();
        addresses.add(shippingAddressDto);
        addresses.add(billingAddressDto);

        UserDto userDto = new UserDto();
        userDto.setFirstName("Alesia");
        userDto.setLastName("Sherstneva");
        userDto.setPassword("12345678");
        userDto.setEmail("test@test.com");
        userDto.setAddresses(addresses);

        UserDto createdUser = userService.createUser(userDto);

        assertNotNull(createdUser);
        assertEquals(userEntity.getFirstName(), createdUser.getFirstName());
    }

    @Test
    void loadUserByUsername() {
    }

    @Test
    void getUserTest() {
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