package top.imlgw.spike.redis;

/**
 * @author imlgw.top
 * @date 2019/12/16 17:58
 */
public class SpikeKey extends  BasePrefix{


    public static SpikeKey getSecKillPath = new SpikeKey(60,"secKillPath");

    public static SpikeKey getSecKillVerifyCode = new SpikeKey(300,"secKillVerifyCode");

    public SpikeKey(int expire, String secKillPath) {
        super(expire,secKillPath);
    }

    public SpikeKey(String prefix){
        super(prefix);
    }
}
