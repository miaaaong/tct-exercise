package com.lgcns.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RunManager {
	
	//file resource
	private static File file = null;
	private static FileReader fr = null;
	private static BufferedReader br = null;
	
	//encryption value
	private static Map<Character, Character> map = new HashMap<>();
	
	//previous line info
	private static String prevLine = null;
	private static int count = 0;
	
	//previous result
	private static String prevResult = null;
	
	//file finish
	private static boolean isLast = false;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			//socket 생성
			serverSocket = new ServerSocket(9876);
			//client 대기
			socket = serverSocket.accept();
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			int count = 0;
			byte[] buffer = new byte[1024];
			while((count = inputStream.read(buffer)) > 0) {
				String value = new String(buffer, 0, count);
				if(value.equals("ACK")) {
					String line = getLine(-1);
					prevResult = line;
					outputStream.write(line.getBytes());						
				} else if(value.equals("ERR")) {
					outputStream.write(prevResult.getBytes());						
				} else {
					try {
						int lineNum = Integer.parseInt(value);
						String line = getLine(lineNum);
						prevResult = line;
						outputStream.write(line.getBytes());													
					} catch(Exception ex) {
						//init - 파일명 수신한 경우
						String[] split = value.split("#");
						initCharMap(split[1]);
						setFileResource(split[0]);
						String line = getLine(-1);
						prevResult = line;
						outputStream.write(line.getBytes());
					}
				}
				if(isLast) {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(socket != null) {
					socket.close();
				}
				if(br != null) {
					br.close();
				}
				if(fr != null) {
					fr.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	private static String getLine(int lineNum) throws IOException {
		if(lineNum < 0) {
			//현재 자원으로 계속 라인 연산
			return getLineFromFile();
		} else {
			//파일을 새로 열고 해당 위치로 이동 후 라인 연산
			setFileResource(file.getName());
			for(int i=1; i<lineNum; i++) {
				br.readLine();
			}
			return getLineFromFile();
		}
	}
	
	//현재 br로 유지하여 처리
	private static String getLineFromFile() throws IOException {
		String result = null;
		
		//파일을 처음부터 읽을 때에만 
		if(prevLine == null) {			
			prevLine = br.readLine();
			count = 1;
		}
		
		String currLine = br.readLine();
		while(currLine != null) {
			if(currLine.equals(prevLine)) {
				count++;
			} else {
				if(count == 1) {
					result = getEncryptedLine(prevLine, count);
				} else {
					result = count + "#" + getEncryptedLine(prevLine, count);
				}
				
				//init
				prevLine = currLine;
				count = 1;
				break;
			}
			currLine = br.readLine();
		}
		
		if(currLine == null) {
			//파일 끝
			if(count == 1) {
				result = getEncryptedLine(prevLine, count);
			} else {
				result = count + "#" + getEncryptedLine(prevLine, count);
			}
			isLast = true;
		}
		return result;
	}
	
	private static void setFileResource(String fileName) throws IOException {
		if(file == null) {			
			file = getFile(fileName, new File("BIGFILE"));
		}
		//file open 새로 할 경우
		if(fr != null) {
			fr.close();			
		}
		fr = new FileReader(file);
		br = new BufferedReader(fr);
		
		//이전에 읽었던 값 초기화
		prevLine = null;
	}

	private static String getEncryptedLine(String line, int repeat) {
		String compressedLine = getCompressedLine(line, repeat);
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<compressedLine.length(); i++) {
			char ch = compressedLine.charAt(i);
			int num = (int) ch;
			if(num >= 65 && num <= 90) {
				//encrypt
//				sb.append(getEncryptedChar(num));
				sb.append(map.get(ch));
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}
	
	private static void initCharMap(String key) {
		StringBuilder sb = new StringBuilder();
		sb.append(key);
		for(int i=65; i<=90; i++) {
			if(key.indexOf(i) < 0) {
				sb.append((char)i);
			}
		}
		String target = sb.toString();
		
		for(int i=0; i<target.length(); i++) {
			char value = (char) (i + 65);
			char encryped = target.charAt(i);
			map.put(value, encryped);
		}
	}
	
	private static char getEncryptedChar(int input) {
		int num = input - 5;
		if(num < 65) {
			num = 90 - (65 - num) + 1;
		}
		return (char) num;
	}
	
	private static String getCompressedLine(String line, int repeat) {
		StringBuilder sb = new StringBuilder();
		char prev = line.charAt(0);
		int count = 1;
		for(int i=1; i<line.length(); i++) {
			char ch = line.charAt(i);
			if(ch == prev) {
				count++;
			} else {
				if(count > 2) {
					//compress
					sb.append(count);
					sb.append(prev);
				} else {
					for(int j=0; j<count; j++) {
						sb.append(prev);
					}
				}
				//init
				prev = ch;
				count = 1;
			}
		}
		//last character
		if(count > 2) {
			//compress
			sb.append(count);
			sb.append(prev);
		} else {
			for(int j=0; j<count; j++) {
				sb.append(prev);
			}
		}
		return sb.toString();
	}
	
	private static File getFile(String input, File parentDir) {
		File[] listFiles = parentDir.listFiles();
		if(listFiles == null) {
			return null;
		}
		for(File child : listFiles) {
			if(!child.isDirectory() && child.getName().equals(input)) {
				return child;
			}
		}
		for(File child : listFiles) {
			if(child.isDirectory()) {
				File file = getFile(input, child);
				if(file != null) {
					return file;
				}
			}
		}
		return null;
	}
	
}
