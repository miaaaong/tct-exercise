package com.lgcns.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RunManager {

	private static List<Data> monitoringList = new ArrayList<>();
	
	public static void main(String[] args) {
		DataReader reader = new DataReader(monitoringList);
		Thread t = new Thread(reader);
		t.start();
				
		Scanner sc = new Scanner(System.in); 
		int limit = sc.nextInt();
		
		checkError(limit);
	}
	
	private static void checkError(int limit) {
		System.out.println(monitoringList.size());
		System.out.println(limit);
		int index = 0;
		boolean isError = false;
		List<Data> list = new ArrayList<>();
		while(true) {
			if(monitoringList.size() > index) {
				Data data = monitoringList.get(index);
//				System.out.println(data.getLine());
				if(data.getMovingAvg() <= limit) {
//					System.out.println("임계값 이하 - 초기화 : " + data.getNumber() + ", " + data.getMovingAvg());
					//임계값 이하 - 원점
					list = new ArrayList<>();
					isError = false;
				} else {
					//임계값 넘은 경우
					if(list.size() == 0) {
						// 임계값 최초 초과
//						System.out.println("임계값 최초 초과, 원점 : " + data.getNumber() + ", " + data.getMovingAvg());
						list.add(data);
					} else {
						if(isError) {
							//장애 판별 이후에는 최대값 기준 -3 이내 감소도 포함
							int max = list.stream().mapToInt(Data::getMovingAvg).max().orElse(0);
//							System.out.println(max);
							if(data.getMovingAvg() - max >= -3) {
//								System.out.println("장애 판별 이후 최대값 기준 -3 이내 감소 : " + data.getNumber() + ", " + data.getMovingAvg());
								list.add(data);
							} else {
								//원점
//								System.out.println("장애 판별 이후 최대값 기준 -3 초과 감소, 원점 : " + data.getNumber() + ", " + data.getMovingAvg());
								list = new ArrayList<>();
								isError = false;
								list.add(data);
							}
						} else {
							//아직 장애 발생이 없는 경우에는 이전값 이상이면 포함
							if(data.getMovingAvg() >= list.get(list.size()-1).getMovingAvg()) {
//								System.out.println("아직 장애 발생이 없는데 이전값 이상 : " + data.getNumber() + ", " + data.getMovingAvg());
								list.add(data);
							} else {
								//원점
//								System.out.println("아직 장애 발생이 없는데 이전값 미만, 원점: " + data.getNumber() + ", " + data.getMovingAvg());
								list = new ArrayList<>();
								isError = false;
								list.add(data);
							}
						}
					}
				}
				
				if(list.size() > 5) {
					// 장애
					isError = true;
					alert(list.get(list.size()-1), limit);
				}
				
				index++;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void alert(Data data, int limit) {
		StringBuilder sb = new StringBuilder();
		sb.append(data.getLine());
		if(data.getSysRate() > limit) {
			sb.append("#Y#");
		} else {
			sb.append("#N#");
		}
		sb.append(getRateString(data.getMovingAvg()));
		
		Process cmdProcess = null;
		String[] cmd = {"./SUPPORT/ALERT.EXE", sb.toString()};
		try {
			cmdProcess = new ProcessBuilder(cmd).start();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
//	private static void writeReport(int limit) {
//		File file = new File("./OUTPUT/REPORT.TXT");
//		try(FileWriter fw = new FileWriter(file);
//				BufferedWriter bw = new BufferedWriter(fw)) {
//			for(Data data : monitoringList) {
//				if(data.getSysRate() > limit) {
//					bw.write(data.getLine() + "#Y#" + getRateString(data.getMovingAvg()));
//				} else {
//					bw.write(data.getLine() + "#N#" + getRateString(data.getMovingAvg()));
//				}
//				bw.newLine();
//			}
//		} catch(Exception e) {
//			
//		}
//	}

	private static String getRateString(int rate) {
		String value = "" + rate;
		int count = 3 - value.length();
		while(count > 0) {
			value = "0" + value;
			count--;
		}
		return value;
	}
}
