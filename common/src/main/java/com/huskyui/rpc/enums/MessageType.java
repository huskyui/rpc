package com.huskyui.rpc.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author 王鹏
 */
public enum MessageType {
    HEART_BEAT(1),
    ONLINE(2),
    RESPONSE(3);
    private int type;

    MessageType() {
    }

    MessageType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public static MessageType getMessageType(int type){
        for(MessageType messageType : values()){
            if (messageType.type == type) {
                return messageType;
            }
        }
        return null;
    }
}
