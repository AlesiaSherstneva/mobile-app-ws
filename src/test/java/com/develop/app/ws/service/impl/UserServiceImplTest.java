package com.develop.app.ws.service.impl;

import com.develop.app.ws.exception.UserServiceException;
import com.develop.app.ws.io.entity.AddressEntity;
import com.develop.app.ws.io.entity.UserEntity;
import com.develop.app.ws.repository.UserRepository;
import com.develop.app.ws.shared.Utils;
import com.develop.app.ws.shared.dto.AddressDto;
import com.develop.app.ws.shared.dto.UserDto;
import com.develop.app.ws.ui.model.response.AddressResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
                .email("test@test.com")
                .userId("someRandomUserId123")
                .encryptedPassword("someEncryptedPassword321")
                .addresses(getAddressesEntity())
                .build();
    }

    @Test
    void createUserTest() {
        when(userRepository.findUserEntityByEmail(anyString())).thenReturn(null);
        when(utils.generateEntitiesPublicId(anyInt())).thenReturn("someRandomId456");
        when(passwordEncoder.encode(anyString())).thenReturn("someEncryptedPassword321");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserDto userDto = new UserDto();
        userDto.setFirstName("Alesia");
        userDto.setLastName("Sherstneva");
        userDto.setPassword("12345678");
        userDto.setEmail("test@test.com");
        userDto.setAddresses(getAddressesDto());

        UserDto createdUser = userService.createUser(userDto);

        assertNotNull(createdUser);
        assertEquals(userEntity.getFirstName(), createdUser.getFirstName());
        assertEquals(userEntity.getLastName(), createdUser.getLastName());
        assertNotNull(userEntity.getUserId());

        assertEquals(userEntity.getAddresses().size(), userDto.getAddresses().size());

        verify(utils, times(3)).generateEntitiesPublicId(anyInt());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    private List<AddressDto> getAddressesDto() {
        AddressDto shippingAddressDto = AddressDto.builder()
                .city("Vancouver")
                .country("Canada")
                .postalCode("ABC123")
                .streetName("123 Street name")
                .type("shipping")
                .build();

        AddressDto billingAddressDto = AddressDto.builder()
                .city("Vancouver")
                .country("Canada")
                .postalCode("ABC123")
                .streetName("123 Street name")
                .type("billing")
                .build();

        List<AddressDto> addresses = new ArrayList<>();
        addresses.add(shippingAddressDto);
        addresses.add(billingAddressDto);

        return addresses;
    }

    private List<AddressEntity> getAddressesEntity() {
        List<AddressDto> addresses = getAddressesDto();

        Type listType = new TypeToken<List<AddressResponseModel>>() {
        }.getType();
        return new ModelMapper().map(addresses, listType);
    }

    @Test
    void createUserWhenEmailAlreadyExistsInDbTest() {
        when(userRepository.findUserEntityByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = new UserDto();
        userDto.setFirstName("Alesia");
        userDto.setLastName("Sherstneva");
        userDto.setPassword("12345678");
        userDto.setEmail("test@test.com");
        userDto.setAddresses(getAddressesDto());

        assertThrows(UserServiceException.class,
                () -> userService.createUser(userDto));
    }

    @Test
    void loadUserByUsernameWhenUserDoesNotExistInDb() {
        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("test@test.com"));
    }

    @Test
    void loadUserByUsername() {
        when(userRepository.findUserEntityByEmail(anyString())).thenReturn(userEntity);

        UserDetails user = userService.loadUserByUsername("test@test.com");

        assertEquals(userEntity.getEmail(), user.getUsername());
        assertEquals(userEntity.getEncryptedPassword(), user.getPassword());
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
        when(userRepository.findByUserId(anyString())).thenReturn(userEntity);

        UserDto userDto = userService.getUserByUserId("someRandomUserId123");

        assertNotNull(userDto);
        assertEquals(userDto.getFirstName(), userEntity.getFirstName());
        assertEquals(userDto.getLastName(), userEntity.getLastName());
        assertEquals(userDto.getEmail(), userEntity.getEmail());
        assertEquals(userDto.getUserId(), userEntity.getUserId());
        assertEquals(userDto.getEncryptedPassword(), userEntity.getEncryptedPassword());
    }

    @Test
    void getUserByUserIdWhenUserNotFoundInDbTest() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                () -> userService.getUser("test@test.com")
        );
    }

    @Test
    void getUsers() {
        Page<UserEntity> pageResult = new PageImpl<>(List.of(userEntity));

        when(userRepository.findAll(any(PageRequest.class))).thenReturn(pageResult);

        List<UserDto> userDtos = userService.getUsers(1, 1);

        assertEquals(1, userDtos.size());

        UserDto userDto = userDtos.get(0);

        assertNotNull(userDto);
        assertEquals(userDto.getFirstName(), userEntity.getFirstName());
        assertEquals(userDto.getLastName(), userEntity.getLastName());
        assertEquals(userDto.getEmail(), userEntity.getEmail());
        assertEquals(userDto.getUserId(), userEntity.getUserId());
        assertEquals(userDto.getEncryptedPassword(), userEntity.getEncryptedPassword());
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