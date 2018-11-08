package com.imooc.miaosha.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.imooc.miaosha.redis.RedisService;

@Service
public class MQSender {

	private static Logger log = LoggerFactory.getLogger(MQSender.class);
	
	@Autowired
	AmqpTemplate amqpTemplate ;

	@Autowired
    RabbitTemplate rabbitTemplate;

	//发送成功之后回调函数
	final RabbitTemplate.ConfirmCallback confirmCallback  = new RabbitTemplate.ConfirmCallback() {
		@Override
		public void confirm(CorrelationData correlationData, boolean ack, String s) {
            if(ack){
            	//如果confirm返回成功，则进行更新表数据，更新状态为发送成功1；
			}else{
            	//如果失败，则进行重试或者补偿等手段；目前用一个定时任务定时轮训发送状态为0的，重试3次，如果还不成功就改为2，发送失败，人工干预了。
			}
		}
	};
	
	public void sendMiaoshaMessage(MiaoshaMessage mm) {
	    //
        rabbitTemplate.setConfirmCallback(confirmCallback);
		String msg = RedisService.beanToString(mm);
		log.info("send message:"+msg);
        rabbitTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
	}
	
	public void send(Object message) {
		String msg = RedisService.beanToString(message);
		log.info("send message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.QUEUE, msg);
	}
//	
	public void sendTopic(Object message) {
		String msg = RedisService.beanToString(message);
		log.info("send topic message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", msg+"1");
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg+"2");
	}
//	
	public void sendFanout(Object message) {
		String msg = RedisService.beanToString(message);
		log.info("send fanout message:"+msg);
		amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", msg);
	}
//	
//	public void sendHeader(Object message) {
//		String msg = RedisService.beanToString(message);
//		log.info("send fanout message:"+msg);
//		MessageProperties properties = new MessageProperties();
//		properties.setHeader("header1", "value1");
//		properties.setHeader("header2", "value2");
//		Message obj = new Message(msg.getBytes(), properties);
//		amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "", obj);
//	}

	
	
}
