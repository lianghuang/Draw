package com.sectong.message;

/**
 * 错误信息处理
 * 
 * @author jiekechoo
 *
 */
public class Message {

	public static final int SUCCESS=1;

	public static final int ERROR=0;

	private int code;
	private String message;
	private Object content;

	public void setCode(int code) {
		this.code = code;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public Object getContent() {
		return content;
	}

	public Message() {

	}
	public static Message successMsg(Object content){
		Message message=new Message();
		message.setCode(SUCCESS);
		message.setContent(content);
		return message;
	}

	public static Message errorMsg(String message){
		return new Message(ERROR,message);
	}

	public Message(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public Message(int code, String message, Object content) {
		this.code = code;
		this.message = message;
		this.content = content;
	}

	public void setMsg(int code, String message) {
		this.code = code;
		this.message = message;
		this.content = "no content";
	}

	public void setMsg(int code, String message, Object content) {
		this.code = code;
		this.message = message;
		this.content = content;
	}

}
