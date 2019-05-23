package top.imlgw.spike.utils;

import java.util.UUID;

/**
 * @author imlgw.top
 * @date 2019/5/15 14:38
 */
public class UUIDUtil {

    public static String getUuid(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
