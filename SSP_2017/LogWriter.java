package com.lgcns.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class LogWriter implements Runnable {
	
	private final BlockingQueue<String> sharedQueue;
	private AtomicBoolean running = new AtomicBoolean(true);
	
	public LogWriter(BlockingQueue<String> queue) {
		this.sharedQueue = queue;
	}
	
	public void stop() {
		this.running = new AtomicBoolean(false);
	}

	@Override
	public void run() {
		while(running.get()) {
			try {
				String log = sharedQueue.take();
				String[] split = log.split("#");
				String time = split[0];
				String type = split[1];
				String message = split[2];
				
				String convertLog = convertLog(message);
				writeLog(time, type, convertLog);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static synchronized void checkFile(File file) {
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeLog(String time, String type, String converMessage) {
		String log = time + "#" + type + "#" + converMessage;

		FileChannel channel = null;
		File file = new File("TYPELOG_4_" + type + ".TXT");
		checkFile(file);
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			channel = raf.getChannel();
			while(true) {
				try {
					FileLock lock = channel.tryLock();
					raf.seek(raf.length());
					raf.write(log.getBytes());
					raf.write(System.lineSeparator().getBytes());
					
					lock.release();
					
					break;
				} catch (OverlappingFileLockException oe) {
					System.out.println("oe");
				}
			}
			channel.close();
			raf.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String convertLog(String log) {
		Process cmdProcess = null;
		try {
			cmdProcess = new ProcessBuilder("CODECONV.EXE", log).start();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		// 외부 프로그램 출력 읽기
		String s = null;
		try (InputStream is = cmdProcess.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader stdOut = new BufferedReader(isr)) {
			s = stdOut.readLine();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		return s;
	}

	//외부 프로그램 실행 다른 방법
	private String convertMessage(String message) {
		String msg = null;
		String[] cmd = { "CODECONV.EXE", message };
        Process p;
		try {
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			
			StringBuilder textBuilder = new StringBuilder();
			InputStream inputStream = p.getInputStream();
			int c = 0;
	        while ((c = inputStream.read()) != -1) {
	            textBuilder.append((char) c);
	        }
			msg = textBuilder.toString();
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return msg;
	}
	
}
