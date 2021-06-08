package com.lgcns.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataReader implements Runnable {
	
	private List<Data> monitoringList;

	public DataReader(List<Data> monitoringList) {
		this.monitoringList = monitoringList;
	}
	
	@Override
	public void run() {
		File file = new File("./INPUT/MONITORING.TXT");
		FileReader fr = null;
		BufferedReader br = null;
		String line;
		List<Integer> list = new ArrayList<>();
		
		try {
			while(true) {
				if(file.exists()) {
					if(fr == null && br == null) {						
						fr = new FileReader(file);
						br = new BufferedReader(fr);
					}
					
					line = br.readLine();
					if(line != null) {
						Data data = new Data(line);
						list.add(data.getSysRate());
						if(list.size() > 3) {
							list.remove(0);
						}
						double output = list.stream().mapToInt(Integer::intValue).average().orElse(0.0);
						int average = (int) output;
//						System.out.println(average);
						data.setMovingAvg(average);
						monitoringList.add(data);
					}
						
				}
				Thread.sleep(100);
			}
		} catch(Exception e) {
			
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(fr != null) {
				try {
					fr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

}
