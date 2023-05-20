package com.lgcns.test;

public class Output {

	private String result;
	private long createdTime;
	
	public Output(String result) {
		this.result = result;
		this.createdTime = System.currentTimeMillis();
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

}
