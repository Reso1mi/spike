package top.imlgw.spike.redis;

/**
 * @author imlgw.top
 * @date 2019/5/15 14:42
 */
public class SpikeUserKey extends BasePrefix {

    //token有效期（ms）
    private static final int TOKEN_EXPIRE =3600*24*2;

    public SpikeUserKey(String prefix) {
        super(prefix);
    }

    public SpikeUserKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static SpikeUserKey tkPrefix=new SpikeUserKey(TOKEN_EXPIRE,"tk");

}
