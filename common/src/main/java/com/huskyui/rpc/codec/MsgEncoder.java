package com.huskyui.rpc.codec;

import com.huskyui.rpc.constant.Constant;
import com.huskyui.rpc.model.Message;
import com.huskyui.rpc.utils.JsonUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

import java.nio.charset.StandardCharsets;

/**
 * @author 王鹏
 */
public class MsgEncoder extends MessageToByteEncoder {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object in, ByteBuf byteBuf) throws Exception {
        if (in instanceof Message){
            String jsonStr = JsonUtils.toJson(in);
            byte[] messageBytes= jsonStr.getBytes(CharsetUtil.UTF_8);
            byte[] delimiterBytes = Constant.DELIMITER.getBytes(CharsetUtil.UTF_8);
            byte[] allBytes = new byte[messageBytes.length+delimiterBytes.length];
            System.arraycopy(messageBytes,0,allBytes,0,messageBytes.length);
            System.arraycopy(delimiterBytes,0,allBytes,messageBytes.length,delimiterBytes.length);
            byteBuf.writeBytes(allBytes);
        }
    }
}
