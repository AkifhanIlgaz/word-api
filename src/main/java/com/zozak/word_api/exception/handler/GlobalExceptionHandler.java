package com.zozak.word_api.exception.handler;

import com.zozak.word_api.exception.BaseException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<ApiError<?>> handleBaseException(BaseException e, WebRequest request) {
        return ResponseEntity.status(e.statusCode).body(createApiError(e.getMessage(), request));
    }

    public List<String> add(List<String> list , String newValue){
        list.add(newValue);
        return list;
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex , WebRequest request) {
        Map<String, List<String>> map = new HashMap<>();

        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        for (ObjectError objectError : allErrors) {
            String fieldName = ((FieldError)objectError).getField();
            if(map.containsKey(fieldName)) {
                map.put(fieldName, add(map.get(fieldName), objectError.getDefaultMessage()));
            }else {
                map.put(fieldName, add(new ArrayList<>(), objectError.getDefaultMessage()));
            }
        }

        return ResponseEntity.badRequest().body(createApiError(map, request));
    }


    public <E> ApiError<E> createApiError(E message, WebRequest request) {
        ApiError<E> apiError = new ApiError<>();
        Error<E> exception = new Error<>();

        exception.setTimestamp(new Date());
        exception.setMessage(message);
        exception.setPath(request.getDescription(false).substring(4));

        apiError.setError(exception);

        return apiError;
    }

}
