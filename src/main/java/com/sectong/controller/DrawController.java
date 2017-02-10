package com.sectong.controller;

import com.sectong.domain.DrawEntity;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by huangliangliang on 2/3/17.
 */

@Controller
@Api(description = "你画我猜相关")
public class DrawController {
    private static final Logger logger = LoggerFactory.getLogger(DrawController.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/draw/paint")
    @SendTo("/topic/draw/pts")
    public String drawpts(String message) throws Exception {
        logger.info("message:{}",message);
        return message;
    }

    @MessageMapping("/notify.private.{username}")
    public void filterPrivateMessage(@DestinationVariable("username") String username) {
        String message="这条信息代表系统单独发送给你的推送";
        simpMessagingTemplate.convertAndSend("/user/" + username + "/exchange/amq.direct/notify.message", message);
    }


    @MessageExceptionHandler
    @SendToUser(value = "/exchange/amq.direct/errors", broadcast = false)
    public String handleProfanity(Exception e) {
        return e.getMessage();
    }

}
