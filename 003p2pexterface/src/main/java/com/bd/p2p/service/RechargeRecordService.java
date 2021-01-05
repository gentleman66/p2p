package com.bd.p2p.service;

import com.bd.p2p.model.loan.RechargeRecord;
import com.bd.p2p.model.vo.PaginationVO;

import java.util.List;
import java.util.Map;

/**
 * ClassName:RechargeRecordService
 * Package:com.bd.p2p.service
 * Description: 描述信息
 *
 * @date:2020/12/25 22:07
 * @author:动力节点
 */
public interface RechargeRecordService {

    int addRechargeRecode(RechargeRecord rechargeRecord);
    /**
     * 根据用户id查询充值信息
     * @param
     * @return
     */
    PaginationVO<RechargeRecord> queryRechargeByUid(Map<String, Object> paramMap);

    /**
     * 根据用户id和订单号更新充值信息
     * @param rechargeRecord
     * @return
     */
    Integer editRecordStaus(RechargeRecord rechargeRecord);
}
