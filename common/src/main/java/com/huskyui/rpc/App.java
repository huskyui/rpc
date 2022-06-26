package com.huskyui.rpc;

import com.google.protobuf.InvalidProtocolBufferException;
import com.huskyui.rpc.model.Message;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws InvalidProtocolBufferException {
        Message message = Message.newBuilder()
                .setMessageType(Message.MessageType.ONLINE)
                .setBody("hello i am online")
                .build();
        byte[] bytes = message.toByteArray();
        System.out.println(bytes);

        Message message1 = Message.parseFrom(bytes);
        System.out.println(message1);


    }
}
