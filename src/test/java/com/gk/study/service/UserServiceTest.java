package com.gk.study.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gk.study.entity.User;
import com.gk.study.mapper.UserMapper;
import com.gk.study.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserList_withKeyword() {
        // Given
        User user1 = new User();
        user1.setUsername("testUser1");

        User user2 = new User();
        user2.setUsername("testUser2");

        List<User> mockUsers = Arrays.asList(user1, user2);

        when(userMapper.selectList(any(QueryWrapper.class))).thenReturn(mockUsers);

        // When
        List<User> result = userService.getUserList("test");

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("testUser1", result.get(0).getUsername());

        verify(userMapper, times(1)).selectList(any(QueryWrapper.class));
    }

    @Test
    void testGetAdminUser() {
        // Given
        User user = new User();
        user.setUsername("admin");
        user.setPassword("password");

        User mockAdmin = new User();
        mockAdmin.setUsername("admin");
        mockAdmin.setRole("2"); // Role > 1

        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(mockAdmin);

        // When
        User result = userService.getAdminUser(user);

        // Then
        assertNotNull(result);
        assertEquals("admin", result.getUsername());
        assertEquals("2", result.getRole());

        verify(userMapper, times(1)).selectOne(any(QueryWrapper.class));
    }

    @Test
    void testGetNormalUser() {
        // Given
        User user = new User();
        user.setUsername("normal");
        user.setPassword("password");

        User mockNormalUser = new User();
        mockNormalUser.setUsername("normal");
        mockNormalUser.setRole("1");

        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(mockNormalUser);

        // When
        User result = userService.getNormalUser(user);

        // Then
        assertNotNull(result);
        assertEquals("normal", result.getUsername());
        assertEquals("1", result.getRole());

        verify(userMapper, times(1)).selectOne(any(QueryWrapper.class));
    }

    @Test
    void testCreateUser() {
        // Given
        User user = new User();
        user.setUsername("newUser");

        // When
        userService.createUser(user);

        // Then
        verify(userMapper, times(1)).insert(user);
    }

    @Test
    void testDeleteUser() {
        // Given
        String userId = "123";

        // When
        userService.deleteUser(userId);

        // Then
        verify(userMapper, times(1)).deleteById(userId);
    }

    @Test
    void testUpdateUser() {
        // Given
        User user = new User();
        user.setUsername("updateUser");

        // When
        userService.updateUser(user);

        // Then
        verify(userMapper, times(1)).updateById(user);
    }

    @Test
    void testGetUserByToken() {
        // Given
        String token = "abc123";

        User mockUser = new User();
        mockUser.setToken(token);

        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(mockUser);

        // When
        User result = userService.getUserByToken(token);

        // Then
        assertNotNull(result);
        assertEquals("abc123", result.getToken());

        verify(userMapper, times(1)).selectOne(any(QueryWrapper.class));
    }

    @Test
    void testGetUserByUserName() {
        // Given
        String username = "testUser";

        User mockUser = new User();
        mockUser.setUsername(username);

        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(mockUser);

        // When
        User result = userService.getUserByUserName(username);

        // Then
        assertNotNull(result);
        assertEquals("testUser", result.getUsername());

        verify(userMapper, times(1)).selectOne(any(QueryWrapper.class));
    }

    @Test
    void testGetUserDetail() {
        // Given
        String userId = "123";

        User mockUser = new User();
        mockUser.setId("123");

        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(mockUser);

        // When
        User result = userService.getUserDetail(userId);

        // Then
        assertNotNull(result);
        assertEquals("123", result.getId());

        verify(userMapper, times(1)).selectOne(any(QueryWrapper.class));
    }
}
