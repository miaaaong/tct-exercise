package com.lgcns.test;

public class Device {

	private String deviceName;
	private String type;
	private String hostName;
	private String port;
	private int parallelProcessingCount;

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getParallelProcessingCount() {
		return parallelProcessingCount;
	}

	public void setParallelProcessingCount(int parallelProcessingCount) {
		this.parallelProcessingCount = parallelProcessingCount;
	}

}
