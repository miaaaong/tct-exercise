package com.lgcns.test;

public class Data {

	private String line;
	private String number;
	private int sysRate;
	private int proc1Rate;
	private int proc2Rate;
	private int proc3Rate;
	private int proc4Rate;
	private int proc5Rate;
	private int movingAvg;

	public Data(String line) {
		this.line = line;
		set();
	}

	private void set() {
		String[] split = this.line.split("#");
		this.number = split[0];
		String[] split2 = split[1].split(":");
		this.sysRate = Integer.parseInt(split2[1]);
		split2 = split[2].split(":");
		this.proc1Rate = Integer.parseInt(split2[1]);
		split2 = split[3].split(":");
		this.proc2Rate = Integer.parseInt(split2[1]);
		split2 = split[4].split(":");
		this.proc3Rate = Integer.parseInt(split2[1]);
		split2 = split[5].split(":");
		this.proc4Rate = Integer.parseInt(split2[1]);
		split2 = split[6].split(":");
		this.proc5Rate = Integer.parseInt(split2[1]);
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getSysRate() {
		return sysRate;
	}

	public void setSysRate(int sysRate) {
		this.sysRate = sysRate;
	}

	public int getProc1Rate() {
		return proc1Rate;
	}

	public void setProc1Rate(int proc1Rate) {
		this.proc1Rate = proc1Rate;
	}

	public int getProc2Rate() {
		return proc2Rate;
	}

	public void setProc2Rate(int proc2Rate) {
		this.proc2Rate = proc2Rate;
	}

	public int getProc3Rate() {
		return proc3Rate;
	}

	public void setProc3Rate(int proc3Rate) {
		this.proc3Rate = proc3Rate;
	}

	public int getProc4Rate() {
		return proc4Rate;
	}

	public void setProc4Rate(int proc4Rate) {
		this.proc4Rate = proc4Rate;
	}

	public int getProc5Rate() {
		return proc5Rate;
	}

	public void setProc5Rate(int proc5Rate) {
		this.proc5Rate = proc5Rate;
	}

	public int getMovingAvg() {
		return movingAvg;
	}

	public void setMovingAvg(int movingAvg) {
		this.movingAvg = movingAvg;
	}

}
