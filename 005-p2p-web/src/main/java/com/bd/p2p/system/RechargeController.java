package com.bd.p2p.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.bd.p2p.RedisService;
import com.bd.p2p.model.loan.RechargeRecord;
import com.bd.p2p.model.user.User;
import com.bd.p2p.service.RechargeRecordService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import common.constant.UserConstants;
import common.util.DateUtils;
import common.util.HttpClientUtils;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName:RechargeController
 * Package:com.bd.p2p.system
 * Description: 描述信息
 *
 * @date:2020/12/25 21:35
 * @author:动力节点
 */
@Controller
public class RechargeController {
    @Reference(interfaceClass = RedisService.class,version = "1.0.0",check = false)
    private RedisService redisService;

    @Reference(interfaceClass = RechargeRecordService.class, version = "1.0.0", check = false, timeout = 15000)
    private RechargeRecordService rechargeRecordService;
    @RequestMapping(value = "/loan/toAliPayRecharge")
    public String aliPayRecharge(Double rechargeMoney) {
        System.out.println(rechargeMoney);
        System.out.println("支付宝平台充值");
        return "";
    }

    //跳转到充值页面
    @RequestMapping(value = "/loan/page/toRecharge")
    public String toRecharge(Model model,HttpServletRequest request){

        return "toRecharge";
    }
    @RequestMapping(value = "/loan/toWxPayRecharge")
    public String wxPayRecharge(HttpServletRequest request, Model model, Double rechargeMoney){

        try {
            // 发起微信支付
            String rechargeNo = "";
            // 生成充值记录
            // 从session中获取用户信息
            User sessionUser = (User) request.getSession().getAttribute(UserConstants.SESSION_USER);

            RechargeRecord rechargeRecord = new RechargeRecord();
            rechargeRecord.setUid(sessionUser.getId());
            rechargeRecord.setRechargeMoney(rechargeMoney);
            rechargeRecord.setRechargeStatus("0"); //0 充值中、1. 充值成功、 2 充值失败
            rechargeRecord.setRechargeTime(new Date());
            rechargeRecord.setRechargeDesc("微信充值");

            // 全局唯一订单号 = 时间戳 + redis唯一数组
            rechargeNo = DateUtils.getTimestramp() + redisService.getOnlyNumber();

            rechargeRecord.setRechargeNo(rechargeNo);

            int rechargeRecode = rechargeRecordService.addRechargeRecode(rechargeRecord);

            if (rechargeRecode <= 0) {
                model.addAttribute("target_msg","新增充值记录失败");
                return "toRechargeBack";
            }

            //向pay工程支付方法传递参数
            model.addAttribute("rechargeNo",rechargeNo);
            model.addAttribute("rechargeMoney",rechargeMoney);
            model.addAttribute("rechargeTime",new Date());


        }catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("target_msg","新增充值记录失败");
            return "toRechargeBack";
        }


        return "showQRcode";
    }
    @RequestMapping(value = "/loan/generateQRcode")
    public void generateQRcode(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(value = "rechargeNo", required = true) String rechargeNo,
                               @RequestParam(value = "rechargeMoney", required = true) Double rechargeMoney) throws Exception {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("body","微信扫码支付");
        paramMap.put("out_trade_no", rechargeNo);
        paramMap.put("total_fee", rechargeMoney);

        // 调用pay工程统一下单API接口
        String jsonString = HttpClientUtils.doPost("http://localhost:9090/api/wxpay", paramMap);

        JSONObject jsonObject = JSONObject.parseObject(jsonString);

        String returnCode = jsonObject.getString("return_code");

        // 判断通信标识
        if (!StringUtils.equals("SUCCESS",returnCode)) {
            response.sendRedirect(request.getContextPath()+"/loan/toRechargeBack");
        }

        String resultCode = jsonObject.getString("result_code");
        if (!StringUtils.equals("SUCCESS",resultCode)) {
            response.sendRedirect(request.getContextPath()+"/loan/toRechargeBack");

        }

        // 获取code url
        String code_url = jsonObject.getString("code_url");

        // 将code_url 生成一个二维码图片
        Map<EncodeHintType,Object> encodeHintTypeObjectMap = new HashMap<>();
        encodeHintTypeObjectMap.put(EncodeHintType.CHARACTER_SET,"UTF-8");

        BitMatrix bitMatrix = new MultiFormatWriter().encode(code_url, BarcodeFormat.QR_CODE,200,200,encodeHintTypeObjectMap);

        OutputStream outputStream = response.getOutputStream();
        // 将矩阵转换成流对象
        MatrixToImageWriter.writeToStream(bitMatrix,"jpg",outputStream);

        outputStream.flush();
        outputStream.close();
    }

    /**
     * 接收充值结果
     * @param resultMap
     * @return
     */
    @RequestMapping(value = "/loan/wxpayNotify2")
    public Object wxpayNotify(Map<String, String> resultMap,HttpServletRequest request){
        User sessionUser = (User) request.getSession().getAttribute(UserConstants.SESSION_USER);
        //获取充值结果
        String return_code= resultMap.get("notify_url");

        if(return_code.equals("SUCCESS")){
            //充值成功
            RechargeRecord rechargeRecord=new RechargeRecord();
            //获取订单号
            String transaction_id = resultMap.get("transaction_id");

            //设置订单号
            rechargeRecord.setRechargeNo(transaction_id);
            //获取用户id
            rechargeRecord.setUid(sessionUser.getId());
            //设置充值结果状态
            rechargeRecord.setRechargeStatus("1");
            //更新该订单充值结果状态
            Integer count= rechargeRecordService.editRecordStaus(rechargeRecord);

        }else {
            //充值失败

        }
        return "hello";
    }
}
