package com.bd.p2p;

/**
 * ClassName:RedisService
 * Package:com.bd.p2p
 * Description: 描述信息
 *
 * @date:2020/12/23 11:43
 * @author:动力节点
 */
public interface RedisService {


    void put(String key, String value);

    Object getKey(Object MobileVerification);

    Object getOnlyNumber();
}
