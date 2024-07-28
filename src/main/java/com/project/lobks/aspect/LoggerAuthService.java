package com.project.lobks.aspect;

import com.project.lobks.dto.jwt.request.LoginRequest;
import com.project.lobks.dto.jwt.request.SignUpRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAuthService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * com.project.lobks.controller.AuthController.authenticateUser(..)) && args(loginRequest)")
    private void authenticateUser(LoginRequest loginRequest){}

    @Pointcut("execution(public * com.project.lobks.controller.AuthController.registerUser(..)) && args(signUpRequest)")
    private void registerUser(SignUpRequest signUpRequest) {}

    @Around("authenticateUser(loginRequest)")
    public Object aroundAuthenticateUser(ProceedingJoinPoint joinPoint, LoginRequest loginRequest) throws Throwable {
        logger.info("trying to authenticate user with username=" + loginRequest.getUsername());
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("user is authenticated: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Object();
    }

    @Around("registerUser(signUpRequest)")
    public Object aroundRegisterUser(ProceedingJoinPoint joinPoint, SignUpRequest signUpRequest) throws Throwable {
        logger.info("trying to register user with username=" + signUpRequest.getUsername());
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("user is registered: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Object();
    }
}
