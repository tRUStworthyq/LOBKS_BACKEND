package com.project.lobks.aspect;

import com.project.lobks.entity.enums.StatusUser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerUserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * com.project.lobks.service.UserServiceImpl.findAllUsers())")
    private void findAllUsers(){}

    @Pointcut("execution(public * com.project.lobks.service.UserServiceImpl.findUserByUsername(..)) && args(username)")
    private void findUserByUsername(String username){}

    @Pointcut("execution(public * com.project.lobks.service.UserServiceImpl.findUsersByStatusUser(..)) && args(statusUser)")
    private void findUsersByStatusUser(StatusUser statusUser){}

    @Pointcut("execution(public * com.project.lobks.service.UserServiceImpl.updateStatusUserByUsername(..)) && args(username)")
    private void updateStatusUserByUsername(String username){}

    @Pointcut("execution(public * com.project.lobks.service.UserServiceImpl.findUserById(..)) && args(id)")
    private void findUserById(Long id){}


    @Around("findAllUsers()")
    public Object aroundFindAllUsers(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("trying to get all users");
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("users received: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e){
            e.printStackTrace();
        }

        return new Object();
    }

    @Around("findUserByUsername(username)")
    public Object aroundFindUserByUsername(ProceedingJoinPoint joinPoint, String username) throws Throwable {
        logger.info("trying to get user with username=" + username);
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("user received: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Object();
    }

    @Around("findUsersByStatusUser(statusUser)")
    public Object aroundFindUsersByStatusUser(ProceedingJoinPoint joinPoint, StatusUser statusUser) throws Throwable {
        logger.info("trying to get users with statusUser=" + statusUser);
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("users received: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Object();
    }

    @Around("updateStatusUserByUsername(username)")
    public Object aroundUpdateStatusUserByUsername(ProceedingJoinPoint joinPoint, String username) throws Throwable {
        logger.info("trying to update statusUser for user with username=" + username);
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("statusUser updated: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Object();
    }

    @Around("findUserById(id)")
    public Object aroundFindUserById(ProceedingJoinPoint joinPoint, Long id) throws Throwable {
        logger.info("trying to get user with id=" + id);
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("user received: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Object();
    }


}
