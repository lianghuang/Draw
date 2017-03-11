package com.sectong.event;

import com.sectong.service.RoomService;
import com.sectong.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by huangliangliang on 3/11/17.
 */
@Component
public class UserCleanTask {
    private static final Logger logger = LoggerFactory.getLogger(UserCleanTask.class);

//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private ParticipantRepository participantRepository;

    @Autowired
    private RoomService roomService;

    private final static int timeToClean=8;
    @Scheduled(fixedRate = 10000)
    public void cleanUser() {
        logger.info("start to clean dead users:{}", JsonUtils.toString(participantRepository.getActiveSessions()));
        Set<String> cleanList=new HashSet<>();
        for(Map.Entry<String,LoginEvent> session:participantRepository.getActiveSessions().entrySet()){
            if(session.getValue().getTimeBetweenLogOutAndNow()>8){
                cleanList.add(session.getKey());
                roomService.removeUser(session.getValue().getUsername());
            }
        }
        for(String key:cleanList){
            participantRepository.removeParticipant(key);
        }
    }
}
