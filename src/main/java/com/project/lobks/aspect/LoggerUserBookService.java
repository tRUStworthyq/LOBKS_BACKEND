package com.project.lobks.aspect;

import com.project.lobks.dto.UserBookDTO;
import com.project.lobks.entity.UserBookEmbeddable;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerUserBookService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * com.project.lobks.service.UserBookServiceImpl.findBooksByUserId(..)) && args(id)")
    private void findBooksByUserId(Long id){}
    @Pointcut("execution(public * com.project.lobks.service.UserBookServiceImpl.findUsersByBookId(..)) && args(id)")
    private void findUsersByBookId(Long id) {}
    @Pointcut("execution(public * com.project.lobks.service.UserBookServiceImpl.saveUserBook(..)) && args(userBookDTO, jwt)")
    private void saveUserBook(UserBookDTO userBookDTO, String jwt){}
    @Pointcut("execution(public * com.project.lobks.service.UserBookServiceImpl.deleteUserBook(..)) && args(userBookEmbeddable, jwt)")
    private void deleteUserBook(UserBookEmbeddable userBookEmbeddable, String jwt){}


    @Around("findBooksByUserId(id)")
    public Object aroundFindBooksByUserId(ProceedingJoinPoint joinPoint, Long id) throws Throwable {
        logger.info("trying to get books by userId=" + id);
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("books received: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e){
            e.printStackTrace();
        }
        return new Object();
    }

    @Around("findUsersByBookId(id)")
    public Object aroundFindUsersByBookId(ProceedingJoinPoint joinPoint, Long id) throws Throwable {
        logger.info("trying to get users by bookId=" + id);
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("users received: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e){
            e.printStackTrace();
        }
        return new Object();
    }

    @Around("saveUserBook(userBookDTO, jwt)")
    public Object aroundSaveUserBook(ProceedingJoinPoint joinPoint, UserBookDTO userBookDTO, String jwt) throws Throwable {
        logger.info("trying to add book with id=" + userBookDTO.getBook_id() + " to user with id=" + userBookDTO.getUser_id() + " list");
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("book added: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e){
            e.printStackTrace();
        }
        return new Object();
    }

    @Around("deleteUserBook(userBookEmbeddable, jwt)")
    public Object aroundDeleteUserBook(ProceedingJoinPoint joinPoint, UserBookEmbeddable userBookEmbeddable, String jwt) throws Throwable {
        logger.info("trying to delete book with id=" + userBookEmbeddable.getBookId() + " to user with id=" + userBookEmbeddable.getUserId() + " list");
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("book deleted: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e){
            e.printStackTrace();
        }
        return new Object();
    }


}
