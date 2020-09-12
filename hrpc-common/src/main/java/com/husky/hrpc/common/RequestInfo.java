package com.husky.hrpc.common;

import lombok.Data;

/**
 * @author huskyui
 */
@Data
public class RequestInfo {
    private String className;
    private String methodName;
    private Class[] parameterTypes;
    private Object[] parameters;
    private String requestId;
    private Object result;
}
