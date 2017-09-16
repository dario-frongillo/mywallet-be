package it.italian.coders.web.error;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler  {

    @Autowired
    @Qualifier("errorMessageSource")
    private ReloadableResourceBundleMessageSource messageSource;

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException rnfe, HttpServletRequest request) {

        Locale locale = LocaleContextHolder.getLocale();
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
        errorDetail.setTitle(messageSource.getMessage("BadCredentialsException.title",null,locale));
        errorDetail.setDetail(messageSource.getMessage("BadCredentialsException.detail",null,locale));
        errorDetail.setDeveloperMessage(rnfe.getMessage());
        errorDetail.setException(rnfe.getClass().getName());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<?> handleInvalidGrantException(OptimisticLockingFailureException rnfe, HttpServletRequest request) {

        Locale locale = LocaleContextHolder.getLocale();
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setStatus(HttpStatus.CONFLICT.value());
        errorDetail.setTitle(messageSource.getMessage("OptimisticLockingFailureException.title",null,locale));
        errorDetail.setDetail(messageSource.getMessage("OptimisticLockingFailureException.detail",null,locale));
        errorDetail.setDeveloperMessage(rnfe.getMessage());
        errorDetail.setException(rnfe.getClass().getName());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.CONFLICT);
    }


    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manve, HttpHeaders headers, HttpStatus status, WebRequest request) {

        ErrorDetail errorDetail =
                ErrorDetail.newBuilder()
                        .title("Validation Failed")
                        .detail("Input validation failed")
                        .constrainctErrors(null)
                        .validationErrors(new HashMap<String, List<ValidationError>>())
                        .exception(manve.getClass().getName())
                        .developerMessage(manve.toString())
                        .constrainctErrors(null)
                        .build();


        // Create ValidationError instances
        List<FieldError> fieldErrors =  manve.getBindingResult().getFieldErrors();

        fieldErrors.forEach(fieldError -> {

            String field=fieldError.getField();
            String message=fieldError.getDefaultMessage();
            List<ValidationError> validationErrors = errorDetail.getValidationErrors().get(field);

            if(validationErrors == null) {
                validationErrors = new ArrayList<ValidationError>();
                errorDetail.getValidationErrors().put(field, validationErrors);
            }

            ValidationError validationError = new ValidationError();
            validationError.setCode(fieldError.getCodes()[0]);
            validationError.setMessage(message);
            validationErrors.add(validationError);

        });


        return new ResponseEntity<>(errorDetail, headers, HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        Locale locale = LocaleContextHolder.getLocale();

        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setTimeStamp(new Date().getTime());
        errorDetail.setStatus(status.value());
        errorDetail.setTitle(messageSource.getMessage("HttpMessageNotReadableException.title",null,locale) );
        errorDetail.setDetail(messageSource.getMessage("HttpMessageNotReadableException.detail",null,locale) );
        errorDetail.setDeveloperMessage(ex.getMessage());
        errorDetail.setException(ex.getClass().getName());

        return handleExceptionInternal(ex, errorDetail, headers, status, request);
    }

}
