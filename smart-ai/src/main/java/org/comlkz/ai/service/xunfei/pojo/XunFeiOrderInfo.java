package org.comlkz.ai.service.xunfei.pojo;

import lombok.Data;

@Data
public class XunFeiOrderInfo {

    private Integer status;

    private String orderId;

    private Long originalDuration;

    private Long realDuration;
}
