package com.zozak.word_api.exception;

public class BaseException extends RuntimeException {
  public final Integer statusCode;

  public BaseException(ErrorMessage errorMessage) {
    super(errorMessage.prepareMessage());
    this.statusCode = errorMessage.getMessage().getStatus();
  }
}
