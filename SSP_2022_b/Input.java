package com.lgcns.test;

public class Input {

	int queueNum;
	long timestamp;
	String value;

	public Input() {

	}

	public Input(int queueNum, long timestamp, String value) {
		this.queueNum = queueNum;
		this.timestamp = timestamp;
		this.value = value;
	}

	public int getQueueNum() {
		return queueNum;
	}

	public void setQueueNum(int queueNum) {
		this.queueNum = queueNum;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
