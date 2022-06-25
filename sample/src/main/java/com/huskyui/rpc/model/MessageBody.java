package com.huskyui.rpc.model;

/**
 * @author 王鹏
 */
public class MessageBody {
    private String msg;

    public MessageBody(String msg) {
        this.msg = msg;
    }

    public MessageBody() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
