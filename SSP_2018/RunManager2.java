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
import java.util.Stack;

public class RunManager2 {

	//file resource
	private static File file = null;
	private static FileReader fr = null;
	private static BufferedReader br = null;
	
	//encryption value
	private static Map<Character, Character> map = new HashMap<>();
	
	//line info
	private static Stack<String> stack = null;
	private static String prevResult = null;
	
	//file finish
	private static boolean isLast = false;

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(9876);
			socket = serverSocket.accept();
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			int count = 0;
			byte[] buffer = new byte[2048];
			while((count = is.read(buffer)) > 0) {
				String value = new String(buffer, 0, count);
				if(value.equals("ACK")) {
					//���� ���� ����
					if(isLast) {
						break;
					} else {
						String line = getLine(-1);
						prevResult = line;
						os.write(line.getBytes());
					}
				} else if(value.equals("ERR")) {
					String line = prevResult;
					os.write(line.getBytes());
				} else {
					try {
						int lineNum = Integer.parseInt(value);
						String line = getLine(lineNum);
						prevResult = line;
						os.write(line.getBytes());													
					} catch(Exception ex) {
						//init - ���ϸ� ������ ���
						String[] split = value.split("#");
						initCharMap(split[1]);
						setFileResource(split[0]);
						String line = getLine(-1);
						prevResult = line;
						os.write(line.getBytes());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br != null) {
					br.close();
				}
				if(fr != null) {
					fr.close();
				}
				if(socket != null) {
					socket.close();
				}
			} catch (IOException e) {
//				e.printStackTrace();
			}
		}
	}
	
	private static String getLine(int lineNum) throws IOException {
		if(lineNum < 0) {
			//���� �ڿ����� ��� ���� ����
			return getLineFromFile();
		} else {
			//������ ���� ���� �ش� ��ġ�� �̵� �� ���� ����
			setFileResource(file.getName());
			for(int i=1; i<lineNum; i++) {
				br.readLine();
			}
			return getLineFromFile();
		}
	}
	
	private static String getLineFromFile() throws IOException {
		String result = null;
		
		String line = br.readLine();
		while(line != null) {
			if(stack.isEmpty()) {
				stack.push(line);
			} else {
				//���� ���ΰ� ��
				String lastLine = stack.peek();
				if(lastLine.equals(line)) {
					//������ stack�� �׳� ���� ���� ����
					stack.push(line);
				} else {
					//�ٸ��� ���� ���ΰ� ���� ���� �� stack���� ������
					int count = 0;
					String value = "";
					while(!stack.isEmpty() && (value = stack.peek()).equals(lastLine)) {
						stack.pop();
						count++;
					}
					//���� ������ ��ȣȭ�Ͽ� ����� ��´�
					if(count > 1) {							
						result = count + "#" + getEncryptedLine(lastLine);
					} else {
						result = getEncryptedLine(lastLine);
					}
					//���� ���� �����Ƿ� ���� ���� ��� �ߴ�
					stack.push(line);
					break;
				}
			}
			//���� ���� �б�
			line = br.readLine();
		}
		
		if(line == null) {
			//���� ��
			//stack�� ���� ������ ����� ��´�
			int count = 0;
			String lastLine = stack.peek();
			while(!stack.isEmpty()) {
				stack.pop();
				count++;
			}
			if(count > 1) {							
				result = count + "#" + getEncryptedLine(lastLine);
			} else {
				result = getEncryptedLine(lastLine);
			}
			
			//���� ó�� ��
			isLast = true;
		}
		
		return result;
	}
	
	private static void setFileResource(String fileName) throws IOException {
		if(file == null) {			
			file = getFile(fileName, new File("BIGFILE"));
		}
		//file open ���� �� ���
		if(fr != null) {
			fr.close();			
		}
		fr = new FileReader(file);
		br = new BufferedReader(fr);
		
		//stack �ʱ�ȭ
		stack = new Stack<>();
	}
	
	private static String getEncryptedLine(String line) {
		String compressedLine = getCompressedLine(line);
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

	private static String getCompressedLine(String line) {
		Stack<Character> stack = new Stack<>();
		StringBuilder sb = new StringBuilder();
		
		char[] charArray = line.toCharArray();
		for(char c : charArray) {
			if(stack.isEmpty()) {
				stack.push(c);
			} else {
				//���� ���ڿ� ��
				Character lastChar = stack.peek();
				//�ٸ��� ���� ���ڿ� ���� ���� stack���� ������.
				if(!lastChar.equals(c)) {
					int count = 0;
					Character value = null;
					while(!stack.isEmpty() && (value = stack.peek()).equals(lastChar)) {
						stack.pop();
						count++;
					}
					//����� ��´�
					if(count > 2) {
						sb.append(count).append(lastChar);
					} else if(count == 2) {
						sb.append(lastChar).append(lastChar);
					} else {						
						sb.append(lastChar);
					}
				}
				//���� ���ڸ� stack�� ��´�
				stack.push(c);
			}
		}
		
		//stack�� ���� ���ڸ� ����� ��´�
		int count = 0;
		char lastChar = stack.peek();
		while(!stack.isEmpty()) {
			stack.pop();
			count++;
		}
		if(count > 2) {
			sb.append(count).append(lastChar);
		} else if(count == 2) {
			sb.append(lastChar).append(lastChar);
		} else {						
			sb.append(lastChar);
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
