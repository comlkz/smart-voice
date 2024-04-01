package org.comlkz.ai.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AiRequest implements Serializable {

    private List<SegmentInfo> segments;
}
