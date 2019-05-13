package top.imlgw.spike.redis;

/**
 * @author imlgw.top
 * @date 2019/5/12 0:14
 */
public interface KeyPrefix {

    int expireSecond();

    String getPrefix();
}
