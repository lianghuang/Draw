package com.sectong.domain;

import javax.persistence.*;

/**
 * 用户User POJO定义
 * 
 * @author jiekechoo
 *
 */
@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }) })
public class User {

    /**
     * 用户ID
     */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    /**
     * 用户名，要求唯一
     */
	private String username;

    /**
     * 用户昵称
     */
	private String nickname;

    /**
     * 密码，不会返回给前端
     */
	private String password;

    /**
     * 头像，未用到
     */
	private String image;

    /**
     * 见UserStatus
     */
    @Enumerated(EnumType.STRING)
	private UserStatus status=UserStatus.Empty;

    public enum UserStatus{
        /**
         * 用户离线，未加入任何房间
         */
        Empty,
        /**
         * 用户点击完准备
         */
        Ready,
        /**
         * 用户开始游戏
         */
        Gaming
    }

    /**
     * 是否启用，0未启用，1启用。未用到
     */
	private int enabled;

    /**
     * 当局游戏的分数
     */
	private int currentScore;

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getImage() {
		return "http://localhost:8080/" + image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

    public int getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    @Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", enabled=" + enabled + "]";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		return id.equals(user.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
