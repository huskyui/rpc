package com.husky.hrpc.common.config;

import lombok.Builder;
import lombok.Data;

/**
 * @author huskyui
 */
@Data
@Builder
public class RpcServerConfig {
    private String hostAddress;
    private Integer port;
}
