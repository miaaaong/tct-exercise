package com.lgcns.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class RunManager {
	
	private static Map<String, Integer> map = new TreeMap<>();
	
	private static BlockingQueue<String> sharedQueue = new LinkedBlockingQueue<String>();

	public static void main(String[] args) {
		//start threads
		int threadNum = 20;
		ExecutorService service = Executors.newFixedThreadPool(threadNum);
		
		List<LogWriter> threadList = new ArrayList<>();
		for(int i=0; i<threadNum; i++) {
			LogWriter writer = new LogWriter(sharedQueue);
			threadList.add(writer);
			service.execute(writer);
		}
	
		//process file
		readFile();
		
		//create report
		createReport();
		
		//wait and terminate threads
		while(true) {
			if(sharedQueue.isEmpty()) {
				break;
			}
		}
		
		for(LogWriter writer : threadList) {
			writer.stop();
		}
		
		service.shutdown();
		
		boolean termination = false;
		try {
			termination = service.awaitTermination(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (termination) {
			System.out.println("All tasks have completed execution.");
		} else {
			System.out.println("Timeout elapsed before termination.");
		}
		
	}
	
	private static void readFile() {
		File file = new File("LOGFILE_B.TXT");
		try(FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr)) {
			String line = null;
			while((line = br.readLine()) != null) {
				// enqueue message
				sharedQueue.put(line);
				
				// report
				String[] split = line.split("#");
				String type = split[1];
				
				if(map.containsKey(type)) {
					map.put(type, map.get(type) + 1);
				} else {
					map.put(type, 1);
				}				
			}
		} catch(Exception e) {
			
		}
	}
	
	private static void createReport() {
		File file = new File("REPORT_4.TXT");
		try(FileWriter fr = new FileWriter(file);
				BufferedWriter br = new BufferedWriter(fr)) {
			Iterator<String> iterator = map.keySet().iterator();
			while(iterator.hasNext()) {
				String type = iterator.next();
				String line = type + "#" + map.get(type);
				br.write(line);
				br.write(System.lineSeparator());
			}
		} catch(Exception e) {
			
		}
	}
}
