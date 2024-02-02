package com.artino.service.context;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestInfo {
    /**
     * UserId
     */
    private Long uid;
    /**
     * ip
     */
    private String ip;
    /**
     * 指纹
     */
    private String fp;
}
