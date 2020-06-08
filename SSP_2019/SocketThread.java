package com.lgcns.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SocketThread implements Runnable {

	private ServerSocket serverSocket;
	
	// mobile
	private List<SocketThread> threadList;
	private List<BusInfo> busList;
	private List<Station> stationList;
	
	// bus
	private boolean isBusInfo;
	private String busId;
	private String prev1;
	private String prev2;
	private String time;

	public SocketThread(ServerSocket serverSocket, List<SocketThread> threadList) {
		this.serverSocket = serverSocket;
		this.threadList = threadList;
	}

	@Override
	public void run() {
		Socket socket = null;
		try {
			// client ���
			socket = serverSocket.accept();
			InputStream inputStream = socket.getInputStream();
			int count = 0;
			byte[] buffer = new byte[1024];
			while ((count = inputStream.read(buffer)) > 0) {
				String value = new String(buffer, 0, count).trim();
				if (isBusInfo) {
					// ���� ������ ��� ����
					prepareBusInfo(value);
				} else {
					if (value.startsWith("BUS")) {
						// �� ������� ���� ���� ó��
						isBusInfo = true;
						busId = value;
					} else if (value.equals("PRINT")) {
						// ����� ����
						// �ٸ� �����忡�� ��� ���� ������ ��� ó��, ���
						doMobilePrintWork();
					} else if (!value.equals("MOBILE")){
						// ����� ����
						// ���� ������ �� ���� ��ġ ���� ó��, ���
						doMobileCustomerWork(value, socket);
					}
				}
			}
		} catch (IOException e) {
//			e.printStackTrace();
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void doMobileCustomerWork(String line, Socket socket) {
		String[] split = line.split("#");
		// ���� ������ ������
		String targetStationId = split[0];
		// ���� ���� ��ġ
		int currLoc = Integer.parseInt(split[1]);
		
		// ���� ������ Ż ������
		Station currStation = null;
		for(Station station : stationList) {
			if(station.getLocation() > currLoc) {
				currStation = station;
				break;
			}
		}
		// ���� ������ Ż �����忡 ���� ����� ����
		BusInfo nearestBus = getNearestBus(currStation.getLocation(), busList);
		// �ش� ������ ���� ������ �����忡 ������ ������ �ҿ� �ð�
		Integer laptime = nearestBus.getMap().get(targetStationId);
		
		// ���� �ð� + �ҿ�ð� => ������ ������ ���� �ð�
		String arrivalTime = getArrivalTime(time, laptime);
		
		// ���
		try (OutputStream outputStream = socket.getOutputStream();){
			outputStream.write(arrivalTime.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void doMobilePrintWork() {
		// ���� ����
		busList = new ArrayList<BusInfo>();
		for(SocketThread st : threadList) {
			if(st.isBusInfo) {
				int interval = getInterval(st.time, st.prev2);
				this.time = st.time;
				
				String[] data1 = st.prev1.split("#");
				String[] data2 = st.prev2.split("#");
				int loc1 = Integer.parseInt(data1[1]);
				int loc2 = Integer.parseInt(data2[1]);
				
				int speed = loc2 - loc1;
				BusInfo busWithSpeed = new BusInfo(st.busId);
				// ���� �ӵ�
				busWithSpeed.setSpeed(speed);
				// ������ ��ġ
				busWithSpeed.setLoc(loc2);
				// ��� ��� �ð����� ����
				busWithSpeed.setInterval(interval);
				
				busList.add(busWithSpeed);
			}
		}
		
		// ������ ����
		stationList = readStation();
		
		for(BusInfo bus : busList) {
			// ������ ���� ��ġ
			calculateFinalLocation(bus, stationList);
			// �����庰 �ҿ� �ð�
			calculateStationTime(bus, stationList);
		}
		
		// ��/���� ����, ������ �ֱ��� ���� ���� ó�� �� ���
		calculate(time, busList, stationList);
		// �����庰 ���� ���� ���� ���� ó�� �� ���
		calculateSignage(time, busList, stationList);
		
	}
	
	private void calculateSignage(String time, List<BusInfo> busList, List<Station> stationList) {
		List<String> lines = new ArrayList<>();
		
		// �����庰 ���� ó��
		for(Station station : stationList) {
			String stationId = station.getStationId();
			Integer minTime = null;
			String busId = "NOBUS";
			// ��� ������ �ش� ������ ���� �ҿ�ð��� üũ�Ͽ� ���� ���� �ɸ��� ���� ã��
			for(BusInfo bus : busList) {
				Integer targetTime = bus.getMap().get(stationId);
				if(targetTime != null) {
					if(minTime == null) {
						minTime = targetTime;
						busId = bus.getId();
					} else {
						if(targetTime < minTime) {
							minTime = targetTime;
							busId = bus.getId();
						}
					}
				}
			}
			
			// ���� �ð� + �ҿ� �ð� = ���� �ð�
			String arrivalTime = "00:00:00";
			if(minTime != null) {
				arrivalTime = getArrivalTime(time, minTime);
			}
			
			StringBuffer sb = new StringBuffer();
			sb.append(time);
			sb.append("#");
			sb.append(stationId);
			sb.append("#");
			sb.append(busId);
			sb.append(",");
			sb.append(arrivalTime);
			
			lines.add(sb.toString());
		}
		
		printSignage(lines);
	}
	
	// �ܺ� ���α׷��� ���
	private void printSignage(List<String> lines) {
		Process cmdProcess = null;
		try {
			cmdProcess = new ProcessBuilder("SIGNAGE.EXE").start();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		try (OutputStream os = cmdProcess.getOutputStream();
				OutputStreamWriter osw = new OutputStreamWriter(os);
				BufferedWriter stdOut = new BufferedWriter(osw)) {
			for(String line : lines) {
				stdOut.write(line);
				stdOut.newLine();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
	}
	
	// ��/���� ����, ������ �ֱ��� ���� ���� ó�� �� ���
	private void calculate(String time, List<BusInfo> list, List<Station> stations) {
		// �ָ� �� ������ ����
		Collections.sort(list);
		
		// �������� �� �� ���� ���� ����
		for(int i=0; i<list.size(); i++) {
			BusInfo busLocInfo = list.get(i);
			if(i>0) {
				busLocInfo.setFront(list.get(i-1).getId());
			}
			if(i<list.size()-1) {
				busLocInfo.setBack(list.get(i+1).getId());
			}
		}
		
		// ���� �������� ����� ���� TreeMap�� ����
		Map<String, BusInfo> busMap = new TreeMap<>();
		for(BusInfo info : list) {
			busMap.put(info.getId(), info);
		}
	
		// ������ ��/���� ���� ���� ���� �� ���
		List<String> prepostInfo = createPrepostInfo(time, busMap);
		printFile("OUTFILE/PREPOST.TXT", prepostInfo);
		
		// �����庰 �ֱ��� ���� ���� ���� �� ���
		List<String> nearestBusForStationInfo = calculateNearestBusForStation(time, list, stations);
		printFile("OUTFILE/ARRIVAL.TXT", nearestBusForStationInfo);
	}
	
	// �����庰 �ֱ��� ���� ���� ����
	private List<String> calculateNearestBusForStation(String time, List<BusInfo> list, List<Station> stations) {
		List<String> distanceList = new ArrayList<>();
		for(Station stationInfo : stations) {
			String stationId = stationInfo.getStationId();
			int stationLoc = stationInfo.getLocation();
			// ���� �ð��� ������� �Ÿ��� ���� ����� ����
			BusInfo nearestBus = getNearestBus(stationLoc, list);
			String busId = "NOBUS";
			String distance = "00000";
			if(nearestBus != null) {
				busId = nearestBus.getId();
				distance = getStringValue(stationLoc - nearestBus.getLoc());
			}
			StringBuffer sb = new StringBuffer();
			sb.append(time);
			sb.append("#");
			sb.append(stationId);
			sb.append("#");
			sb.append(busId);
			sb.append(",");
			sb.append(distance);
			
			distanceList.add(sb.toString());
		}
		
		return distanceList;
	}
	
	private BusInfo getNearestBus(int stationLoc, List<BusInfo> list) {
		BusInfo bus = null;
		for(BusInfo info : list) {
			if(info.getLoc() < stationLoc) {
				bus = info;
				break;
			}
		}
		return bus;
	}
	
	// ������ ��/���� ���� ���� ���� �� ���
	private List<String> createPrepostInfo(String time, Map<String, BusInfo> map) {
		List<String> list = new ArrayList<>();
		Iterator<String> iterator = map.keySet().iterator();
		while(iterator.hasNext()) {
			String busId = iterator.next();
			BusInfo busLocInfo = map.get(busId);
			
			String frontId = "NOBUS";
			String frontDistance = "00000";
			if(busLocInfo.getFront() != null) {
				BusInfo frontBus = map.get(busLocInfo.getFront());
				frontId = frontBus.getId();
				frontDistance = getStringValue(frontBus.getLoc() - busLocInfo.getLoc());
			}
			 
			String backId = "NOBUS";
			String backDistance = "00000";
			if(busLocInfo.getBack() != null) {
				BusInfo backBus = map.get(busLocInfo.getBack());
				backId = backBus.getId();
				backDistance = getStringValue(busLocInfo.getLoc() - backBus.getLoc());
			}
			
			StringBuffer sb = new StringBuffer();
			sb.append(time);
			sb.append("#");
			sb.append(busId);
			sb.append("#");
			sb.append(frontId);
			sb.append(",");
			sb.append(frontDistance);
			sb.append("#");
			sb.append(backId);
			sb.append(",");
			sb.append(backDistance);
			
			list.add(sb.toString());
		}
		return list;
	}
	
	// ������ interval �ð� �� ���� ��ġ ���
	private void calculateFinalLocation(BusInfo bus, List<Station> stationList) {
		int interval = bus.getInterval();
		int currLocation = bus.getLoc();
		int stationIdx = -1;
		for(int i=0; i<stationList.size(); i++) {
			Station station = stationList.get(i);
			if(currLocation < station.getLocation()) {
				stationIdx = i;
				break;
			}
		}
		Station targetStation = stationList.get(stationIdx);
		int currSpeed = Math.min(bus.getSpeed(), stationList.get(stationIdx-1).getLimitSpeed());
		
		int remainTime = interval;
		while(remainTime > 0) {
			int laptime = (targetStation.getLocation() - currLocation)/currSpeed;
			if(laptime > remainTime) {
				// remainTime�� ���
				currLocation += remainTime * currSpeed;
				remainTime = 0;
			} else {
				// �� ���� ��
				currLocation = targetStation.getLocation();
				stationIdx++;
				targetStation = stationList.get(stationIdx);
				currSpeed = Math.min(bus.getSpeed(), stationList.get(stationIdx-1).getLimitSpeed());
				remainTime -= laptime;
			}
		}
		
		bus.setLoc(currLocation);
	}
	
	// ��� �������� ���� ���� �ð� ���
	private void calculateStationTime(BusInfo bus, List<Station> stationList) {
		int currLocation = bus.getLoc();
		int stationIdx = -1;
		for(int i=0; i<stationList.size(); i++) {
			Station station = stationList.get(i);
			if(currLocation < station.getLocation()) {
				stationIdx = i;
				break;
			}
		}
		
		Map<String, Integer> map = new HashMap<>();

		int interval = 0;
		for(int i=stationIdx; i<stationList.size(); i++) {
			Station targetStation = stationList.get(i);
			int currSpeed = Math.min(bus.getSpeed(), stationList.get(i-1).getLimitSpeed());
			int laptime = (targetStation.getLocation() - currLocation)/currSpeed;
			interval += laptime;
			map.put(targetStation.getStationId(), interval);
			currLocation = targetStation.getLocation();
		}
		
		bus.setMap(map);
	}
	
	// ������ ���� ����
	private List<Station> readStation() {
		List<Station> list = new ArrayList<>();
		File file = new File("INFILE/STATION.TXT");
		try (FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);) {
			String line = br.readLine();
			while(line != null) {
				String[] split = line.split("#");
				Station station = new Station();
				station.setStationId(split[0]);
				station.setLocation(Integer.parseInt(split[1]));
				station.setLimitSpeed(Integer.parseInt(split[2])*1000/60/60);
				list.add(station);
				
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	// �ð� ����(��) ���
	private int getInterval(String time, String data) {
		String time2 = data.split("#")[0];
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date1 = sdf.parse(time);
			Date date2 = sdf.parse(time2);
			int diff = (int) ((date1.getTime() - date2.getTime())/1000);
			return diff;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	// ���� �ð����κ��� lapTime(��) �� �ð� ���
	private String getArrivalTime(String startTime, int lapTime) {
		String arrivalTime = "00:00:00";
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date1 = sdf.parse(startTime);
			long result = date1.getTime() + lapTime*1000;
			Date newDate = new Date(result);
			arrivalTime = sdf.format(newDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return arrivalTime;
	}
	
	// ������ ��ȿ ���� 2�ǰ� ������ �ð� ���� ����
	private void prepareBusInfo(String line) {
		if (prev1 == null) {
			prev1 = line;
			return;
		}
		if (prev2 == null) {
			prev2 = line;
			return;
		}
		if (line.indexOf("#") < 0) {
			time = line;
		} else {
			prev1 = prev2;
			prev2 = line;
		}
	}

	// ���� ���
	private void printFile(String filePath, List<String> list) {
		File file = new File(filePath);
		try(FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw)) {
			for(String line : list) {
				bw.write(line);
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// �Ÿ��� 5�ڸ��� ���� ��ȯ
	private String getStringValue(int distance) {
		StringBuffer sb = new StringBuffer();
		String value = String.valueOf(distance);
		int count = 5 - value.length();
		for(int i=0; i<count; i++) {
			sb.append("0");
		}
		sb.append(value);
		return sb.toString();
	}
	
}
