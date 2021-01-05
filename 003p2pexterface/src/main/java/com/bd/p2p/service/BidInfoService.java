package com.bd.p2p.service;

import com.bd.p2p.model.loan.BidInfo;
import com.bd.p2p.model.loan.RechargeRecord;
import com.bd.p2p.model.vo.BidUserTop;
import com.bd.p2p.model.vo.PaginationVO;

import java.util.List;
import java.util.Map;

/**
 * 用户投标信息表接口
 */
public interface BidInfoService {
    //获取累计成交金额
    Double queryAllBidMoney();
    /**
     * 根据该产品的id获取用户投标信息
     * @param loanId
     */
    List<BidInfo> queryBidInfo(Integer loanId);

    /**
     * 用户投资
     * @param paramMap
     * @return
     */
    Object invest(Map<String, Object> paramMap);

    /**
     * 根据uid获取投资信息
     * @param uid
     * @return
     */
    List<BidInfo> queryBidInfoByUid(Integer uid);

    /**
     * 从redis缓存中获取用户投资排行榜
     * @return
     */
    List<BidUserTop> queryBidUserTop();

    /**
     * 查出所有的投资记录
     * @param paramMap
     * @return
     */
    PaginationVO<BidInfo> queryAllBidInfoByUid(Map<String, Object> paramMap);
}
