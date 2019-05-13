package top.imlgw.spike.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author imlgw.top
 * @date 2019/5/12 17:46
 */
public class ValidatorUtil {

    //匹配电话号码1开头10个
    private static  final Pattern mobile_pattern =Pattern.compile("1\\d{10}");

    public static boolean isMobile(String mobile){
        if(StringUtils.isEmpty(mobile)){
            return  false;
        }
        Matcher matcher=mobile_pattern.matcher(mobile);
        return matcher.matches();
    }
}
