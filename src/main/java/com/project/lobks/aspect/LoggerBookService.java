package com.project.lobks.aspect;


import com.project.lobks.dto.BookDTO;
import com.project.lobks.entity.Book;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class LoggerBookService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut(value = "execution(public * com.project.lobks.service.BookServiceImpl.readBookById(..)) && args(id)")
    private void readBookByIdPointcut(Long id) {}

    @Pointcut(value = "execution(public * com.project.lobks.service.BookServiceImpl.readAllBooks())")
    private void readAllBooks() {}

    @Pointcut(value = "execution(public * com.project.lobks.service.BookServiceImpl.createBook(..)) && args(bookDTO)")
    private void createBookPointcut(BookDTO bookDTO) {}

    @Pointcut(value = "execution(public * com.project.lobks.service.BookServiceImpl.updateBook(..)) && args(book)")
    private void updateBookPointcut(Book book) {}

    @Pointcut(value = "execution(public * com.project.lobks.service.BookServiceImpl.deleteBook(..)) && args(id)")
    private void deleteBookPointcut(Long id) {}

    @Around("readBookByIdPointcut(id)")
    public Object aroundReadBookById(ProceedingJoinPoint joinPoint, Long id) throws Throwable {
        logger.info("trying to get a book by id=" + id);
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("book received: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e){
            e.printStackTrace();
        }

        return new Object();
    }

    @Around("readAllBooks()")
    public Object aroundReadAllBooks(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("trying to get all books");
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("book received: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e){
            e.printStackTrace();
        }
        return new Object();
    }

    @Around("createBookPointcut(bookDTO)")
    public Object aroundCreateBook(ProceedingJoinPoint joinPoint, BookDTO bookDTO) throws Throwable {
        logger.info("trying to contribute book in DB");
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("book introduced: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e){
            e.printStackTrace();
        }
        return new Object();

    }

    @Around("updateBookPointcut(book)")
    public Object aroundUpdateBook(ProceedingJoinPoint joinPoint, Book book) throws Throwable {
        logger.info("trying to update book");
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("book updated: " + targetMethodResult);
            return targetMethodResult;
        } catch (Exception e){
            e.printStackTrace();
        }
        return new Object();

    }

    @Around("deleteBookPointcut(id)")
    public Object aroundDeleteBook(ProceedingJoinPoint joinPoint, Long id) throws Throwable {
        logger.info("trying to delete book from db");
        try {
            Object targetMethodResult = joinPoint.proceed();
            logger.info("book deleted");
            return targetMethodResult;
        } catch (Exception e){
            e.printStackTrace();
        }
        return new Object();

    }


}
