package com.husky.hrpc.common.config;

import lombok.Builder;
import lombok.Data;

/**
 * @author huskyui
 */
@Data
@Builder
public class ZkConfig {
    private String hostAddress;
    private Integer port;
}
