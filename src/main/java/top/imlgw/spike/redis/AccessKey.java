package top.imlgw.spike.redis;

/**
 * @author imlgw.top
 * @date 2019/12/21 16:32
 */
public class AccessKey extends BasePrefix{

    public AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public AccessKey(String prefix) {
        super(prefix);
    }

    //5s 5次
    public static AccessKey access = new AccessKey(5,"access");

    /**
     * 上面的access的expireSeconds是写死的，这里是自定义设置的
     * @param expireSeconds
     * @return
     */
    public static AccessKey withExpire(int expireSeconds){
        return new AccessKey(expireSeconds,"access");
    }
}
