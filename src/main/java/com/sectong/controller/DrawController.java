package com.sectong.controller;

import com.sectong.domain.DrawEntity;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by huangliangliang on 2/3/17.
 */

@Controller
@Api(description = "你画我猜相关")
public class DrawController {
    private static final Logger logger = LoggerFactory.getLogger(DrawController.class);


    @MessageMapping("/draw/paint")
    @SendTo("/topic/draw/pts")
    public String drawpts(String message) throws Exception {
        logger.info("message:{}",message);
        return message;
    }

}
