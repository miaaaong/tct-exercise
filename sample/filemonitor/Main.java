package sample.filemonitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

	public static void main(String[] args) {
		// 파일별 내용을 담을 변수
		Map<String, List<String>> map = new HashMap<>();
		
		// 디렉토리에 대한 모니터링 시작
		DirectoryMonitor dirMonitor = new DirectoryMonitor(map);
		Thread t = new Thread(dirMonitor);
		t.start();
		
		// 모니터링과 별개로 해야할 작업 구현
	}

}
