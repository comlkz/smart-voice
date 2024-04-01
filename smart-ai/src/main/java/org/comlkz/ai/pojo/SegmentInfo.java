package org.comlkz.ai.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SegmentInfo {

    private String text;

    /**
     * 1：对方 2：自己
     */
    private Integer role;


}
