package com.husky.hrpc.util;

import java.util.List;

/**
 * @author huskyui
 */

public class RandomUtil {

    public static Object getRandomOne(List list) {
        if (list == null || list.isEmpty()) {
            throw new RuntimeException("error");
        }
        int randomIndex = (int) (Math.random() * list.size());
        return list.get(randomIndex);
    }
}
