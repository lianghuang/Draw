package com.sectong.controller;

import com.sectong.domain.Question;
import com.sectong.domain.Room;
import com.sectong.domain.User;
import com.sectong.message.Message;
import com.sectong.repository.PaintHistoryRepository;
import com.sectong.service.QuestionService;
import com.sectong.service.RoomService;
import com.sectong.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

/**
 * Created by huangliangliang on 2/15/17.
 */
@RestController
@RequestMapping(value = "/api/v1", name = "API")
@Api(description = "API")
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private PaintHistoryRepository paintHistoryRepository;

    @GetMapping(value = "room/into")
    @ApiOperation(value = "", notes = "用户进入房间")
    public Message getRandomRoom(@RequestParam(name = "username") String username) {
        User user=userService.getUserByUsername(username);
        if(user==null){
            return Message.errorMsg("未找到该用户");
        }
        Room room= roomService.getRandomRoom(user);
        //房间内的其他玩家收到该用户进入房间的推送消息
        simpMessagingTemplate.convertAndSend("/topic/room."+room.getRoomId()+"/in", user);
        return Message.successMsg(room);
    }

    @GetMapping(value = "room/create")
    @ApiOperation(value = "", notes = "创建私有房间")
    public Message createRoom(@RequestParam(name = "username") String username) {
        User user=userService.getUserByUsername(username);
        Room room= roomService.createRoom(user);
        return Message.successMsg(room);
    }

    @GetMapping(value = "room/into/{roomId}")
    @ApiOperation(value = "", notes = "进入私有房间")
    public Message intoRoom(@RequestParam(name = "username") String username, @PathVariable(name="roomId") String roomId) {
        User user=userService.getUserByUsername(username);
        Room room= roomService.intoRoom(user,roomId);
        if(room==null){
            return Message.errorMsg("未找到"+roomId+"的房间");
        }
        //房间内的其他玩家收到该用户进入房间的推送消息
        simpMessagingTemplate.convertAndSend("/topic/room."+room.getRoomId()+"/in", user);
        return Message.successMsg(room);
    }


    @GetMapping(value = "room/{roomId}/question/list")
    @ApiOperation(value = "", notes = "获得问题的备选列表")
    public Message questionList(@PathVariable(name="roomId") String roomId,@RequestParam(name = "size") int size) {
        Room room= roomService.findRoomById(roomId);
        if(room==null){
            return Message.errorMsg("未找到"+roomId+"的房间");
        }
        List<Question> quesitons=questionService.getRandomQuesitons(roomId,size);
        if(CollectionUtils.isEmpty(quesitons)){
            return Message.errorMsg("问题列表都被用光了，没有新的问题了");
        }
        simpMessagingTemplate.convertAndSend("/topic/room."+room.getRoomId()+"/questions","正在选择");
        return Message.successMsg(quesitons);
    }

    @GetMapping(value = "room/{roomId}/question/ok")
    @ApiOperation(value = "", notes = "选择完当前问题")
    public Message questionOk(@PathVariable(name="roomId") String roomId,@RequestParam(name = "questionId") String questionId) {
        Room room= roomService.findRoomById(roomId);
        if(room==null){
            return Message.errorMsg("未找到"+roomId+"的房间");
        }
        Question quesiton=questionService.getQuestionById(questionId);
        room.setCurrentQuestion(quesiton);
        room.getQuestions().add(quesiton);
        roomService.save(room);
        simpMessagingTemplate.convertAndSend("/topic/room."+room.getRoomId()+"/question/ok",quesiton);
        return Message.successMsg(quesiton);
    }

    @GetMapping(value = "room/leave")
    @ApiOperation(value = "", notes = "用户离开房间")
    public  Message leaveRoom(@RequestParam(name = "username") String username) {
        logger.info("用户离开房间：username:{}",username);
        User user=userService.getUserByUsername(username);
        String roomId=roomService.findRoomIdByUser(username);
        if(StringUtils.isEmpty(roomId)){
            return Message.errorMsg("username:"+username+"的用户未进入任何房间");
        }
        logger.info("用户离开房间：username:{},roomId:{}",username,roomId);
        Room room= roomService.findRoomById(roomId);
        roomService.removeUser(user,room);
        //房间内的其他玩家收到该用户退出房间的推送消息
        simpMessagingTemplate.convertAndSend("/topic/room."+room.getRoomId()+"/out", user);
        return Message.successMsg(true);
    }
    @GetMapping(value = "room/{roomId}/ready")
    @ApiOperation(value = "", notes = "用户在房间内点击准备")
    public Message userReady(@PathVariable("roomId")String roomId,@RequestParam(name = "username") String username) throws Exception {
        logger.info("userReady:username:{},roomId:{}",username,roomId);
        if(!validateRoomAndUserName(roomId,username)){
            return Message.errorMsg("房间和您当前用户校验错误");
        }
        //存储用户状态
        User user=userService.getUserByUsername(username);
        user.setStatus(User.UserStatus.Ready);
        userService.saveUser(user);
        Room room= roomService.findRoomById(roomId);
        simpMessagingTemplate.convertAndSend("/topic/room."+room.getRoomId()+"/ready", user);
        if(room.getNowUserNum()>=Room.minGameUserNum){
            boolean notifyFlag=true;
            for(User roomuser:room.getAddedUserList()){
                if(room.getRoomOwnerName().equals(roomuser.getUsername())){
                    continue;
                }
                if(!roomuser.getStatus().equals(User.UserStatus.Ready)){
                    notifyFlag=false;
                }
            }
            if(notifyFlag){
                //通知房间内倒计时
                simpMessagingTemplate.convertAndSend("/topic/room."+room.getRoomId()+"/owner.countdown", Room.timeToStart);
            }
        }
        return Message.successMsg(room);
    }

    @GetMapping(value = "room/{roomId}/readycancel")
    @ApiOperation(value = "", notes = "用户在房间内点击取消准备")
    public Message userReadyCancel(@PathVariable("roomId")String roomId,@RequestParam(name = "username") String username) throws Exception {
        logger.info("userReadyCancel:username:{},roomId:{}",username,roomId);
        if(!validateRoomAndUserName(roomId,username)){
            return Message.errorMsg("房间和您当前用户校验错误");
        }
        //存储用户状态
        User user=userService.getUserByUsername(username);
        user.setStatus(User.UserStatus.Empty);
        userService.saveUser(user);
        Room room= roomService.findRoomById(roomId);
        simpMessagingTemplate.convertAndSend("/topic/room."+room.getRoomId()+"/readycancel", user);
        simpMessagingTemplate.convertAndSend("/topic/room."+room.getRoomId()+"/owner.countdown.cancel",Room.timeToStart);
        return Message.successMsg(room);
    }

    @GetMapping(value = "room/{roomId}/start")
    @ApiOperation(value = "", notes = "房主用户在房间内点击游戏开始")
    public Message gameStart(@PathVariable("roomId")String roomId,@RequestParam(name = "username") String username) throws Exception{
        logger.info("gameStart:ownerName:{},roomId:{}",username,roomId);
        if(!validateRoomAndUserName(roomId,username)){
            return Message.errorMsg("房间和您当前用户校验错误");
        }
        Room room=roomService.roomBeginGame(roomId);
        simpMessagingTemplate.convertAndSend("/topic/room."+roomId+"/start.game",room);
        return Message.successMsg(room);
    }

    @MessageMapping("/room.{roomId}/talk")
    public void gameTalk(@DestinationVariable("roomId")String roomId,String message) throws Exception{
        simpMessagingTemplate.convertAndSend("/topic/room."+roomId+"/game.talk",message);
    }



    private boolean validateRoomAndUserName(String comeRoomId,String username){
        String roomId=roomService.findRoomIdByUser(username);
        if(StringUtils.isEmpty(roomId)){
            logger.error("userName:{}的玩家未加入一个房间",username);
            return false;
        }
        if(!roomId.equals(comeRoomId)){
            logger.error("传递的roomId{}和用户{}所属的不一致{}",comeRoomId,username,roomId);
            return false;
        }
        return true;
    }

}
