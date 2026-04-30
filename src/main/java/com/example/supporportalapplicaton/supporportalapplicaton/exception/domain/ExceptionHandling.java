package com.example.supporportalapplicaton.supporportalapplicaton.exception.domain;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.supporportalapplicaton.supporportalapplicaton.domain.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionHandling {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String ACCOUNT_LOCKED = "Your account has been locked. Please contact support.";
    private static final String METHOD_IS_NOT_ALLOWED = "This request method is not allowed.";
    private static final String INTERNAL_SERVER_ERROR_MSG = "An Error Occurred. Please try again later.";
    private static final String INCORRECT_CREDENTIALS = "Username or password is incorrect. Please try again.";
    private static final String ACCOUNT_DISABLE = "Your account has been disabled. Please contact support.";
    private static final String ERROR_PROCESSING_FILE = "Error occurred while processing file:";
    private static final String NOT_ENOUGH_PERMISSION = "You do not have permission to perform this action.";

    @ExceptionHandler(value = {DisabledException.class})
    public ResponseEntity<HttpResponse> accountDisableException() {
        return createHttpResponse(BAD_REQUEST, ACCOUNT_DISABLE);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    public ResponseEntity<HttpResponse> badCredentialsException() {
        return createHttpResponse(BAD_REQUEST, INCORRECT_CREDENTIALS);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<HttpResponse> accessDeniedException() {
        return createHttpResponse(FORBIDDEN, NOT_ENOUGH_PERMISSION);
    }

    @ExceptionHandler(value = {LockedException.class})
    public ResponseEntity<HttpResponse> lockedException() {
        return createHttpResponse(UNAUTHORIZED, ACCOUNT_LOCKED);
    }

    @ExceptionHandler(value = {TokenExpiredException.class})
    public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException ex) {
        return createHttpResponse(UNAUTHORIZED, ex.getMessage().toUpperCase());
    }

    @ExceptionHandler(value = {EmailExistsException.class})
    public ResponseEntity<HttpResponse> emailExistsException(EmailExistsException ex) {
        return createHttpResponse(BAD_REQUEST, ex.getMessage().toUpperCase());
    }

    @ExceptionHandler(value = {UserNameExistsException.class})
    public ResponseEntity<HttpResponse> userNameExistException(UserNameExistsException ex) {
        return createHttpResponse(BAD_REQUEST, ex.getMessage().toUpperCase());
    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException ex) {
        return createHttpResponse(BAD_REQUEST, ex.getMessage().toUpperCase());
    }

    @ExceptionHandler(value = {EmailNotFoundException.class})
    public ResponseEntity<HttpResponse> emailNotFoundException(EmailNotFoundException ex) {
        return createHttpResponse(BAD_REQUEST, ex.getMessage().toUpperCase());
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        HttpMethod supportedMethod = Objects.requireNonNull(ex.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED + "%s", supportedMethod));
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<HttpResponse> internalServerErrorException(Exception ex) {
        LOGGER.error(ex.getMessage());
        return createHttpResponse(INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }

    @ExceptionHandler(value = {NoResultException.class})
    public ResponseEntity<HttpResponse> notFoundException(NoResultException ex) {
        LOGGER.error("Exception:{}", ex.getMessage());
        return createHttpResponse(NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(value = {IOException.class})
    public ResponseEntity<HttpResponse> ioException(IOException ex) {
        LOGGER.error(ex.getMessage());
        return createHttpResponse(INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
    }

    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public ResponseEntity<HttpResponse> ioException(NoHandlerFoundException ex) {
        return createHttpResponse(BAD_REQUEST, "This page was not found.");
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus status, String message) {
        return new ResponseEntity<>(new HttpResponse(status.value(), status, status.getReasonPhrase(), message.toUpperCase()), status);
    }


}
