package com.bd.p2p.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bd.p2p.model.user.User;
import com.bd.p2p.service.BidInfoService;
import common.constant.Constant;
import common.constant.UserConstants;
import common.domain.AjaxResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:BidInfoController
 * Package:com.bd.p2p.web
 * Description: 描述信息 投资信息表
 *
 * @date:2020/12/25 10:28
 * @author:动力节点
 */
@Controller
public class BidInfoController {
    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0",check = false)
    private BidInfoService bidInfoService;

    @GetMapping(value = "/invest")
    @ResponseBody
    public Object invest(HttpServletRequest request,
                         @RequestParam(value = "loanId",required = true)Integer loanId,
                         @RequestParam(value = "bidMoney",required = true)Double bidMoney){

        //从session中获取用户信息
        User sessionUser= (User) request.getSession().getAttribute(UserConstants.SESSION_USER);
        //准备请求参数
        Map<String,Object> paramMap=new HashMap<>();
        //用户标识
        paramMap.put("uid",sessionUser.getId());
        //产品标识
        paramMap.put("loanId",loanId);
        //投资金额
        paramMap.put("bidMoney",bidMoney);
        //用户手机号
        paramMap.put("phone",sessionUser.getPhone());
        //用户投资（用户标识，产品标识，投资金额）
        String msg = (String) bidInfoService.invest(paramMap);

        return msg.equals(Constant.RESULT_SUCCESS)? AjaxResult.success():AjaxResult.error(msg);
    }
}
