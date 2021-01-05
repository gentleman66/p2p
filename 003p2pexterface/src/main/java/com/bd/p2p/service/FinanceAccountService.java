package com.bd.p2p.service;

import com.bd.p2p.model.user.FinanceAccount;

/**
 * ClassName:FinanceAccountService
 * Package:com.bd.p2p.service
 * Description: 描述信息
 *
 * @date:2020/12/24 17:46
 * @author:动力节点
 */
public interface FinanceAccountService {
    /**
     * 根据uid查询用户金额信息
     * @param uid
     */
    FinanceAccount queryFinanceAccounByUid(Integer uid);
}
