package com.gk.study.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gk.study.entity.ThingWish;
import com.gk.study.mapper.ThingWishMapper;
import com.gk.study.service.impl.ThingWishServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ThingWishServiceImplTest {

    @InjectMocks
    private ThingWishServiceImpl thingWishService;

    @Mock
    private ThingWishMapper thingWishMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetThingWishList() {
        // Given
        String userId = "123";
        List<Map> mockWishList = Arrays.asList(mock(Map.class), mock(Map.class));
        when(thingWishMapper.getThingWishList(userId)).thenReturn(mockWishList);

        // When
        List<Map> result = thingWishService.getThingWishList(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(thingWishMapper, times(1)).getThingWishList(userId);
    }

    @Test
    void testCreateThingWish() {
        // Given
        ThingWish thingWish = new ThingWish();
        thingWish.setThingId("1");
        thingWish.setUserId("1");

        // When
        thingWishService.createThingWish(thingWish);

        // Then
        verify(thingWishMapper, times(1)).insert(thingWish);
    }

    @Test
    void testDeleteThingWish() {
        // Given
        String wishId = "123";

        // When
        thingWishService.deleteThingWish(wishId);

        // Then
        verify(thingWishMapper, times(1)).deleteById(wishId);
    }

    @Test
    void testGetThingWish() {
        // Given
        String userId = "1";
        String thingId = "10";
        ThingWish mockWish = new ThingWish();
        mockWish.setUserId("1");
        mockWish.setThingId("10");

        when(thingWishMapper.selectOne(any(QueryWrapper.class))).thenReturn(mockWish);

        // When
        ThingWish result = thingWishService.getThingWish(userId, thingId);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getUserId());
        assertEquals("10", result.getThingId());
        verify(thingWishMapper, times(1)).selectOne(any(QueryWrapper.class));
    }
}
