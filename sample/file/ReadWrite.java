package sample.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadWrite {
	
	// 파일 읽기
	private List<String> initStationList() {
		List<String> stationList = new ArrayList<>();
		File file = new File("./INFILE/STATION.TXT");
		try (FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr); ) {
			String line = "";
			//다음 내용이 있으면
			while((line = br.readLine()) != null) {
				// 값에 대한 로직 구현
				// 결과에 담음
				stationList.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stationList;
	}

	// 파일 쓰기
	private static void printStation(List<String> list) {
		File file = new File("./OUTFILE/ARRIVAL.TXT");
		try (FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw); ) {
			for(String s : list) {
				// 값에 대한 로직 구현
				StringBuilder sb = new StringBuilder();
				sb.append(s);
				sb.append("#");
				// 파일에 쓰기
				bw.write(sb.toString());
				bw.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
