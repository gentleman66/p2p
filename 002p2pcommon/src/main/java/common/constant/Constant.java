package common.constant;

/**
 * 通用常量信息
 */
public class Constant {
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";
    /**
     * 产品的年化历史平均收益率
     */
    public static final String HISTORY_ANNUAL_RATE="queryHistoryAnnualRate";

    //获取累计成交金额
    public static final String All_Bid_Money="queryAllBidMoney";
    ////获取平台注册人数
    public static final String All_User_Count="queryAllUserCount";

    //用户投标信息集合（根据产品id）
    public static  final String  BID_INFO_LIST="bidInfoList";
    //产品类型为新手宝
    public static final Integer PRODUCT_TYPE_X = 0;
    //产品类型优选产品
    public static final Integer PRODUCT_TYPE_U = 1;
    //产品类型为散标
    public static final Integer PRODUCT_TYPE_S = 2;
    /**
     * 图像验证码
     */
    public static final String CAPTCHA = "captcha";

    //系统繁忙
    public static final String ERROR_MESSAGE="系统繁忙请稍后再试";
    //操作成功
    public static final String RESULT_SUCCESS="操作成功";
    //操作失败
    public static final String RESULT_FALSE="操作失败";
    //投资排行榜
    public static final String INVEST_TOP="investTop";
    /**
     * 用redis生成唯一值
     */
    public static final Object ONLY_NUMBER ="onlyNumber" ;
}
