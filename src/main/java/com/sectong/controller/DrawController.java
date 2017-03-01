package com.sectong.controller;

import com.sectong.domain.Room;
import com.sectong.domain.User;
import com.sectong.repository.PaintHistoryRepository;
import com.sectong.service.RoomService;
import com.sectong.service.UserService;
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

    @Autowired
    private UserService userService;

    @MessageMapping("/room.{roomId}/draw/paint")
    public String drawpts(@DestinationVariable("roomId")String roomId,String message) throws Exception {
        logger.debug("message:{}",message);
        paintHistoryRepository.addHistory(roomId,message);
        //消息转发
        simpMessagingTemplate.convertAndSend("/topic/room."+roomId+"/draw/pts",message);
        return message;
    }


    @MessageMapping("/room.{roomId}/{username}/draw/paint/history")
    public List<String> drawHistory(@DestinationVariable("roomId")String roomId,
                                    @DestinationVariable("username")String username) throws Exception {
        List<String> historys=paintHistoryRepository.getHistory(roomId);
        //消息转发
        simpMessagingTemplate.convertAndSend("/queue/room."+roomId+"/"+username+"/draw/paint/history",historys);
        return historys;
    }


    @MessageMapping("/room.{roomId}/{username}/draw/answer")
    public void answer(@DestinationVariable("roomId")String roomId,
                       @DestinationVariable("username")String username,
                       String answer) throws Exception {
        Room room=roomService.findRoomById(roomId);
        if(room.getCurrentQuestion().getQuestion().equals(answer)){
            //回答正确
            simpMessagingTemplate.convertAndSend("/topic/room."+roomId+"/answer/correct",answer);
            //给用户计分
            User user=userService.getUserByUsername(username);
            user.setCurrentScore(user.getCurrentScore()+room.getCurrentQuestion().getScore());
            userService.saveUser(user);
            scorePush(roomId);
            //如果为当前房间的最后一个问题，推送游戏结束指令
            if(room.getQuestions().size()>=room.getQuestionNum()){
                gameEnded(roomId);
            }
        }else{
            //回答错误
            simpMessagingTemplate.convertAndSend("/topic/room."+roomId+"/answer/incorrect",answer);
        }
    }

    private void gameEnded(String roomId){
        paintHistoryRepository.destroyRoomHistry(roomId);
        Room room=roomService.findRoomById(roomId);
        simpMessagingTemplate.convertAndSend("/topic/room."+roomId+"/game/end",room);
        //后台数据清理
        for(User roomuser:room.getAddedUserList()){
            roomuser.setCurrentScore(0);
            roomuser.setStatus(User.UserStatus.Empty);
        }
        room.setQuestions(null);
        room.setCurrentQuestion(null);
        room.setStage(Room.Stage.Ready);
        roomService.save(room);
    }

    private void scorePush(String roomId){
        Room room=roomService.findRoomById(roomId);
        simpMessagingTemplate.convertAndSend("/topic/room."+roomId+"/scores",room);
    }
}
