package com.bd.p2p.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bd.p2p.model.loan.BidInfo;
import com.bd.p2p.model.loan.LoanInfo;
import com.bd.p2p.model.loan.RechargeRecord;
import com.bd.p2p.model.user.FinanceAccount;
import com.bd.p2p.model.user.User;
import com.bd.p2p.model.vo.PaginationVO;
import com.bd.p2p.service.BidInfoService;
import com.bd.p2p.service.FinanceAccountService;
import com.bd.p2p.service.RechargeRecordService;
import common.constant.UserConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName:MyCenterController
 * Package:com.bd.p2p.web
 * Description: 描述信息
 *
 * @date:2020/12/25 16:12
 * @author:动力节点
 */
@Controller
public class MyCenterController {

    /*用户投标信息表接口*/
    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0",check = false)
    private BidInfoService bidInfoService;
    @Reference(interfaceClass = FinanceAccountService.class,version = "1.0.0",check = false)
    private FinanceAccountService financeAccountService;
    @Reference(interfaceClass = RechargeRecordService.class, version = "1.0.0", check = false, timeout = 15000)
    private RechargeRecordService rechargeRecordService;
    /**
     * 跳转到个人中心
     * @return
     */
    @RequestMapping(value = "/loan/myCenter")
    public String loanMyCenter(HttpServletRequest request, Model model){
        //从session取出user
        User sessionUser= (User) request.getSession().getAttribute(UserConstants.SESSION_USER);
        //获取资金信息
        FinanceAccount financeAccount = financeAccountService.queryFinanceAccounByUid(sessionUser.getId());
        model.addAttribute("financeAccount",financeAccount);
        //按投资时间倒序，展示最近 5 笔投资记录，
        //展示投资产品，   投资金额，投资时间

       List<BidInfo> bidInfoList= bidInfoService.queryBidInfoByUid(sessionUser.getId());


        model.addAttribute("bidInfoList",bidInfoList);
        return "myCenter" ;
    }
    //查看全部充值记录
    @RequestMapping(value = "/loan/toRecharge")
    public String toRecharge(HttpServletRequest request,Model model,
                           @RequestParam(value = "currentPage",required = false, defaultValue = "1") Integer currentPage){
        //获取session中用户信息
        User sessionUser = (User) request.getSession().getAttribute(UserConstants.SESSION_USER);
        //设置分页
        int pageSize = 5;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("currentPage",(currentPage - 1) * pageSize);
        paramMap.put("pageSize",pageSize);
        paramMap.put("uid",sessionUser.getId());
        // VO对象 ValueObject 值对象，
        PaginationVO<RechargeRecord> paginationVO =rechargeRecordService.queryRechargeByUid(paramMap);
        // 要求某对象有：LoanInfo属性 还要求有一个总条数属性  PaginationVO

        // 计算总页数
        Integer totalPage = paginationVO.getTotal().intValue() / pageSize;
        Integer mod = paginationVO.getTotal().intValue() % pageSize;
        if (mod > 0) {
            totalPage = totalPage + 1;
        }
        //将查出的充值记录放入request作用域里
        model.addAttribute("rechargeRecordList",paginationVO.getDataList());
        model.addAttribute("totalRows",paginationVO.getTotal());
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("currentPage",currentPage);
        return "myRecharge";
    }

    //查看全部投资记录
    @RequestMapping(value = "/loan/myInvest")
    public Object myInvest(HttpServletRequest request,Model model,
                           @RequestParam(value = "currentPage",required = false, defaultValue = "1") Integer currentPage){

    //获取session中用户信息
       User  sessionUser = (User) request.getSession().getAttribute(UserConstants.SESSION_USER);
        //设置分页
        int pageSize = 5;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("currentPage",(currentPage - 1) * pageSize);
        paramMap.put("pageSize",pageSize);
        paramMap.put("uid",sessionUser.getId());
        // VO对象 ValueObject 值对象，
        PaginationVO<BidInfo> paginationVO=bidInfoService.queryAllBidInfoByUid(paramMap);
        // 计算总页数
        Integer totalPage = paginationVO.getTotal().intValue() / pageSize;
        Integer mod = paginationVO.getTotal().intValue() % pageSize;
        if (mod > 0) {
            totalPage = totalPage + 1;
        }
        //将查出的充值记录放入request作用域里
        model.addAttribute("bidInfoList",paginationVO.getDataList());
        model.addAttribute("totalRows",paginationVO.getTotal());
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("currentPage",currentPage);
        return "myInvest";

    }

}
