package com.lgcns.test;

public class Message {

	private String id;
	private String value;
	private String queueName;
	private boolean sendable = true;

	public Message() {

	}

	public Message(String queueName, String value) {
		String id = queueName + System.currentTimeMillis();
		this.queueName = queueName;
		this.value = value;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public boolean isSendable() {
		return sendable;
	}

	public void setSendable(boolean sendable) {
		this.sendable = sendable;
	}

	public boolean equals(Message msg) {
		if(msg.getId().equals(this.id)) {
			return true;
		} else {
			return false;
		}
	}
}
