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
			// client 대기
			socket = serverSocket.accept();
			InputStream inputStream = socket.getInputStream();
			int count = 0;
			byte[] buffer = new byte[1024];
			while ((count = inputStream.read(buffer)) > 0) {
				String value = new String(buffer, 0, count).trim();
				if (isBusInfo) {
					// 버스 정보면 계속 담음
					prepareBusInfo(value);
				} else {
					if (value.startsWith("BUS")) {
						// 이 스레드는 버스 정보 처리
						isBusInfo = true;
						busId = value;
					} else if (value.equals("PRINT")) {
						// 모바일 접속
						// 다른 스레드에서 모든 버스 정보를 모아 처리, 출력
						doMobilePrintWork();
					} else if (!value.equals("MOBILE")){
						// 모바일 접속
						// 고객의 목적지 및 현재 위치 정보 처리, 출력
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
		// 고객의 목적지 정류장
		String targetStationId = split[0];
		// 고객의 현재 위치
		int currLoc = Integer.parseInt(split[1]);
		
		// 고객이 버스를 탈 정류장
		Station currStation = null;
		for(Station station : stationList) {
			if(station.getLocation() > currLoc) {
				currStation = station;
				break;
			}
		}
		// 고객이 버스를 탈 정류장에 가장 가까운 버스
		BusInfo nearestBus = getNearestBus(currStation.getLocation(), busList);
		// 해당 버스가 고객의 목적지 정류장에 도착할 때까지 소요 시간
		Integer laptime = nearestBus.getMap().get(targetStationId);
		
		// 기준 시간 + 소요시간 => 목적지 정류장 도착 시간
		String arrivalTime = getArrivalTime(time, laptime);
		
		// 출력
		try (OutputStream outputStream = socket.getOutputStream();){
			outputStream.write(arrivalTime.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void doMobilePrintWork() {
		// 버스 정보
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
				// 추정 속도
				busWithSpeed.setSpeed(speed);
				// 마지막 위치
				busWithSpeed.setLoc(loc2);
				// 출력 대상 시간과의 차이
				busWithSpeed.setInterval(interval);
				
				busList.add(busWithSpeed);
			}
		}
		
		// 정류장 정보
		stationList = readStation();
		
		for(BusInfo bus : busList) {
			// 버스별 최종 위치
			calculateFinalLocation(bus, stationList);
			// 정류장별 소요 시간
			calculateStationTime(bus, stationList);
		}
		
		// 선/후행 정보, 정류장 최근접 버스 정보 처리 및 출력
		calculate(time, busList, stationList);
		// 정류장별 버스 도착 예정 정보 처리 및 출력
		calculateSignage(time, busList, stationList);
		
	}
	
	private void calculateSignage(String time, List<BusInfo> busList, List<Station> stationList) {
		List<String> lines = new ArrayList<>();
		
		// 정류장별 정보 처리
		for(Station station : stationList) {
			String stationId = station.getStationId();
			Integer minTime = null;
			String busId = "NOBUS";
			// 모든 버스의 해당 정류장 도착 소요시간을 체크하여 가장 적게 걸리는 버스 찾기
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
			
			// 기준 시간 + 소요 시간 = 도착 시간
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
	
	// 외부 프로그램에 출력
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
	
	// 선/후행 정보, 정류장 최근접 버스 정보 처리 및 출력
	private void calculate(String time, List<BusInfo> list, List<Station> stations) {
		// 멀리 간 순서로 정렬
		Collections.sort(list);
		
		// 버스별로 앞 뒤 버스 정보 세팅
		for(int i=0; i<list.size(); i++) {
			BusInfo busLocInfo = list.get(i);
			if(i>0) {
				busLocInfo.setFront(list.get(i-1).getId());
			}
			if(i<list.size()-1) {
				busLocInfo.setBack(list.get(i+1).getId());
			}
		}
		
		// 버스 오름차순 출력을 위해 TreeMap에 담음
		Map<String, BusInfo> busMap = new TreeMap<>();
		for(BusInfo info : list) {
			busMap.put(info.getId(), info);
		}
	
		// 버스별 선/후행 버스 정보 생성 및 출력
		List<String> prepostInfo = createPrepostInfo(time, busMap);
		printFile("OUTFILE/PREPOST.TXT", prepostInfo);
		
		// 정류장별 최근접 버스 정보 생성 및 출력
		List<String> nearestBusForStationInfo = calculateNearestBusForStation(time, list, stations);
		printFile("OUTFILE/ARRIVAL.TXT", nearestBusForStationInfo);
	}
	
	// 정류장별 최근접 버스 정보 생성
	private List<String> calculateNearestBusForStation(String time, List<BusInfo> list, List<Station> stations) {
		List<String> distanceList = new ArrayList<>();
		for(Station stationInfo : stations) {
			String stationId = stationInfo.getStationId();
			int stationLoc = stationInfo.getLocation();
			// 기준 시간에 정류장과 거리가 가장 가까운 버스
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
	
	// 버스별 선/후행 버스 정보 생성 및 출력
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
	
	// 버스별 interval 시간 후 추정 위치 계산
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
				// remainTime만 계산
				currLocation += remainTime * currSpeed;
				remainTime = 0;
			} else {
				// 더 가야 함
				currLocation = targetStation.getLocation();
				stationIdx++;
				targetStation = stationList.get(stationIdx);
				currSpeed = Math.min(bus.getSpeed(), stationList.get(stationIdx-1).getLimitSpeed());
				remainTime -= laptime;
			}
		}
		
		bus.setLoc(currLocation);
	}
	
	// 모든 정류장의 버스 도착 시간 계산
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
	
	// 정류장 정보 세팅
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
	
	// 시간 차이(초) 계산
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
	
	// 기준 시간으로부터 lapTime(초) 후 시간 계산
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
	
	// 버스의 유효 정보 2건과 마지막 시간 정보 갱신
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

	// 파일 출력
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
	
	// 거리를 5자리에 맞춰 변환
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
