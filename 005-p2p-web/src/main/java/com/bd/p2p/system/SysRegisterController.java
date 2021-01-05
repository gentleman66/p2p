package com.bd.p2p.system;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSONObject;
import com.bd.p2p.RedisService;
import com.bd.p2p.model.user.User;

import com.bd.p2p.service.RegisterService;
import com.bd.p2p.service.UserService;
import common.constant.Constant;
import common.constant.UserConstants;
import common.domain.AjaxResult;
import common.util.HttpClientUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * ClassName:SysRegisterController
 * Package:com.bd.p2p.system
 * Description: 描述信息 注册验证
 *
 * @date:2020/12/19 20:58
 * @author:动力节点
 */
@Controller
public class SysRegisterController {

    @Reference(interfaceClass = RegisterService.class,version = "1.0.0",check = false)
    private  RegisterService registerService;
    /*用户表接口*/
    @Reference(interfaceClass = UserService.class,version = "1.0.0",check = false)
    private UserService userService;

    @Reference(interfaceClass = RedisService.class,version = "1.0.0",check = false)
    private RedisService redisService;
    /**
     * 检查手机号是否唯一
     * @param user
     * @return
     */
    @PostMapping(value = "/checkPhoneUnique")
    @ResponseBody
    public Object ajaxRegister(User user){
        String msg = registerService.checkPhoneUnique(user);

        return "".equals(msg)? AjaxResult.success():AjaxResult.error(msg) ;
    }

    /**
     *先验证后注册
     * @param user
     * @param model
     * @return
     */
    @PostMapping(value = "/register")
    @ResponseBody
    public Object register(User user,Model model,HttpServletRequest request){
        //先验证判断手机号是否重复
        String msg =  registerService.checkPhoneUnique(user);
        if(!"".equals(msg)){
            return AjaxResult.error(msg);
        }
        Integer register = userService.register(user);
        if(register!=1){
            return AjaxResult.error(UserConstants.USER_REGISTER_FALSE);
        }

        //将注册的用户信息保存在session中
        request.getSession().setAttribute(UserConstants.SESSION_USER,userService.queryUserByPhone(user.getPhone()));

        return AjaxResult.success();
    }

    /**
     * 跳转到认证页面
     * @return
     */
    @RequestMapping(value = "/realName")
    public String realName(){
        System.out.println("222");
        return "realName";
    }

    /**
     * 检查身份证号是否唯一
     * @param idCard
     * @return
     */
    @PostMapping(value = "/checkIdCardUnique")
    @ResponseBody
    public Object checkIdCardUnique(String idCard){
        String msg= registerService.checkIdCardUnique(idCard);
        return "".equals(msg)?AjaxResult.success():AjaxResult.error(msg) ;
    }

    /**
     * 身份认证，认证通过送福利！
     * @param
     * @return
     */
    @RequestMapping(value = "/authentication")
    public Object authentication(HttpServletRequest request,
                                 @RequestParam(value = "realName",required = true)String realName,
                                 @RequestParam(value = "idCard",required = true)String idCard){

        String msg="";
        //验证参数
        if (!Pattern.matches("^[\\u4e00-\\u9fa5]{0,}$",realName)){
            return AjaxResult.error(UserConstants.REALNAME_FALSE);
        }
     /*   if(!Pattern.matches("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)\n",idCard)){
           return AjaxResult.error(UserConstants.IDCARD_FALSE);
        }*/
        //发送请求验证用户真实信息

        //更新用户信息
        User SessionUser = (User) request.getSession().getAttribute(UserConstants.SESSION_USER);
        User updateUser=new User();
        updateUser.setId(SessionUser.getId());
        updateUser.setName(realName);
        updateUser.setIdCard(idCard);
        Integer modifyUserCount= userService.modifyUserById(updateUser);
        if(modifyUserCount>0){
            //更新session中用户信息
            request.getSession().setAttribute(UserConstants.SESSION_USER,
                    userService.queryUserByPhone(SessionUser.getPhone()));
            return AjaxResult.success();
        }else {
           return AjaxResult.error(Constant.ERROR_MESSAGE);
        }

    }

    @PostMapping(value = "/checkCaptcha")
    @ResponseBody
    public Object checkCaptcha(String captcha, HttpServletRequest request){
        String msg="";
        //获取session中的图像验证码
        String sessionCaptcha  = (String) request.getSession().getAttribute(Constant.CAPTCHA);
        sessionCaptcha = sessionCaptcha.replaceAll(" ", "");
        //验证图形码是否一致
        if(!StringUtils.equalsIgnoreCase(sessionCaptcha,captcha)){
            msg=UserConstants.USER_CAPTCHA_FALSE;
        }
        return "".equals(msg)? AjaxResult.success():AjaxResult.error(msg);
    }


    /**
     * 发送手机号验证码
     * @param phone
     * @param request
     * @return
     */
    @PostMapping(value ="/messageCode" )
    @ResponseBody
    public Object messageCode(String phone,HttpServletRequest request){
       /* if(!Pattern.matches("^1[1-9]\\\\d{9}$",phone)){
            return AjaxResult.error(UserConstants.PHONE_FORMAT_FALSE);
        }*/
        //看手机号是否唯一
        User user=new User();
        user.setPhone(phone);
        if(userService.checkPhoneUnique(user).equals(UserConstants.USER_PHONE_NOT_UNIQUE)){
            return AjaxResult.error(UserConstants.USER_PHONE_FALSE);
        }
        //准备手机号认证参数
        Map<String,Object> paramMap=new HashMap<>();
        //手机号认证appkey
        paramMap.put("appkey","b9a08c9d4e362c625abd73676ac96721");
        paramMap.put("mobile",phone);

        //发送请求验证用户真实信息
        try {
            String code=this.getRandomCode(6);
            paramMap.put("msg","【巴卜技术】您的验证码是"+code+",若非本人操作请忽略");
            //返回json字符串
            String jsonString = HttpClientUtils.doPost("https://way.jd.com/BABO/sms", paramMap);
            //解析json字符串，使用fastjson，将json字符串转换为json对象
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            //获取code通信标识
            String resultCode= jsonObject.getString("code");
            //判断通信是否成功
            if(StringUtils.equals("10000",resultCode)){
               /* //通信成功
                String xmlString =  jsonObject.getString("result");
                //将xml格式的字符串转换为document对象
                Document document = DocumentHelper.parseText(xmlString);
                //根据节点路径获取节点对象
                Node node = document.selectSingleNode("//ReturnStatus[1]");
                //获取第1个节点的对象的文本内容
                String nodeText = node.getText();
                if(!StringUtils.equals(nodeText,"Success")){
                        return AjaxResult.error("发送短信异常");
                }*/


                String resultString = jsonObject.getJSONObject("result").getString("msg");

                if(!StringUtils.equals(resultString,"SUCCESS")){
                    return AjaxResult.error("发送短信异常");
                }
                // 将生成的验证码存放到redis中
                redisService.put(phone,code);

                return AjaxResult.success();
            }else {
                //通信失败
                return AjaxResult.error(Constant.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.success();
    }

    /**
     * 验证手机号验证码
     * @param MobileVerification
     * @return
     */
    @PostMapping(value = "/verification")
    @ResponseBody
    public Object verification(String MobileVerification){
        //取出redis的手机验证码
        redisService.put("888888","1");
        String msg="";
         msg= (String) redisService.getKey(MobileVerification);
        return Constant.RESULT_SUCCESS.equals(msg) ? AjaxResult.success() :AjaxResult.error(msg);
    }
    /**
     * 生成count位随机数
     * @param count
     */
    private String getRandomCode(int count) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; i++) {
            int code = (int)(Math.random()*10);
            sb.append(code);
        }
        return sb.toString();

    }
}
