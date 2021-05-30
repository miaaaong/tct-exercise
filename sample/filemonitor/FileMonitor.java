package sample.filemonitor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class FileMonitor implements Runnable {
	
	private String fileName;
	private List<String> monitoringList;

	public FileMonitor(String fileName, List<String> monitoringList) {
		this.fileName = fileName;
		// 읽은 내용을 담을 변수 - 이 thread를 생성한 main에서도 사용할 수 있도록 받아서 사용
		this.monitoringList = monitoringList;
	}
	
	@Override
	public void run() {
		//모니터링 대상 파일
		File file = new File("./INPUT/"+fileName);
		FileReader fr = null;
		BufferedReader br = null;
		String line;
		try {
			// 모니터링 - 계속 반복
			while(true) {
				// 파일이 생긴 후에만 읽기 시작
				if(file.exists()) {
					// reader는 맨 처음에만 오픈, 이후에는 기존 것 사용
					if(fr == null && br == null) {						
						fr = new FileReader(file);
						br = new BufferedReader(fr);
					}
					
					// 현재 위치에서 파일 읽기
					line = br.readLine();
					if(line != null) {
						// 새로 추가된 내용이 있어 읽은 값이 있을 때
						// 로직 구현
						monitoringList.add(line);
					}
				}
				// 모니터 간격
				Thread.sleep(100);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
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

}