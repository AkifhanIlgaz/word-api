package com.zozak.word_api.exception.handler;

import com.zozak.word_api.exception.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError<E> {
    private Error<E> error;
}
