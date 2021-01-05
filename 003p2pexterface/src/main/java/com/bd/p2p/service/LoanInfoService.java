package com.bd.p2p.service;

import com.bd.p2p.model.loan.LoanInfo;
import com.bd.p2p.model.vo.PaginationVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 投标产品信息表接口
 */
public interface LoanInfoService {
    /**
     * 获取产品的年化历史平均收益率
     * @return
     */
    Double queryHistoryAnnualRate();

    // 获取新手宝产品信息根据产品类型
    List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap);
    //根据产品id获取产品信息
    List<LoanInfo>  queryLoanInfoByLoanId(Integer loanId);

    /**
     * 分页查询产品列表
     * @param paramMap
     * @return
     */
    PaginationVO<LoanInfo> queryLoanInfoListByPage(Map<String, Object> paramMap);
}
