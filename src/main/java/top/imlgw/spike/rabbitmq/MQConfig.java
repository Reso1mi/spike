package top.imlgw.spike.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;

/**
 * @author imlgw.top
 * @date 2019/12/15 12:04
 */
@Configuration
public class MQConfig {

/*    public static final String QUEUE="queue";

    public static final String TOPIC_QUEUE1="topic_queue1";

    public static final String TOPIC_QUEUE2="topic_queue2";

    public static final String HEADER_QUEUE="header_queue";

    public static final String TOPIC_EXCHANGE="topic_exchange";

    public static final String FANOUT_EXCHANGE="fanout_exchange";

    public static final String HEADERS_EXCHANGE="headers_exchange";

    public static final String ROUTING_KEY1="topic.key1";

    public static final String ROUTING_KEY2="topic.#";

    *//*
    * direct模式不需要交换机
    * *//*
    @Bean
    public Queue queue(){
        return  new Queue(QUEUE,true);
    }

    @Bean
    public Queue headQueue(){
        return new Queue(HEADER_QUEUE,true);
    }

    @Bean
    public Queue topicQueue1(){
        return  new Queue(TOPIC_QUEUE1,true);
    }

    @Bean
    public Queue topicQueue2(){
        //name,是否持久化
        return  new Queue(TOPIC_QUEUE2,true);
    }


    *//*
    * topic模式
    * *//*
    @Bean
    public TopicExchange exchange(){
        return  new TopicExchange(TOPIC_EXCHANGE);
    }

    @Bean
    public Binding topicBinding1(){
        return BindingBuilder.bind(topicQueue1()).to(exchange()).with(ROUTING_KEY1);
    }

    @Bean
    public Binding topicBinding2(){
        return BindingBuilder.bind(topicQueue2()).to(exchange()).with(ROUTING_KEY2);
    }


    *//*
    * Fanout模式
    * *//*

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUT_EXCHANGE);
    }

    @Bean
    public Binding FanoutBind1(){
        return BindingBuilder.bind(topicQueue1()).to(fanoutExchange());
    }

    @Bean
    public Binding FanoutBind2(){
        return BindingBuilder.bind(topicQueue2()).to(fanoutExchange());
    }

    @Bean
    public HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERS_EXCHANGE);
    }

    @Bean
    public Binding headerBind(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("header1","value1");
        map.put("header2","value2");
        return BindingBuilder.bind(headQueue()).to(headersExchange()).whereAll(map).match();
    }*/

    public static final String SPIKE_QUEUE="spike.queue";

    @Bean
    public Queue queue(){
        return  new Queue(SPIKE_QUEUE,true);
    }

}
