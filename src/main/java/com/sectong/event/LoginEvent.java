package com.sectong.event;

import java.util.Date;

/**
 * 
 * @author Sergi Almar
 */
public class LoginEvent {

	private String username;
	private Date loginTime;
	private Date logoutTime;

	public LoginEvent(String username) {
		this.username = username;
		loginTime = new Date();
	}

	public void logout(){
		this.logoutTime=new Date();
	}

	public int getTimeBetweenLogOutAndNow(){
		if(logoutTime==null){
			return -1;//不会被删除
		}
		Date now=new Date();
		Long time=(now.getTime()-logoutTime.getTime())/1000;
		return time.intValue();
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public Date getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}
}
