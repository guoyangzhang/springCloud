package com.zhang.elaticsearch.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: Mr.ZHANG
 * @Date: 2018/12/24 0024 下午 2:08
 */
@Data
public class IndexParam implements Serializable {

    private static final long serialVersionUID = -8820889220229014823L;
    private String[] aliasesName;

    private String[] indexName;

    private String[] indexType;

    private String[] fileds;

    private String[] excludeFiles;

    private String queryParam;

    private String[] orderFoeld;

    private String[] orderFlag;
    @Deprecated
    private int num = 100;
    private int pageNum = 1;
    private int pageSize = 100;
    public IndexParam() {
    }

}
