package top.imlgw.spike.redis;

/**
 * @author imlgw.top
 * @date 2019/5/12 12:47
 */
public abstract class BasePrefix implements KeyPrefix{

    //s为单位
    private int expireSeconds;

    private String prefix;

    public BasePrefix(String prefix) {
        this(0,prefix);
    }

    public BasePrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    @Override
    public int expireSecond() { //0代表永远不过期
        return expireSeconds;
    }

    @Override
    public String getPrefix() {
        String className=getClass().getSimpleName();
        return className+"-"+prefix;
    }
}
