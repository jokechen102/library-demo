package com.gk.study.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gk.study.entity.ThingCollect;
import com.gk.study.mapper.ThingCollectMapper;
import com.gk.study.service.impl.ThingCollectServiceImpl;
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

class ThingCollectServiceImplTest {

    @InjectMocks
    private ThingCollectServiceImpl thingCollectService;

    @Mock
    private ThingCollectMapper thingCollectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetThingCollectList() {
        // Given
        String userId = "123";
        List<Map> mockCollectList = Arrays.asList(mock(Map.class), mock(Map.class));
        when(thingCollectMapper.getThingCollectList(userId)).thenReturn(mockCollectList);

        // When
        List<Map> result = thingCollectService.getThingCollectList(userId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(thingCollectMapper, times(1)).getThingCollectList(userId);
    }

    @Test
    void testCreateThingCollect() {
        // Given
        ThingCollect thingCollect = new ThingCollect();
        thingCollect.setThingId("1");
        thingCollect.setUserId("1");

        // When
        thingCollectService.createThingCollect(thingCollect);

        // Then
        verify(thingCollectMapper, times(1)).insert(thingCollect);
    }

    @Test
    void testDeleteThingCollect() {
        // Given
        String collectId = "123";

        // When
        thingCollectService.deleteThingCollect(collectId);

        // Then
        verify(thingCollectMapper, times(1)).deleteById(collectId);
    }

    @Test
    void testGetThingCollect() {
        // Given
        String userId = "1";
        String thingId = "10";
        ThingCollect mockCollect = new ThingCollect();
        mockCollect.setUserId("1");
        mockCollect.setThingId("10");

        when(thingCollectMapper.selectOne(any(QueryWrapper.class))).thenReturn(mockCollect);

        // When
        ThingCollect result = thingCollectService.getThingCollect(userId, thingId);

        // Then
        assertNotNull(result);
        assertEquals("1", result.getUserId());
        assertEquals("10", result.getThingId());
        verify(thingCollectMapper, times(1)).selectOne(any(QueryWrapper.class));
    }
}
