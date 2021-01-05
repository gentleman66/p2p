package com.bd.p2p.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.support.Parameter;
import com.bd.p2p.model.loan.LoanInfo;
import com.bd.p2p.model.user.FinanceAccount;
import com.bd.p2p.model.user.User;
import com.bd.p2p.service.BidInfoService;
import com.bd.p2p.service.FinanceAccountService;
import com.bd.p2p.service.LoanInfoService;
import com.bd.p2p.service.UserService;
import common.constant.Constant;
import common.constant.UserConstants;
import common.domain.AjaxResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:UserController
 * Package:com.bd.p2p.system
 * Description: 描述信息
 *
 * @date:2020/12/24 9:27
 * @author:动力节点
 */
@Controller
public class UserController {
    @Reference(interfaceClass = UserService.class,version = "1.0.0",check = false)
    private UserService userService;
    @Reference(interfaceClass = LoanInfoService.class,version = "1.0.0",check = false)
    private LoanInfoService loanInfoService;
    @Reference(interfaceClass = BidInfoService.class,version = "1.0.0",check = false)
    private BidInfoService bidInfoService;
    @Reference(interfaceClass = FinanceAccountService.class,version = "1.0.0",check = false)
    private FinanceAccountService financeAccountService;
    /**
     * 用户登录
     * @param request
     * @param phone
     * @param loginPassword
     * @return
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public Object login(HttpServletRequest request,
                        @RequestParam(value = "phone",required = true)String phone,
                        @RequestParam(value = "loginPassword",required = true)String loginPassword){

        //根据手机号和密码查用户信息
        User user= userService.queryUserByPhonePassword(phone,loginPassword);
        //判断user是否为空
        if(user==null){
            //查询是不是密码不对
            if(userService.queryUserByPhone(phone)!=null){
                return AjaxResult.error(UserConstants.LOGIN_PASSWORD_FALSE);
            }
            return AjaxResult.error(UserConstants.user_Login_false);
        }
        //将用户信息存入session
        request.getSession().setAttribute(UserConstants.SESSION_USER,user);

        return AjaxResult.success();
    }
    //登陆后点击退出
    @RequestMapping(value = "/loan/logout")
    public String logout(HttpServletRequest request){
        //让session失效或者清楚指定session中key值
        request.getSession().invalidate();
        /*request.getSession().removeAttribute(UserConstants.SESSION_USER);*/


        //return "login";
        //跳转到首页需要重定向，因为index是一个请求
        return "redirect:/index";
    }

    @GetMapping(value = "/loadStat")
    public @ResponseBody Object loadStat(HttpServletRequest request){
        Map<String,Object> setMap= new HashMap<String, Object>();
        //历史平均年华收益率
        Double aDouble = loanInfoService.queryHistoryAnnualRate();
        DecimalFormat df=new DecimalFormat("0.00");
        String queryHistoryAnnualRate = df.format(aDouble);
        //平台注册总人数
        Integer queryAllUserCount = userService.queryAllUserCount();
        //累计投资金额
        Double queryAllBidMoney = bidInfoService.queryAllBidMoney();
        setMap.put(Constant.HISTORY_ANNUAL_RATE,queryHistoryAnnualRate);
        setMap.put(Constant.All_User_Count,queryAllUserCount);
        setMap.put(Constant.All_Bid_Money,queryAllBidMoney);
        return setMap;
    }

    @GetMapping(value = "/queryAvailableMoney")
    public @ResponseBody Object queryAvailableMoney(Integer uid){

        FinanceAccount financeAccount= financeAccountService.queryFinanceAccounByUid(uid);
        if(financeAccount!=null){
            return financeAccount;
        }
        return null;
    }
}
