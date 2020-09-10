package com.husky.hrpc.sever;

import io.netty.channel.ChannelHandlerContext;


/**
 * @author huskyui
 */

public final class CtxHolder {
    private ChannelHandlerContext ctx;

    public void set(ChannelHandlerContext ctx){
        this.ctx = ctx;
    }

}
