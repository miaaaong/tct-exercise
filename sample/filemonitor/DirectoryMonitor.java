package sample.filemonitor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DirectoryMonitor implements Runnable {

	private List<String> fileNameList = new ArrayList<>();
	private Map<String, List<String>> map;
	
	public DirectoryMonitor(Map<String, List<String>> map) {
		// 파일별 데이터를 담을 map
		// 이 디렉토리 모니터를 실행한 main에서 값을 사용해야 하는 경우에는 받아서 사용
		this.map = map;
	}
	
	@Override
	public void run() {
		// 모니터링 대상 디렉토리
		File dir = new File("./INPUT");
		// 모니터링 - 계속 반복
		while(true) {
			String[] list = dir.list();
			for(String file : list) {
				// 파일이 기존 파일 목록에 없던 경우 - 새로 생성된 파일
				if(fileNameList.indexOf(file) < 0) {
					// 신규 파일 내용을 담을 변수
					List<String> monitoringList = new ArrayList<>();
					// 신규 파일에 대한 모니터링 시작
					FileMonitor reader = new FileMonitor(file, monitoringList);
					Thread t = new Thread(reader);
					t.start();
					// 기존 파일 목록에 신규 파일도 추가
					fileNameList.add(file);
					// 신규 파일 내용 변수를 map에 추가
					map.put(file, monitoringList);
				}
			}
			
			try {
				// 모니터링 주기
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
