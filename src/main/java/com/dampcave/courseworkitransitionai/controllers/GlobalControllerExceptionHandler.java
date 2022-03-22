package com.dampcave.courseworkitransitionai.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public void handleConflict() {
        // Nothing to do
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ApiError handleMethodArgNotValid(MethodArgumentNotValidException exception, HttpServletRequest request){
//        ApiError error = new ApiError(400, exception.getMessage(), request.getServletPath());
//        BindingResult bindingResult = exception.getBindingResult();
//        Map<String, String> validationErrors = new HashMap<>();
//        for(FieldError fieldError: bindingResult.getFieldErrors()){
//            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
//        }
//        error.setValidationErrors(validationErrors);
//        return error;
//    }

}
