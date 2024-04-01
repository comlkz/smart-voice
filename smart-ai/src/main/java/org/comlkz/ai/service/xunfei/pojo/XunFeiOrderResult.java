package org.comlkz.ai.service.xunfei.pojo;

import lombok.Data;

import java.util.List;

@Data
public class XunFeiOrderResult {

    private List<XunFeiLattice> lattice;

    private List lattice2;

    private Object label;
}
