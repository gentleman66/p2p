package com.bd.p2p.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bd.p2p.model.loan.LoanInfo;
import com.bd.p2p.service.BidInfoService;
import com.bd.p2p.service.LoanInfoService;
import com.bd.p2p.service.UserService;
import common.constant.Constant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    /*投标产品信息表接口*/
    @Reference(interfaceClass = LoanInfoService.class,version = "1.0.0",check = false)
    private LoanInfoService loanInfoService;

    /*用户表接口*/
    @Reference(interfaceClass = UserService.class,version = "1.0.0",check = false)
    private UserService userService;

    /*用户投标信息表接口*/
    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0",check = false)
    private BidInfoService bidInfoService;

    @RequestMapping(value = "/index")
    public String doSome(Model model){
        //获取产品的年化历史平均收益率
        Double aDouble = loanInfoService.queryHistoryAnnualRate();
        // 方法一：输出时对double进行格式化保留两位小数 四舍五入
        DecimalFormat df=new DecimalFormat("0.00");
        String queryHistoryAnnualRate = df.format(aDouble);
        model.addAttribute(Constant.HISTORY_ANNUAL_RATE,queryHistoryAnnualRate);
        //获取平台注册人数
        Integer queryAllUserCount = userService.queryAllUserCount();
        model.addAttribute(Constant.All_User_Count,queryAllUserCount);
        //获取累计成交金额
        Double queryAllBidMoney = bidInfoService.queryAllBidMoney();
        model.addAttribute(Constant.All_Bid_Money,queryAllBidMoney);

        // 页面显示的三种产品只是product_type的值不一样
        // 获取新手宝产品信息 product_type=0,起始位置 currentPage=0, pageSize=1
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("currentPage",0);
        paramMap.put("pageSize",1);
        paramMap.put("productType",Constant.PRODUCT_TYPE_X);
        List<LoanInfo> xLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);
        model.addAttribute("xLoanInfoList",xLoanInfoList);

        // 获取优选类产品信息 product_type=1,起始位置 currentPage=0, pageSize=4
        paramMap.put("pageSize",4);
        paramMap.put("productType",Constant.PRODUCT_TYPE_U);
        List<LoanInfo> uLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);
        model.addAttribute("uLoanInfoList",uLoanInfoList);

        // 获取散标类产品信息 product_type=2,起始位置 currentPage=0, pageSize=8
        paramMap.put("pageSize",8);
        paramMap.put("productType",Constant.PRODUCT_TYPE_S);
        List<LoanInfo> sLoanInfoList = loanInfoService.queryLoanInfoListByProductType(paramMap);
        model.addAttribute("sLoanInfoList",sLoanInfoList);
        return "index";
    }
    //跳转到登录
    @RequestMapping(value = "/loan/page/login")
    public String login(){
        return "login";
    }
    //跳转到注册
    @RequestMapping(value = "/loan/page/register")
    public String  register(){
        return "register";
    }
}
