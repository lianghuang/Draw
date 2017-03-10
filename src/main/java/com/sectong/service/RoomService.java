package com.sectong.service;

import com.sectong.domain.Room;
import com.sectong.domain.User;
import com.sectong.repository.RoomRepository;
import com.sectong.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

/**
 * Created by huangliangliang on 2/16/17.
 */
@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);


    public void save(Room room){
        roomRepository.save(room);
    }


    public Room removeUser(User user,Room room){
        room.removeUser(user);
        if(room.getNowUserNum()<=0){
            roomRepository.delete(room);
        }else{
            roomRepository.save(room);
        }
        return room;
    }

    public void removeUser(String username){
        String roomId=findRoomIdByUser(username);
        if(StringUtils.isNotEmpty(roomId)){
            removeUser(userRepository.findByUsername(username),findRoomById(roomId));
        }
    }

    public void deleteRoom(String roomId){
        roomRepository.delete(roomId);
    }

    @Transactional
    public Room findRoomById(String roomId){
        return roomRepository.findOne(roomId);
    }

    public Room roomBeginGame(String roomId){
        Room room= findRoomById(roomId);
        room.setStage(Room.Stage.Gaming);
        List<User> users=room.getAddedUserList();
        for(User user:users){
            user.setStatus(User.UserStatus.Gaming);
        }
        roomRepository.save(room);
        return room;
    }

    public String findRoomIdByUser(String username){
        return roomRepository.findRoomIdByUserName(username);
    }


    public Room getRandomRoom(User user){
        String roomId=findRoomIdByUser(user.getUsername());
        if(StringUtils.isNotEmpty(roomId)){
//            removeUser(user,findRoomById(roomId));
            return findRoomById(roomId);
        }
        Pageable page=new PageRequest(0,1);
        Room room= roomRepository.findMostSuitRoom(Room.maxUserNum);
        if(room==null){
            Room room1 =generateEmptyRoom();
            room1.addUser(user);
            roomRepository.save(room1);
            return room1;
        }else{
            room.addUser(user);
            try{
                roomRepository.save(room);
                return room;
            }catch(Exception e){
                logger.error("JPA Exception:",e);
                return getRandomRoom(user);
            }
        }
    }
    public Room createRoom(User user){
        String roomId=findRoomIdByUser(user.getUsername());
        if(StringUtils.isNotEmpty(roomId)){
            removeUser(user,findRoomById(roomId));
        }
        Room room =generateEmptyRoom();
        room.addUser(user);
        room.setType(Room.RoomType.Specified);
        roomRepository.save(room);
        return room;
    }

    public Room intoRoom(User user,String roomId){
        Room room=roomRepository.findOne(roomId);
        if(room==null){
            return null;
        }
        if(room.getType().equals(Room.RoomType.Random)){
            return null;//不允许用户指定房间号进入随机房间
        }
        String roomIdOrig=findRoomIdByUser(user.getUsername());
        if(StringUtils.isNotEmpty(roomIdOrig)){
            removeUser(user,findRoomById(roomIdOrig));
        }
        room.addUser(user);
        roomRepository.save(room);
        return room;
    }


    public Room generateEmptyRoom(){
        String roomId=getRoomRandomNumber();
        while(roomRepository.findOne(roomId)!=null){
            roomId=getRoomRandomNumber();
        }
        Room room =new Room();
        room.setRoomId(roomId);
        return room;
    }

    private String getRoomRandomNumber(){
        Random rnd = new Random();
        Integer n = 100000 + rnd.nextInt(900000);
        return n.toString();
    }
}
