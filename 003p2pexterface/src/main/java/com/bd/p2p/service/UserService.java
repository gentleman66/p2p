package com.bd.p2p.service;

import com.bd.p2p.model.user.User;

/**
 * 用户 业务层
 */
public interface UserService {
    //获取平台注册人数
    Integer queryAllUserCount();

    /**
     *校验手机号码是否唯一
     * @param user
     * @return
     */
    public String checkPhoneUnique(User user);

    /**
     * 根据手机号和密码注册
     * @param user
     */
    Integer register(User user);

    /**
     * 查看身份证号是否唯一
     * @param idCard
     * @return
     */
    String checkIdCardUnique(String idCard);

    /**
     * 根据手机号查用户信息
     * @param phone
     * @return
     */
    User queryUserByPhone(String phone);

    /**
     * 根据用户标识更新用户信息
     * @param user
     * @return
     */
    Integer modifyUserById(User user);

    /**
     * 根据手机号和密码查用户信息
     * @param phone
     * @param loginPassword
     * @return
     */
    User queryUserByPhonePassword(String phone, String loginPassword);
}
