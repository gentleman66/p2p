package common.constant;

/**
 * ClassName:UserConstants
 * Package:common.constant
 * Description: 描述信息 用户常量信息
 *
 * @date:2020/12/20 21:14
 * @author:动力节点
 */
public class UserConstants {
    /**
     * 身份证号码是否唯一的返回结果
     */
    public final static String USER_IDCARD_UNIQUE = "0";
    public final static String USER_IDCARD_NOT_UNIQUE = "1";
    /** 手机号码是否唯一的返回结果 */
    public final static String USER_PHONE_UNIQUE = "0";
    public final static String USER_PHONE_NOT_UNIQUE = "1";
    public final static String USER_IDCARD_FALSE = "保存失败，身份证号已存在";
    public final static String USER_PHONE_FALSE = "保存失败，手机号已存在";
    /**
     * 注册失败
     */
    public final static String USER_REGISTER_FALSE = "注册失败";

    //
    public final static String USER_CAPTCHA_FALSE = "图片验证码不一致";
    //用户
    public final static String  SESSION_USER = "user";
    /**
     * 密码长度限制
     */
    public static final int PASSWORD_MIN_LENGTH = 6;
    public static final int PASSWORD_MAX_LENGTH = 16;
    //密码不正确
    public static  final String LOGIN_PASSWORD_FALSE="密码不正确";
    //真实姓名报错
    public static  final String REALNAME_FALSE="真实姓名只支持中文";
    //身份证卡号报错
    public static final String IDCARD_FALSE="身份证卡号格式不对哦";
    //手机号格式不对
    public static final String PHONE_FORMAT_FALSE="手机号格式不对";
    //验证码不对
    public static  final String MOBILE_VERIFICATION_FALSE="验证码不对";
    //用户登录失败
    public static final String user_Login_false="该用户不存在请注册";


}
