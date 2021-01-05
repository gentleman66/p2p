package com.bd.p2p.service;

import com.bd.p2p.model.user.User;

/**
 * ClassName:RegisterService
 * Package:com.bd.p2p.service
 * Description: 描述信息
 *
 * @date:2020/12/21 11:28
 * @author:动力节点
 */
public interface RegisterService {

    /**
     * 查看手机号是否唯一
     * @param user
     * @return
     */
    String checkPhoneUnique(User user);



    /**
     * 查看身份证号是否唯一
     * @param idCard
     * @return
     */
    String checkIdCardUnique(String idCard);

}
