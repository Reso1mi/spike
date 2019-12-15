package top.imlgw.spike.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.imlgw.spike.redis.RedisService;

/**
 * @author imlgw.top
 * @date 2019/12/15 12:06
 */
@Service
public class MQSender {

    private static Logger log=LoggerFactory.getLogger(MQReceiver.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

/*
    public void send(Object object){
        String msg= RedisService.beanToString(object);
        log.info("send message:"+msg);
        amqpTemplate.convertAndSend(MQConfig.QUEUE,msg);
    }

    public void sendTopic(Object object){
        String msg= RedisService.beanToString(object);
        log.info("send topic message:"+msg);
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,MQConfig.ROUTING_KEY1,msg+"1");
        amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key321421",msg+"2");
    }

    public void sendFanout(Object object){
        String msg= RedisService.beanToString(object);
        log.info("send topic message:"+msg);
        amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg);
    }

    public void sendHeader(Object object){
        String msg= RedisService.beanToString(object);
        log.info("send header message:"+msg);
        MessageProperties messageProperties=new MessageProperties();
        messageProperties.setHeader("header1","value1");
        messageProperties.setHeader("header2","value2");
        Message message =new Message(msg.getBytes(),messageProperties);
        amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE,"",message);
    }
*/

    public void sendSpikeMessage(SpikeMessage spikeMessage) {
        String msg = RedisService.beanToString(spikeMessage);
        log.info("send message: "+msg);
        amqpTemplate.convertAndSend(MQConfig.SPIKE_QUEUE,msg);
    }
}
