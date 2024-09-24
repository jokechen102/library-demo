package com.gk.study.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gk.study.entity.Thing;
import com.gk.study.entity.ThingTag;
import com.gk.study.mapper.ThingMapper;
import com.gk.study.mapper.ThingTagMapper;
import com.gk.study.service.impl.ThingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ThingServiceImplTest {

    @InjectMocks
    private ThingServiceImpl thingService;

    @Mock
    private ThingMapper thingMapper;

    @Mock
    private ThingTagMapper thingTagMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetThingList_withKeywordAndSort() {
        // Given
        Thing thing1 = new Thing();
        thing1.setTitle("Test Thing 1");

        Thing thing2 = new Thing();
        thing2.setTitle("Test Thing 2");

        List<Thing> mockThings = Arrays.asList(thing1, thing2);
        when(thingMapper.selectList(any(QueryWrapper.class))).thenReturn(mockThings);

        // When
        List<Thing> result = thingService.getThingList("Test", "recent", null, null);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Thing 1", result.get(0).getTitle());
        verify(thingMapper, times(1)).selectList(any(QueryWrapper.class));
    }

    @Test
    void testCreateThing() {
        // Given
        Thing thing = new Thing();
        thing.setTitle("New Thing");
        List<Long> tags = new ArrayList<>();
        tags.add(1L);
        tags.add(2L);
        thing.setTags(tags);

        // When
        thingService.createThing(thing);

        // Then
        verify(thingMapper, times(1)).insert(thing);
        verify(thingTagMapper, times(2)).insert(any(ThingTag.class)); // 2 tags should be inserted
    }

    @Test
    void testDeleteThing() {
        // Given
        String thingId = "123";

        // When
        thingService.deleteThing(thingId);

        // Then
        verify(thingMapper, times(1)).deleteById(thingId);
    }

    @Test
    void testUpdateThing() {
        // Given
        Thing thing = new Thing();
        thing.setId(123L);
        List<Long> tags = new ArrayList<>();
        tags.add(1L);
        tags.add(2L);
        thing.setTags(tags);

        // When
        thingService.updateThing(thing);

        // Then
        verify(thingMapper, times(1)).updateById(thing);
        verify(thingTagMapper, times(2)).insert(any(ThingTag.class)); // 2 tags should be inserted
    }

    @Test
    void testGetThingById() {
        // Given
        String thingId = "123";

        Thing mockThing = new Thing();
        mockThing.setId(123L);

        when(thingMapper.selectById(thingId)).thenReturn(mockThing);

        // When
        Thing result = thingService.getThingById(thingId);

        // Then
        assertNotNull(result);
        assertEquals(123L, result.getId());
        verify(thingMapper, times(1)).selectById(thingId);
    }

    @Test
    void testAddWishCount() {
        // Given
        String thingId = "123";
        Thing mockThing = new Thing();
        mockThing.setWishCount("5");

        when(thingMapper.selectById(thingId)).thenReturn(mockThing);

        // When
        thingService.addWishCount(thingId);

        // Then
        assertEquals("6", mockThing.getWishCount());
        verify(thingMapper, times(1)).updateById(mockThing);
    }

    @Test
    void testAddCollectCount() {
        // Given
        String thingId = "123";
        Thing mockThing = new Thing();
        mockThing.setCollectCount("5");

        when(thingMapper.selectById(thingId)).thenReturn(mockThing);

        // When
        thingService.addCollectCount(thingId);

        // Then
        assertEquals("6", mockThing.getCollectCount());
        verify(thingMapper, times(1)).updateById(mockThing);
    }
}
