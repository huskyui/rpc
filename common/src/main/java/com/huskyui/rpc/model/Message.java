package com.huskyui.rpc.model;

import com.huskyui.rpc.enums.MessageType;
import lombok.Data;

/**
 * @author 王鹏
 */
@Data
public class Message {
    private MessageType messageType;

    private String body;

    public Message(MessageType messageType, String body) {
        this.messageType = messageType;
        this.body = body;
    }
}
