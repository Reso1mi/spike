package top.imlgw.spike.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imlgw.spike.entity.SpikeOrder;
import top.imlgw.spike.entity.SpikeUser;
import top.imlgw.spike.redis.RedisService;
import top.imlgw.spike.service.GoodsService;
import top.imlgw.spike.service.OrderService;
import top.imlgw.spike.service.SpikeService;
import top.imlgw.spike.vo.GoodsVo;


/**
 * @author imlgw.top
 * @date 2019/12/15 12:11
 */
@Service
public class MQReceiver {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SpikeService spikeService;

    @Autowired
    private OrderService orderService;

    private static Logger log=LoggerFactory.getLogger(MQReceiver.class);

/*    @RabbitListener(queues=MQConfig.QUEUE)
    public void  receiver(String msg){
        log.info("receiver:"+msg);
    }

    @RabbitListener(queues=MQConfig.TOPIC_QUEUE1)
    public void  receiverTopic1(String msg){
        log.info("receiver topic queue1:"+msg);
    }

    @RabbitListener(queues=MQConfig.TOPIC_QUEUE2)
    public void  receiverTopic2(String msg){
        log.info("receiver topic queue2:"+msg);
    }

    @RabbitListener(queues=MQConfig.HEADER_QUEUE)
    public void  receiverHeader(byte[] msg){
        log.info("receiver headers queue:"+new String(msg));
    }*/

    @RabbitListener(queues=MQConfig.SPIKE_QUEUE)
    public void  receiver(String msg){
        log.info("receiver:"+msg);
        SpikeMessage spikeMessage = RedisService.stringToBean(msg, SpikeMessage.class);
        SpikeUser user = spikeMessage.getUser();
        long goodsId = spikeMessage.getGoodsId();
        GoodsVo goodsVo= goodsService.getGoodsVoByGoodsId(goodsId);
        int stock=goodsVo.getStockCount(); //这里拿的是秒杀商品里面的库存,不是商品里面的库存
        if(stock<0){
            return;
        }
        //判断是是否重复秒杀
        SpikeOrder spikeOrder = orderService.getOrderByUserIdAndGoodsId(user.getId(), goodsId);
        if (spikeOrder!=null){
            return;
        }
        spikeService.doSpike(user.getId(),goodsVo);
    }
}
