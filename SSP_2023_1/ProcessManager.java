package com.lgcns.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProcessManager {

	// 디바이스별 실행 중인 프로세스 수
	private static Map<String, Integer> countMap = new HashMap<String, Integer>();
	// 디바이스별 실행 중인 커맨드 정보
	private static Map<String, Set<String>> commandMap = new HashMap<String, Set<String>>();
	
	public synchronized static boolean canProcessNow(Device device, String command) {
		String deviceName = device.getDeviceName();
		
		// 실행 중인 프로세스 수가 max이면 더 실행 불가
		int currentCount = (countMap.containsKey(deviceName) ? countMap.get(deviceName) : 0);
		if(device.getParallelProcessingCount() <= currentCount) {
			return false;
		}
		
		// 실행 중인 커맨드와 중복이면 더 실행 불가
		if(commandMap.containsKey(deviceName) && commandMap.get(deviceName).contains(command)) {
			return false;
		}
		
		// 실행 중인 프로세스 수 증가
		countMap.put(deviceName, ++currentCount);
		
		// 실행 중인 커맨드 정보에 추가
		if(!commandMap.containsKey(deviceName)) {
			Set<String> set = new HashSet<String>();
			commandMap.put(deviceName, set);
		}
		commandMap.get(deviceName).add(command);
	
		// 실행 가능
		return true;
	}
	
	public synchronized static void completeProcess(Device device, String command) {
		String deviceName = device.getDeviceName();
		
		// 실행 중인 프로세스 수 감소
		int count = countMap.get(deviceName);
		countMap.put(deviceName, --count);
		
		// 실행 중인 커맨드 정보에서 삭제
		Set<String> commands = commandMap.get(deviceName);
		commands.remove(command);
	}
}
