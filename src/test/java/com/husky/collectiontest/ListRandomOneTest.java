package com.husky.collectiontest;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author huskyui
 */
public class ListRandomOneTest {

    public static void main(String[] args) throws UnknownHostException {
        System.out.println(InetAddress.getLocalHost().getHostAddress());
    }


//    @Test
//    public void testRandomOne(){
//
//    }


    public static Object getRandomOne(List list) {
        if (list == null || list.isEmpty()) {
            throw new RuntimeException("error");
        }
        int randomIndex = (int) (Math.random() * list.size());
        return list.get(randomIndex);
    }
}
