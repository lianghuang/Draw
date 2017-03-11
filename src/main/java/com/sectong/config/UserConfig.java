package com.sectong.config;

import com.sectong.event.ParticipantRepository;
import com.sectong.event.PresenceEventListener;
import com.sectong.service.RoomService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.messaging.simp.SimpMessagingTemplate;


/**
 * Created by huangliangliang on 2/10/17.
 */
@Configuration
public class UserConfig {

    public static class Destinations {
        private Destinations() {
        }

        private static final String LOGIN = "/topic/user.login";
        private static final String LOGOUT = "/topic/user.logout";
    }

    private static final int MAX_PROFANITY_LEVEL = 5;

    @Bean
    @Description("Tracks user presence (join / leave) and broacasts it to all connected users")
    public PresenceEventListener presenceEventListener(SimpMessagingTemplate messagingTemplate) {
        PresenceEventListener presence = new PresenceEventListener(messagingTemplate, participantRepository());
        presence.setLoginDestination(Destinations.LOGIN);
        presence.setLogoutDestination(Destinations.LOGOUT);
        return presence;
    }

    @Bean
    @Description("Keeps connected users")
    public ParticipantRepository participantRepository() {
        return new ParticipantRepository();
    }


}
