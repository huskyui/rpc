package com.huskyui.rpc.fixedlength;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author 王鹏
 */
public class FixedLengthFrameEncoder extends MessageToByteEncoder<String> {
    private int length;

    public FixedLengthFrameEncoder(int length) {
        this.length = length;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) throws Exception {
        if (msg.length() > length) {
            throw new RuntimeException("unSupport the length of message");
        }
        if (msg.length() < length) {
            msg = addSpace(msg);
        }
        out.writeBytes(msg.getBytes(CharsetUtil.UTF_8));
    }

    private String addSpace(String msg) {
        StringBuilder stringBuilder = new StringBuilder(msg);
        for (int i = msg.length(); i < length; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }
}
