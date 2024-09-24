package com.gk.study.service;

import com.gk.study.entity.Comment;
import com.gk.study.mapper.CommentMapper;
import com.gk.study.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private CommentMapper commentMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCommentList() {
        // Given
        Comment comment1 = new Comment();
        comment1.setContent("Comment 1");

        Comment comment2 = new Comment();
        comment2.setContent("Comment 2");

        List<Comment> mockComments = Arrays.asList(comment1, comment2);
        when(commentMapper.getList()).thenReturn(mockComments);

        // When
        List<Comment> result = commentService.getCommentList();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Comment 1", result.get(0).getContent());
        verify(commentMapper, times(1)).getList();
    }

    @Test
    void testCreateComment() {
        // Given
        Comment comment = new Comment();
        comment.setContent("New Comment");

        // When
        commentService.createComment(comment);

        // Then
        assertNotNull(comment.getCommentTime());
        verify(commentMapper, times(1)).insert(comment);
    }

    @Test
    void testDeleteComment() {
        // Given
        String commentId = "123";

        // When
        commentService.deleteComment(commentId);

        // Then
        verify(commentMapper, times(1)).deleteById(commentId);
    }

    @Test
    void testUpdateComment() {
        // Given
        Comment comment = new Comment();
        comment.setId(123L);
        comment.setContent("Updated Comment");

        // When
        commentService.updateComment(comment);

        // Then
        verify(commentMapper, times(1)).updateById(comment);
    }

    @Test
    void testGetCommentDetail() {
        // Given
        String commentId = "123";
        Comment mockComment = new Comment();
        mockComment.setId(123L);

        when(commentMapper.selectById(commentId)).thenReturn(mockComment);

        // When
        Comment result = commentService.getCommentDetail(commentId);

        // Then
        assertNotNull(result);
        assertEquals(123L, result.getId());
        verify(commentMapper, times(1)).selectById(commentId);
    }

    @Test
    void testGetThingCommentList() {
        // Given
        String thingId = "1";
        String order = "recent";

        Comment comment1 = new Comment();
        comment1.setContent("Thing Comment 1");

        List<Comment> mockThingComments = Arrays.asList(comment1);
        when(commentMapper.selectThingCommentList(thingId, order)).thenReturn(mockThingComments);

        // When
        List<Comment> result = commentService.getThingCommentList(thingId, order);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Thing Comment 1", result.get(0).getContent());
        verify(commentMapper, times(1)).selectThingCommentList(thingId, order);
    }

    @Test
    void testGetUserCommentList() {
        // Given
        String userId = "1";

        Comment comment1 = new Comment();
        comment1.setContent("User Comment 1");

        List<Comment> mockUserComments = Arrays.asList(comment1);
        when(commentMapper.selectUserCommentList(userId)).thenReturn(mockUserComments);

        // When
        List<Comment> result = commentService.getUserCommentList(userId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("User Comment 1", result.get(0).getContent());
        verify(commentMapper, times(1)).selectUserCommentList(userId);
    }
}
