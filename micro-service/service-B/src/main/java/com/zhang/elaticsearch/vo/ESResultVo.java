package com.zhang.elaticsearch.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: Mr.ZHANG
 * @Date: 2018/12/24 0024 下午 2:49
 */
@Data
public class ESResultVo implements Serializable {
    private static final long serialVersionUID = 1372566732630952214L;

    private long count;

    private List<String> rowkey;

    private List<Map<String, Object>> dataList;

}
