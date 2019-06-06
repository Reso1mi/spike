package top.imlgw.spike.redis;

/**
 * 测试代码
 *
 *
 * @author imlgw.top
 * @date 2019/5/12 12:53
 */
public class UserKey extends BasePrefix {

    private UserKey(String prefix) {
        super(prefix);
    }

    private UserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static UserKey userId=new UserKey("id");

    public static UserKey userName=new UserKey("name");
}
