package sample.externalprocess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class ExternalProcess {

	// argument가 있는 프로세스 실행 - ProcessBuilder 사용
	public String callProcess1(String log) {
		// argument를 전달하며 실행
		Process cmdProcess = null;
		try {
			cmdProcess = new ProcessBuilder("CODECONV.EXE", log).start();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		// 또는 프로세스명, argument를 array로 전달하며 실행
		try {
			String[] cmd = {"./SUPPORT/ALERT.EXE", log};
			cmdProcess = new ProcessBuilder(cmd).start();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		// 실행 결과 처리
		String s = null;
		try (InputStream is = cmdProcess.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr)) {
			s = br.readLine();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		
		return s;
	}
	
	// 프로세스 실행 후 standard output으로 값 전달
	public void callProcess2(List<String> lines) {
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
	
	// 프로세스 실행 - console 입출력
	// 참고 - https://stackoverflow.com/questions/5591215/run-external-program-concurrently-and-communicate-with-it-through-stdin-stdout
	public void callProcess4(String param) {
		Process proc = null;
		try {
			proc = new ProcessBuilder("/bin/bash").start();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		PrintWriter out = new PrintWriter(proc.getOutputStream());
		out.println("touch hello1");
		out.flush();

		out.println("ls -la hel*");
		out.flush();

		Scanner in = new Scanner(proc.getInputStream());
		while (in.hasNextLine())
            		System.out.println(in.nextLine());
		
	}	
	
	// argument가 있는 프로세스 실행 - Runtime 사용
	public String callProcess3(String message) {
		String msg = null;
		String[] cmd = { "CODECONV.EXE", message };
        	Process p;
		try {
			p = Runtime.getRuntime().exec(cmd);
			p.waitFor();
			
			
			// 실행 결과 처리
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
