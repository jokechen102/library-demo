package com.gk.study.service;

import com.gk.study.entity.Borrow;
import com.gk.study.mapper.BorrowMapper;
import com.gk.study.service.impl.BorrowServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowServiceImplTest {

    @InjectMocks
    private BorrowServiceImpl borrowService;

    @Mock
    private BorrowMapper borrowMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBorrowList() {
        // Given
        Borrow borrow1 = new Borrow();
        borrow1.setStatus("1");

        Borrow borrow2 = new Borrow();
        borrow2.setStatus("2");

        List<Borrow> mockBorrowList = Arrays.asList(borrow1, borrow2);
        when(borrowMapper.getList()).thenReturn(mockBorrowList);

        // When
        List<Borrow> result = borrowService.getBorrowList();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).getStatus());
        verify(borrowMapper, times(1)).getList();
    }

    @Test
    void testCreateBorrow() {
        // Given
        Borrow borrow = new Borrow();

        // When
        borrowService.createBorrow(borrow);

        // Then
        assertNotNull(borrow.getBorrowTime());
        assertEquals("1", borrow.getStatus());
        verify(borrowMapper, times(1)).insert(borrow);
    }

    @Test
    void testDeleteBorrow() {
        // Given
        String borrowId = "123";

        // When
        borrowService.deleteBorrow(borrowId);

        // Then
        verify(borrowMapper, times(1)).deleteById(borrowId);
    }

    @Test
    void testUpdateBorrow() {
        // Given
        Borrow borrow = new Borrow();
        borrow.setId(123L);

        // When
        borrowService.updateBorrow(borrow);

        // Then
        verify(borrowMapper, times(1)).updateById(borrow);
    }

    @Test
    void testGetUserBorrowList() {
        // Given
        String userId = "1";
        String status = "active";

        Borrow borrow1 = new Borrow();
        borrow1.setStatus("active");

        List<Borrow> mockUserBorrowList = Arrays.asList(borrow1);
        when(borrowMapper.getUserBorrowList(userId, status)).thenReturn(mockUserBorrowList);

        // When
        List<Borrow> result = borrowService.getUserBorrowList(userId, status);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("active", result.get(0).getStatus());
        verify(borrowMapper, times(1)).getUserBorrowList(userId, status);
    }

    @Test
    void testDetail() {
        // Given
        Long borrowId = 123L;
        Borrow mockBorrow = new Borrow();
        mockBorrow.setId(borrowId);

        when(borrowMapper.selectById(borrowId)).thenReturn(mockBorrow);

        // When
        Borrow result = borrowService.detail(borrowId);

        // Then
        assertNotNull(result);
        assertEquals(borrowId, result.getId());
        verify(borrowMapper, times(1)).selectById(borrowId);
    }
}
