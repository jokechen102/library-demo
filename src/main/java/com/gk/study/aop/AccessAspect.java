package com.gk.study.aop;

import com.google.gson.Gson;
import com.gk.study.common.APIResponse;
import com.gk.study.common.ResponeCode;
import com.gk.study.entity.OpLog;
import com.gk.study.entity.User;
import com.gk.study.permission.Access;
import com.gk.study.permission.AccessLevel;
import com.gk.study.service.OpLogService;
import com.gk.study.service.UserService;
import com.gk.study.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

//@Aspect
//@Component
@Slf4j
public class AccessAspect {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private OpLogService service;

    @Autowired
    private UserService userService;

    // 环绕切面，用于处理验权
    @Around("@annotation(com.gk.study.permission.Access)")
    public Object aroundAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Access access = method.getAnnotation(Access.class);
        if (access == null) {
            // 如果没有注解，放行
            return joinPoint.proceed();
        }

        // 检查权限
        if (access.level().getCode() == AccessLevel.ADMIN.getCode()) {
            return handleAdminAccess(joinPoint);
        } else if (access.level().getCode() == AccessLevel.LOGIN.getCode()) {
            return handleUserAccess(joinPoint);
        }

        return joinPoint.proceed(); // 放行
    }

    private Object handleAdminAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        String token = request.getHeader("ADMINTOKEN");
        log.info("管理员 token==>" + token);
        User user = userService.getUserByToken(token);
        if (user != null && user.getRole().equals(String.valueOf(User.AdminUser))) {
            return joinPoint.proceed();
        } else {
            APIResponse apiResponse = new APIResponse(ResponeCode.FAIL, "无操作权限");
            writeResponse(apiResponse);
            return null;
        }
    }

    private Object handleUserAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        String token = request.getHeader("TOKEN");
        log.info("用户 token==>" + token);
        User user = userService.getUserByToken(token);
        if (user != null && user.getRole().equals(String.valueOf(User.NormalUser))) {
            return joinPoint.proceed();
        } else {
            APIResponse apiResponse = new APIResponse(ResponeCode.FAIL, "未登录");
            writeResponse(apiResponse);
            return null;
        }
    }

    // 日志记录
    @After("@annotation(com.gk.study.permission.Access)")
    public void logAfter() {
        Long endTime = System.currentTimeMillis();
        Long startTime = (Long) request.getAttribute("_startTime");
        if (startTime == null) {
            startTime = System.currentTimeMillis();
        }
        Long diff = endTime - startTime;

        OpLog opLog = new OpLog();
        opLog.setReIp(IpUtils.getIpAddr(request));
        opLog.setReMethod(request.getMethod());
        opLog.setReUrl(request.getRequestURI());
        opLog.setReUa(request.getHeader(HttpHeaders.USER_AGENT));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        opLog.setReTime(formatter.format(new Date()));
        opLog.setAccessTime(String.valueOf(diff));
        service.createOpLog(opLog);
    }

    // 响应写入
    public void writeResponse(APIResponse apiResponse) throws IOException {
        response.setStatus(200);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        Gson gson = new Gson();
        String jsonStr = gson.toJson(apiResponse);
        response.getWriter().println(jsonStr);
        response.getWriter().flush();
    }
}
