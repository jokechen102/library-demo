package com.gk.study.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IpUtilsTest {

    @Mock
    private HttpServletRequest mockRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetIpAddr_xForwardedFor() {
        // Given
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn("192.168.0.1");

        // When
        String ip = IpUtils.getIpAddr(mockRequest);

        // Then
        assertEquals("192.168.0.1", ip);
    }

    @Test
    void testGetIpAddr_proxyClientIp() {
        // Given
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(null);
        when(mockRequest.getHeader("Proxy-Client-IP")).thenReturn("192.168.0.2");

        // When
        String ip = IpUtils.getIpAddr(mockRequest);

        // Then
        assertEquals("192.168.0.2", ip);
    }

    @Test
    void testGetIpAddr_wlProxyClientIp() {
        // Given
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(null);
        when(mockRequest.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(mockRequest.getHeader("WL-Proxy-Client-IP")).thenReturn("192.168.0.3");

        // When
        String ip = IpUtils.getIpAddr(mockRequest);

        // Then
        assertEquals("192.168.0.3", ip);
    }

    @Test
    void testGetIpAddr_httpClientIp() {
        // Given
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(null);
        when(mockRequest.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(mockRequest.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
        when(mockRequest.getHeader("HTTP_CLIENT_IP")).thenReturn("192.168.0.4");

        // When
        String ip = IpUtils.getIpAddr(mockRequest);

        // Then
        assertEquals("192.168.0.4", ip);
    }

    @Test
    void testGetIpAddr_httpXForwardedFor() {
        // Given
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(null);
        when(mockRequest.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(mockRequest.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
        when(mockRequest.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
        when(mockRequest.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn("192.168.0.5");

        // When
        String ip = IpUtils.getIpAddr(mockRequest);

        // Then
        assertEquals("192.168.0.5", ip);
    }

    @Test
    void testGetIpAddr_remoteAddr() {
        // Given
        when(mockRequest.getHeader("x-forwarded-for")).thenReturn(null);
        when(mockRequest.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(mockRequest.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
        when(mockRequest.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
        when(mockRequest.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn(null);
        when(mockRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        // When
        String ip = IpUtils.getIpAddr(mockRequest);

        // Then
        assertEquals("127.0.0.1", ip);
    }

    @Test
    void testGetIpAddr_errorHandling() {
        // Given
        when(mockRequest.getHeader(anyString())).thenThrow(new RuntimeException("Header Error"));

        // When
        String ip = IpUtils.getIpAddr(mockRequest);

        // Then
        assertNull(ip);
    }
}
