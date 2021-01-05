package common.util;

import java.util.UUID;

/**
 * ClassName:UUIDUtils
 * Package:common.util
 * Description: 描述信息
 *
 * @date:2020/12/21 20:00
 * @author:动力节点
 */
public class UUIDUtils {
    public static String getUUID(){

        return UUID.randomUUID().toString().replace("-","");

    }
}
