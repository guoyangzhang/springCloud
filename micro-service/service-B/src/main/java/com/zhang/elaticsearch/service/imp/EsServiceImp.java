package com.zhang.elaticsearch.service.imp;

import com.zhang.elaticsearch.dto.IndexParam;
import com.zhang.elaticsearch.service.EsService;
import com.zhang.elaticsearch.vo.ESResultVo;
import org.springframework.stereotype.Service;

/**
 * @Author: Mr.ZHANG
 * @Date: 2018/12/24 0024 下午 3:24
 */
@Service
public class EsServiceImp implements EsService {

    public void main() {
        ESResultVo esResultVo = queryData();
    }


    public ESResultVo queryData() {
        IndexParam indexParam = new IndexParam();
//        indexParam.setIndexName();
//        indexParam.setIndexType();
//        indexParam.setFileds();
//        indexParam.setPageNum();
//        indexParam.setPageSize();
//        indexParam.setQueryParam();

        return null;

    }
}
