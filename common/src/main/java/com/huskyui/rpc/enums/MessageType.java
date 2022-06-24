package com.huskyui.rpc.enums;

/**
 * @author 王鹏
 */
public enum MessageType {
    HEART_BEAT((byte)1),
    ONLINE((byte)2);

    private byte type;

    MessageType(byte type) {
        this.type = type;
    }
}
