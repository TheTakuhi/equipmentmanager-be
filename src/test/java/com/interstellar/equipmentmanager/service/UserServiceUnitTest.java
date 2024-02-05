package com.interstellar.equipmentmanager.service;

import com.interstellar.equipmentmanager.model.dto.user.in.UserCreateDTO;
import com.interstellar.equipmentmanager.model.dto.user.in.UserEditDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserCroppedDTO;
import com.interstellar.equipmentmanager.model.dto.user.out.UserDTO;
import com.interstellar.equipmentmanager.model.entity.User;
import com.interstellar.equipmentmanager.model.enums.UserRole;
import com.interstellar.equipmentmanager.model.filter.UserFilter;
import com.interstellar.equipmentmanager.repository.UserRepository;
import com.interstellar.equipmentmanager.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


public class UserServiceUnitTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_Success() {
        UserCreateDTO userCreateDTO = new UserCreateDTO();

        User user = new User();
        UserDTO userDTO = new UserDTO();

        when(modelMapper.map(userCreateDTO, User.class)).thenReturn(user);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(UUID.randomUUID());
            return savedUser;
        });
        when(modelMapper.map(user, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.createUser(userCreateDTO);

        assertNotNull(result);
    }

    @Test
    void updateUser_SuccessWithoutKeycloakSync() {
        UUID userId = UUID.randomUUID();
        UserEditDTO userEditDTO = new UserEditDTO();
        userEditDTO.setId(userId);
        Boolean syncRolesToKeycloak = false;
        UserDTO userDTO = new UserDTO();

        User originalUser = new User();
        originalUser.setId(userId);

        when(modelMapper.map(userEditDTO, User.class)).thenReturn(originalUser);
        when(userRepository.save(eq(originalUser))).thenReturn(originalUser);
        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(originalUser));
        when(modelMapper.map(originalUser, UserDTO.class)).thenReturn(userDTO);


        UserDTO result = userService.updateUser(userId, userEditDTO, syncRolesToKeycloak);

        assertNotNull(result);
    }

    @Test
    void getUserById_Success() {
        UUID userId = UUID.randomUUID();
        User originalUser = new User();
        UserDTO expectedUserDTO = new UserDTO();

        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(originalUser));
        when(modelMapper.map(eq(originalUser), eq(UserDTO.class))).thenReturn(expectedUserDTO);

        UserDTO result = userService.getUserById(userId);

        assertNotNull(result);
    }

    @Test
    void getUserByLogin_Success() {
        String login = "testLogin";
        User expectedUser = new User();

        when(userRepository.findByLogin(eq(login))).thenReturn(Optional.of(expectedUser));

        User result = userService.getUserBylogin(login);

        assertNotNull(result);
    }

    @Test
    void getUserByLdapId_Success() {
        UUID ldapId = UUID.randomUUID();
        User expectedUser = new User();
        UserDTO expectedUserDTO = new UserDTO();

        when(userRepository.findByIdAndRemoved(eq(ldapId), eq(false))).thenReturn(Optional.of(expectedUser));
        when(modelMapper.map(eq(expectedUser), eq(UserDTO.class))).thenReturn(expectedUserDTO);

        UserDTO result = userService.getUserByLdapId(ldapId);

        assertNotNull(result);
    }

    @Test
    void getOriginalUser_Success() {
        UUID userId = UUID.randomUUID();
        User expectedUser = new User();

        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(expectedUser));

        User result = userService.getOriginalUser(userId);

        assertNotNull(result);
    }

    @Test
    void getAllUsers_Success() {
        UserFilter filter = new UserFilter();
        filter.setFullName("FLLL NMMA");
        filter.setLogin("fllmnasd");
        filter.setIncludeRemovedUser(false);
        filter.setUserRoles(List.of(UserRole.ADMIN));

        List<User> filteredUsers = Collections.singletonList(new User());
        Page<User> page = new PageImpl<>(filteredUsers);

        when(userRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);

        Page<UserCroppedDTO> result = userService.getAllUsers(filter, null);

        Assertions.assertEquals(filteredUsers.size(), result.getContent().size());
    }

    @Test
    void deleteUserById_Success() {
        UUID userId = UUID.randomUUID();
        User originalUser = new User();
        originalUser.setId(userId);

        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(originalUser));

        userService.deleteUserById(userId);

        assertDoesNotThrow(() -> userService.deleteUserById(userId));
    }
}
