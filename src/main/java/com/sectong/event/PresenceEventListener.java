package com.sectong.event;

import com.sectong.constants.WebConstant;
import com.sectong.service.RoomService;
import com.sectong.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

/**
 * Created by huangliangliang on 2/10/17.
 */
public class PresenceEventListener {
    private static final Logger logger = LoggerFactory.getLogger(PresenceEventListener.class);

    private ParticipantRepository participantRepository;

    private SimpMessagingTemplate messagingTemplate;

    private String loginDestination;

    private String logoutDestination;

//    private RoomService roomService;

    public PresenceEventListener(SimpMessagingTemplate messagingTemplate, ParticipantRepository participantRepository
                                 ) {
        this.messagingTemplate = messagingTemplate;
        this.participantRepository = participantRepository;
//        this.roomService=roomService;
    }

    @EventListener
    private void handleSessionConnected(SessionConnectEvent event) {
///        logger.info("event:{}", JsonUtils.toString(event));
        SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
//        logger.info("===============headers:{}",headers.toString());
        if(headers.getHeader(SimpMessageHeaderAccessor.SESSION_ATTRIBUTES)!=null){
            @SuppressWarnings("unchecked")
            Map<String, Object> simpSessionAttributes= (Map<String, Object>)headers.getHeader(SimpMessageHeaderAccessor.SESSION_ATTRIBUTES);
            String username=(String) simpSessionAttributes.get(WebConstant.USERNAME);
            logger.info("===============username:{},connected",username);
            LoginEvent loginEvent = new LoginEvent(username);
//            messagingTemplate.convertAndSend(loginDestination, loginEvent);
            // We store the session as we need to be idempotent in the disconnect event processing
            participantRepository.removePreviousSession(username);
            participantRepository.add(headers.getSessionId(), loginEvent);

        }
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) {
        LoginEvent loginEvent=participantRepository.getParticipant(event.getSessionId());
        if(loginEvent!=null){
            logger.info("===============username:{},disconnected",loginEvent.getUsername());
//          messagingTemplate.convertAndSend(logoutDestination, new LogoutEvent(loginEvent.getUsername()));
            loginEvent.logout();
//            participantRepository.removeParticipant(event.getSessionId());
//            roomService.removeUser(loginEvent.getUsername());
        }
    }

    public void setLoginDestination(String loginDestination) {
        this.loginDestination = loginDestination;
    }

    public void setLogoutDestination(String logoutDestination) {
        this.logoutDestination = logoutDestination;
    }
}
