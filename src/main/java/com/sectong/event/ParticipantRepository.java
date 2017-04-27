package com.sectong.event;

import org.thymeleaf.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author Sergi Almar
 */
public class ParticipantRepository {

	private Map<String, LoginEvent> activeSessions = new ConcurrentHashMap<>();

	public void add(String sessionId, LoginEvent event) {
		activeSessions.put(sessionId, event);
	}

	public void removePreviousSession(String username){
		String key=null;
		for(Map.Entry<String,LoginEvent> session:activeSessions.entrySet()){
			if(session.getValue().getUsername().equals(username)){
				key=session.getKey();
				break;
			}
		}
		if(!StringUtils.isEmpty(key)){
			activeSessions.remove(key);
		}
	}

	public boolean containsUser(String username){
		for(Map.Entry<String,LoginEvent> session:activeSessions.entrySet()){
			if(session.getValue().getUsername().equals(username)){
				return true;
			}
		}
		return false;
	}

	public LoginEvent getParticipant(String sessionId) {
		return activeSessions.get(sessionId);
	}

	public void removeParticipant(String sessionId) {
		activeSessions.remove(sessionId);
	}

	public Map<String, LoginEvent> getActiveSessions() {
		return activeSessions;
	}

	public void setActiveSessions(Map<String, LoginEvent> activeSessions) {
		this.activeSessions = activeSessions;
	}
}
