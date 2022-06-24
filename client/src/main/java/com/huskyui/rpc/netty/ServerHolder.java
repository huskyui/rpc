package com.huskyui.rpc.netty;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 王鹏
 */
public class ServerHolder {

    public static Map<String, List<Channel>> HOLDER = new HashMap<>();



}
