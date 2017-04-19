package com.sectong.domain;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 创建用户字段POJO定义
 * 
 * @author jiekechoo
 *
 */
public class UserCreateForm {

	@NotEmpty
	private String username;

	@NotEmpty
	private String password;

	private String nickname;

	private String image;

//	@NotEmpty
	private String vcode;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getVcode() {
		return vcode;
	}

	public void setVcode(String vcode) {
		this.vcode = vcode;
	}

	@Override
	public String toString() {
		return "UserCreateForm [username=" + username + ", password=" + password + "]";
	}

}
