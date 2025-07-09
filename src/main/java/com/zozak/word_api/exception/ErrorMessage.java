package com.zozak.word_api.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorMessage {
    private String ofStatic;
    private MessageType message;

    public String prepareMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(ofStatic);
        sb.append(" : ");
        sb.append(message.getMessage());
        return sb.toString();
    }
}
