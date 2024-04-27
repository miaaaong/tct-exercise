package com.lgcns.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ProcessManager {

	// ����̽��� ���� ���� ���μ��� ��
	private static Map<String, Integer> countMap = new HashMap<String, Integer>();
	// ����̽��� ���� ���� Ŀ�ǵ� ����
	private static Map<String, Set<String>> commandMap = new HashMap<String, Set<String>>();
	
	public synchronized static boolean canProcessNow(Device device, String command) {
		String deviceName = device.getDeviceName();
		
		// ���� ���� ���μ��� ���� max�̸� �� ���� �Ұ�
		int currentCount = (countMap.containsKey(deviceName) ? countMap.get(deviceName) : 0);
		if(device.getParallelProcessingCount() <= currentCount) {
			return false;
		}
		
		// ���� ���� Ŀ�ǵ�� �ߺ��̸� �� ���� �Ұ�
		if(commandMap.containsKey(deviceName) && commandMap.get(deviceName).contains(command)) {
			return false;
		}
		
		// ���� ���� ���μ��� �� ����
		countMap.put(deviceName, ++currentCount);
		
		// ���� ���� Ŀ�ǵ� ������ �߰�
		if(!commandMap.containsKey(deviceName)) {
			Set<String> set = new HashSet<String>();
			commandMap.put(deviceName, set);
		}
		commandMap.get(deviceName).add(command);
	
		// ���� ����
		return true;
	}
	
	public synchronized static void completeProcess(Device device, String command) {
		String deviceName = device.getDeviceName();
		
		// ���� ���� ���μ��� �� ����
		int count = countMap.get(deviceName);
		countMap.put(deviceName, --count);
		
		// ���� ���� Ŀ�ǵ� �������� ����
		Set<String> commands = commandMap.get(deviceName);
		commands.remove(command);
	}
}
