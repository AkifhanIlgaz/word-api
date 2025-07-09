package com.zozak.word_api.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class RootEntity<T> {
    private Integer status;

    private T payload;

    private String errorMessage;

    public static <T> RootEntity<T> ok(T payload) {
       RootEntity<T> entity =  new RootEntity<>();

       entity.setStatus(HttpStatus.OK.value());
       entity.setPayload(payload);
       entity.setErrorMessage(null);

       return entity;
    }

    public static <T> RootEntity<T> error(String errorMessage, HttpStatus status) {
        RootEntity<T> entity =  new RootEntity<>();

        entity.setStatus(status.value());
        entity.setPayload(null);
        entity.setErrorMessage(errorMessage);

        return entity;
    }
}
