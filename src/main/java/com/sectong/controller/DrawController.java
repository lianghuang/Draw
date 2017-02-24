package com.sectong.controller;

import com.sectong.domain.Room;
import com.sectong.repository.PaintHistoryRepository;
import com.sectong.service.RoomService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;


/**
 * Created by huangliangliang on 2/3/17.
 */

@Controller
@Api(description = "画图相关API")
public class DrawController {
    private static final Logger logger = LoggerFactory.getLogger(DrawController.class);

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private PaintHistoryRepository paintHistoryRepository;

    @Autowired
    private RoomService roomService;

    @MessageMapping("/room.{roomId}/draw/paint")
    public String drawpts(@DestinationVariable("roomId")String roomId,String message) throws Exception {
        logger.info("message:{}",message);
        paintHistoryRepository.addHistory(roomId,message);
        //消息转发
        simpMessagingTemplate.convertAndSend("/topic/room."+roomId+"/draw/pts",message);
        return message;
    }


    @MessageMapping("/room.{roomId}/draw/paint/history")
    public List<String> drawHistory(@DestinationVariable("roomId")String roomId) throws Exception {
        List<String> historys=paintHistoryRepository.getHistory(roomId);
        //消息转发
        simpMessagingTemplate.convertAndSend("/topic/room."+roomId+"/draw/paint/history",historys);
        return historys;
    }


    @MessageMapping("/room.{roomId}/draw/answer")
    public void answer(@DestinationVariable("roomId")String roomId,String answer) throws Exception {
        Room room=roomService.findRoomById(roomId);
        if(room.getCurrentQuestion().getQuestion().equals(answer)){
            //回答正确
            simpMessagingTemplate.convertAndSend("/topic/room."+roomId+"/answer/correct",answer);
        }else{
            //回答错误
            simpMessagingTemplate.convertAndSend("/topic/room."+roomId+"/answer/incorrect",answer);
        }
    }
}
