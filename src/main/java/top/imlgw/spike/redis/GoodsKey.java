package top.imlgw.spike.redis;

/**
 * @author imlgw.top
 * @date 2019/6/7 8:39
 */
public class GoodsKey extends BasePrefix{

    //60s的过期时间
    public static final int GOODS_EXPIRE=60;

    public GoodsKey(String prefix) {
        super(prefix);
    }

    public GoodsKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKey glsPrefix=new GoodsKey(GOODS_EXPIRE,"gsl");

    public static GoodsKey gdsPrefix=new GoodsKey(GOODS_EXPIRE,"gds");
}
