package org.comlkz.ai.service.xunfei.pojo;

import lombok.Data;

import java.util.List;

@Data
public class XunFeiSt {

    /**
     * 单个句子的开始时间，单位毫秒
     */
    private String bg;

    /**
     * 单个句子的结束时间，单位毫秒
     */
    private String ed;

    /**
     * 分离的角色编号，取值正整数，需开启角色分离的功能才返回对应的分离角色编号
     */
    private String rl;

    private List<XunFeiWs> rt;
}
