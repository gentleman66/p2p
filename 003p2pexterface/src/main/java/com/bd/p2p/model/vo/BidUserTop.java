package com.bd.p2p.model.vo;

import java.io.Serializable;

/**
 * ClassName:BidUserTop
 * Package:com.bd.p2p.model.vo
 * Description: 描述信息
 *
 * @date:2020/12/25 19:27
 * @author:动力节点
 */
public class BidUserTop implements Serializable {

    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 分数，累计投资金额
     */
    private Double score;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}
