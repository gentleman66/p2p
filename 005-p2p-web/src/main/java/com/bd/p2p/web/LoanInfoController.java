package com.bd.p2p.web;

import com.alibaba.dubbo.config.annotation.Reference;
import com.bd.p2p.model.loan.BidInfo;
import com.bd.p2p.model.loan.LoanInfo;
import com.bd.p2p.model.user.FinanceAccount;
import com.bd.p2p.model.user.User;
import com.bd.p2p.model.vo.BidUserTop;
import com.bd.p2p.model.vo.PaginationVO;
import com.bd.p2p.service.*;
import common.constant.Constant;
import common.constant.UserConstants;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投标产品信息表的web层
 */
@Controller
public class LoanInfoController {
    /*投标产品信息表接口*/
    @Reference(interfaceClass = LoanInfoService.class,version = "1.0.0",check = false)
    private LoanInfoService loanInfoService;
    /*用户表接口*/
    @Reference(interfaceClass = UserService.class,version = "1.0.0",check = false)
    private UserService userService;

    /*用户投标信息表接口*/
    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0",check = false)
    private BidInfoService bidInfoService;
    @Reference(interfaceClass = FinanceAccountService.class,version = "1.0.0",check = false)
    private FinanceAccountService financeAccountService;
    @Reference(interfaceClass = IncomeRecordService.class,version = "1.0.0",check = false)
    private IncomeRecordService incomeRecordService;
    /**
     * 查看更多散标产品
     * @return
     */
    @RequestMapping("/loan/loan")
    public String loan(Model model,@RequestParam(value = "ptype", required = false) Integer ptype,
                       @RequestParam(value = "currentPage",required = false, defaultValue = "1") Integer currentPage){

        //查询产品列表  共20条3页　当前为第2页
        /** List<LoanInfo>
         * 返回 产品列表  共20条3页　当前为第2页
         * 每页显示的条数是固定的，
         * 请求方法提供的参数 产品类型(ptype) 当前页(current) 每页显示的条数(固定9条)
         * 请求方法返回的数据 List<LoanInfo> 、 总条数
         */
        int pageSize = 9;
        Map<String, Object> paramMap = new HashMap<String, Object>();

        if (ObjectUtils.allNotNull(ptype)) {
            paramMap.put("productType", ptype);
        }
        paramMap.put("currentPage",(currentPage - 1) * pageSize);
        paramMap.put("pageSize",pageSize);

        // VO对象 ValueObject 值对象，
        PaginationVO<LoanInfo> paginationVO = loanInfoService.queryLoanInfoListByPage(paramMap);
        // 要求某对象有：LoanInfo属性 还要求有一个总条数属性  PaginationVO

        // 计算总页数
         Integer totalPage = paginationVO.getTotal().intValue() / pageSize;
        Integer mod = paginationVO.getTotal().intValue() % pageSize;
        if (mod > 0) {
            totalPage = totalPage + 1;
        }
        // 将数据给到页面
        model.addAttribute("loanInfoList",paginationVO.getDataList());
        model.addAttribute("totalRows",paginationVO.getTotal());
        model.addAttribute("totalPage",totalPage);
        model.addAttribute("currentPage",currentPage);

        if (ObjectUtils.allNotNull(ptype)) {
            model.addAttribute("productType",ptype);
        }


        //投资排行榜
        //TODO
        List<BidUserTop> bidUserTopList=bidInfoService.queryBidUserTop();
        //用户手机号保留前三位与最后两位
        for(BidUserTop i : bidUserTopList){
            String mobile=i.getPhone();
            char[] m =  mobile.toCharArray();
            for(int j=0; j<m.length;j++){
                if(j>2 && j<9){
                    m[j] = '*';
                }
            }
            String mobileHide =  String.valueOf(m);
            i.setPhone(mobileHide);
        }
        model.addAttribute("bidUserTopList",bidUserTopList);
        return "loan";
    }
    //用户点击“立即投资”跳转至产品详情页面
    @RequestMapping(value = "/loan/loanInfo")
    public String loanInfo(Model model,HttpServletRequest request,Integer LoanId){
        //获取该产品的投资时间，投资金额，
        //获取投资人联系方式
        List<BidInfo> bidInfoList = bidInfoService.queryBidInfo(LoanId);
        //投资记录中，用户手机号保留前三位与最后两位
        for (BidInfo i:bidInfoList){
            String mobile=i.getUser().getPhone();
            char[] m =  mobile.toCharArray();
            for(int j=0; j<m.length;j++){
                if(j>2 && j<9){
                    m[j] = '*';
                }
            }
            String mobileHide =  String.valueOf(m);
            i.getUser().setPhone(mobileHide);
        }
        model.addAttribute(Constant.BID_INFO_LIST,bidInfoList);
        //根据产品id获取产品信息
        List<LoanInfo> loanInfoList = loanInfoService.queryLoanInfoByLoanId(LoanId);
        model.addAttribute("loanInfoList",loanInfoList);
        //判断用户是否登录
        User user=(User) request.getSession().getAttribute("user");
        if(user!=null){

        }
        //获取当前用户信息
        User sessionUser = (User) request.getSession().getAttribute(UserConstants.SESSION_USER);
        //判断用户是否登录
        if(user!=null){
            //获取当前用户可用余额
            FinanceAccount financeAccount = financeAccountService.queryFinanceAccounByUid(sessionUser.getId());
            model.addAttribute("financeAccount",financeAccount);
        }
        return "loanInfo";
    }


}
