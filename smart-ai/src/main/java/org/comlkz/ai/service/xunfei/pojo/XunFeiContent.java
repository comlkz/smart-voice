package org.comlkz.ai.service.xunfei.pojo;

import lombok.Data;
import org.comlkz.ai.service.xunfei.VoiceToTextService;

@Data
public class XunFeiContent {

    private XunFeiOrderInfo orderInfo;

    private String orderResult;
}
