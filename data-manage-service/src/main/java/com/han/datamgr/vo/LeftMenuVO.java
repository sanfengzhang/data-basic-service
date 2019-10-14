package com.han.datamgr.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Hanl
 * @date :2019/10/11
 * @desc:
 */
@Data
public class LeftMenuVO implements Serializable {

    private String type;

    private String name;

    private String ico;

    private List<CommandInstanceVO> children;

}
