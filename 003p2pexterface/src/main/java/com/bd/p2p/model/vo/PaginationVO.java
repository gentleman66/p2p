package com.bd.p2p.model.vo;

import java.io.Serializable;
import java.util.List;

/**
 * ClassName:PaginationVO
 * Package:com.bd.p2p.model.vo
 * Description: 描述信息
 *
 * @date:2020/12/19 12:45
 * @author:动力节点
 */
public class PaginationVO<T> implements Serializable {
    // 总条数
    private Long total;

    // 产品列表信息
    private List<T> dataList;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
