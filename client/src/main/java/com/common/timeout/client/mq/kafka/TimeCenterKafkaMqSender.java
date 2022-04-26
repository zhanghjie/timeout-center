package com.common.timeout.client.mq.kafka;

import com.alibaba.fastjson.JSON;
import com.common.timeout.client.db.model.TaskTypeMangerDTO;
import com.common.timeout.client.db.model.TimeoutTaskDTO;
import com.common.timeout.client.mq.TimeCenterMqSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Sender
 * 功能描述：发送MQ kafka 实现
 *
 * @author zhanghaojie
 * @date 2022/4/25 15:25
 */
@Service
@Slf4j
public class TimeCenterKafkaMqSender implements TimeCenterMqSendService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    //构造器方式注入  kafkaTemplate
    public TimeCenterKafkaMqSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 功能描述: 发送日志
     *
     * @param msg
     * @return
     * @author zhanghaojie
     * @date 2022/4/25 15:28
     */
    @Override
    public void sendMessage(TimeoutTaskDTO msg, TaskTypeMangerDTO taskTypeMangerDTO) {
        //回调函数在Producer收到ack时异步调用
        kafkaTemplate.send(taskTypeMangerDTO.getMqTopic(), JSON.toJSONString(msg))
                .addCallback(new TimeoutMqCallback(msg.getBizType(), msg.getBizId()));
    }

}
