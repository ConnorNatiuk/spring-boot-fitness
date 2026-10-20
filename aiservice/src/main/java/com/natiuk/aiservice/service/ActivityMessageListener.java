package com.natiuk.aiservice.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.natiuk.aiservice.model.Activity;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor    
public class ActivityMessageListener {

    @RabbitListener(queues = "activity.queue") 
    public void processActivity(Activity activity) {
        log.info("Received activity for processing: {}", activity.getId());
    }
}
