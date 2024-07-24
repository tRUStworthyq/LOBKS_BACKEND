package com.project.lobks.aspect;

import com.project.lobks.dto.AuthorDTO;
import com.project.lobks.entity.Author;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerAuthorService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut(value = "execution(public * com.project.lobks.service.AuthorServiceImpl.readAllAuthors())")
    private void readAllAuthorsPointcut() {}

    @Pointcut(value = "execution(public * com.project.lobks.service.AuthorServiceImpl.readAuthorById(..)) && args(id)")
    private void readAuthorByIdPointcut(Long id) {}

    @Pointcut(value = "execution(public * com.project.lobks.service.AuthorServiceImpl.findAuthorsByFirstnameLikeAndLastnameLike(..)) && args(authorDTO)")
    private void findAuthorsByFirstnameLikeAndLastnameLike(AuthorDTO authorDTO) {}

    @Pointcut(value = "execution(public * com.project.lobks.service.AuthorServiceImpl.createAuthor(..)) && args(authorDTO)")
    private void createAuthorPointcut(AuthorDTO authorDTO) {}

    @Pointcut(value = "execution(public * com.project.lobks.service.AuthorServiceImpl.updateAuthor(..)) && args(author)")
    private void updateAuthorPointcut(Author author) {}

    @Pointcut(value = "execution(public * com.project.lobks.service.AuthorServiceImpl.deleteAuthor(..)) && args(id)")
    private void deleteAuthorPointcut(Long id) {}

    @Around("readAllAuthorsPointcut()")
    public Object aroundReadAllAuthors(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("trying to get all authors");
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("authors received: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e){
            e.printStackTrace();
        }

        return new Object();
    }

    @Around("readAuthorByIdPointcut(id)")
    public Object aroundReadAuthorById(ProceedingJoinPoint joinPoint, Long id) throws Throwable {
        logger.info("trying to get a author by id=" + id);
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("author received: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e){
            e.printStackTrace();
        }

        return new Object();
    }

    @Around("findAuthorsByFirstnameLikeAndLastnameLike(authorDTO)")
    public Object aroundFindAuthorsByFirstnameLikeAndLastnameLike(ProceedingJoinPoint joinPoint, AuthorDTO authorDTO) throws Throwable {
        logger.info("trying to get a author by firstname=" + authorDTO.getFirstname() + " and lastname=" + authorDTO.getLastname());
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("authors received: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e){
            e.printStackTrace();
        }

        return new Object();
    }

    @Around("createAuthorPointcut(authorDTO)")
    public Object aroundCreateAuthor(ProceedingJoinPoint joinPoint, AuthorDTO authorDTO) throws Throwable {
        logger.info("trying to contribute author in DB");
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("author introduced: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e){
            e.printStackTrace();
        }
        return new Object();
    }

    @Around("updateAuthorPointcut(author)")
    public Object aroundUpdateAuthor(ProceedingJoinPoint joinPoint, Author author) throws Throwable {
        logger.info("trying to update author");
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("author updated: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e){
            e.printStackTrace();
        }

        return new Object();
    }
    @Around("deleteAuthorPointcut(id)")
    public Object aroundDeleteAuthor(ProceedingJoinPoint joinPoint, Long id) throws Throwable {
        logger.info("trying to delete author from db");
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("author deleted");
            return targetMethodResult;
        } catch (Exception e){
            e.printStackTrace();
        }

        return new Object();
    }
}

