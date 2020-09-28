package com.husky.hrpc.util;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author huskyui
 */

public class NetUtil {

    public static int getIdlePort() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        String hostAddress = inetAddress.getHostAddress();
        int idlePort = -1;
        for (int port = 10240; port <= 65535; port++) {
            if (!portIsUsed(hostAddress, port)) {
                idlePort = port;
                break;
            }
        }
        if (idlePort == -1) {
            throw new RuntimeException("hostAddress: " + hostAddress + " 端口 :[10240,65535] 不存在空闲端口");
        }
        return idlePort;
    }

    public static boolean portIsUsed(String host, int port) {
        Socket socket = null;
        try {
            InetAddress address = InetAddress.getByName(host);
            socket = new Socket(address, port);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
