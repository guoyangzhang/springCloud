package com.zhang.study.entity;

import com.zhang.study.entity.vo.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * Created by Mr.zhang on 2019/8/7
 */
@Data
public class TreeDemo extends BaseEntity {

    private String guid;

    private String name;

    private String parentId;

    private String pathUrl;

    private String rootName;

    private List<TreeDemo> childList;

    private Boolean isHidden;

    private Boolean isFile;

    private String parentPathUrl;

}
