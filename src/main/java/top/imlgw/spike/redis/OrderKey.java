package top.imlgw.spike.redis;

/**
 * @author imlgw.top
 * @date 2019/12/15 15:50
 */
public class OrderKey extends BasePrefix{
    public OrderKey( String prefix) {
        super(prefix);
    }

    public static OrderKey getSpikeOrderByUidGid = new OrderKey("od");
}
