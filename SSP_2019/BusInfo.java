package com.lgcns.test;

import java.util.Map;

public class BusInfo implements Comparable<BusInfo>{

	private String id;
	private int loc;
	private String front;
	private String back;
	// m/s
	private int speed;
	// 정류장별 소요 시간 
	private Map<String, Integer> map;
	private int interval;
	
	public BusInfo(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLoc() {
		return loc;
	}

	public void setLoc(int loc) {
		this.loc = loc;
	}

	public String getFront() {
		return front;
	}

	public void setFront(String front) {
		this.front = front;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}
	
	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public Map<String, Integer> getMap() {
		return map;
	}

	public void setMap(Map<String, Integer> map) {
		this.map = map;
	}
	
	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	@Override
	public int compareTo(BusInfo o) {
		if(this.loc > o.getLoc()) {
			return -1;
		} else if(this.loc < o.getLoc()) {
			return 1;
		}
		return 0;
	}

}