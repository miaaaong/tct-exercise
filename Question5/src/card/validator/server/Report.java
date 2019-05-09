package card.validator.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import card.validator.server.vo.AnalysisResult;
import card.validator.utils.CardUtility;

public class Report {

	public static void analyze() {
		//오늘 날짜에 해당하는 대상 파일 조회
		List<File> todayFiles = getTodayFiles();
		
		//파일별로 합산해서 map에 검사원id별로 반영
		Map<String, AnalysisResult> map = new TreeMap<String, AnalysisResult>();
		for(File file : todayFiles) {
			AnalysisResult result = getAnalysisResult(file);
			if(map.containsKey(result.getEmpId())) {
				//기존 결과에 더함
				AnalysisResult oldResult = map.get(result.getEmpId());
				oldResult.setCardNum(oldResult.getCardNum() + result.getCardNum());
				oldResult.setInvalidNum(oldResult.getInvalidNum() + result.getInvalidNum());
				map.put(oldResult.getEmpId(), oldResult);
			} else {
				//현재 결과 추가
				map.put(result.getEmpId(), result);
			}
		}
		
		//report 파일 생성
		createReport(map);
	}
	
	public static void printReport(String date) {
		File dir = new File("../SERVER");
		String fileName = "REPORT_" + date + ".TXT";
		File report = new File(dir, fileName);
		if(report.exists()) {
			try (FileReader fr = new FileReader(report);
					BufferedReader br = new BufferedReader(fr)) {
				String line = "";
				while ((line = br.readLine()) != null) {
					System.out.println(line);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void printSortedReport(String date) {
		File dir = new File("../SERVER");
		String fileName = "REPORT_" + date + ".TXT";
		File report = new File(dir, fileName);
		if(report.exists()) {
			List<AnalysisResult> list = new ArrayList<AnalysisResult>();
			
			try (FileReader fr = new FileReader(report);
					BufferedReader br = new BufferedReader(fr)) {
				String line = "";
				while ((line = br.readLine()) != null) {
					String[] split = line.split(" ");
					String empId = split[0];
					int cardNum = Integer.parseInt(split[1]);
					int invalidNum = Integer.parseInt(split[2]);
					AnalysisResult result = new AnalysisResult();
					result.setEmpId(empId);
					result.setCardNum(cardNum);
					result.setInvalidNum(invalidNum);
					list.add(result);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Collections.sort(list);
			for(AnalysisResult result : list) {
				System.out.println(result);
			}
		}
	}
	
	private static void createReport(Map<String, AnalysisResult> map) {
		//map에서 검사원id별로 꺼내서 한줄씩 쓰기 (검사원id 오름차순)
		File dir = new File("../SERVER");
		String fileName = "REPORT_" + CardUtility.getCurrentDateString() + ".TXT";
		File report = new File(dir, fileName);
		try(FileWriter fw = new FileWriter(report);
			BufferedWriter bw = new BufferedWriter(fw)) {
			for(String key : map.keySet()) {
				AnalysisResult analysisResult = map.get(key);
				bw.write(analysisResult.toString());
				bw.write(System.lineSeparator());
			}
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static AnalysisResult getAnalysisResult(File file) {
		String empId = file.getName().substring(0, 8);
		int cardNum = 0;
		int invalidNum = 0;
		try (FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr)) {
			String line = "";
			//ex. INSP_007#BUS_003#CARD_001BUS_003N20180131134404#R1#20180131135022
			while((line = br.readLine()) != null) {
				String[] split = line.split("#");
				cardNum++;
				String code = split[3];
				if(!code.equals("R1")) {
					invalidNum++;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		AnalysisResult result = new AnalysisResult();
		result.setEmpId(empId);
		result.setCardNum(cardNum);
		result.setInvalidNum(invalidNum);
		
		return result;
	}
	
	private static List<File> getTodayFiles() {
		List<File> list = new ArrayList<File>();
		String currentDateString = CardUtility.getCurrentDateString();
		File dir = new File("../SERVER");
		if(dir.exists()) {
			//오늘 날짜 파일만 필터링
			String exp = "INSP_[0-9]{3}_" + currentDateString + "[0-9]{6}.TXT";
			File[] listFiles = dir.listFiles();
			for(File file : listFiles) {
				boolean matches = file.getName().matches(exp);
				if(matches) {
					list.add(file);
				}
			}
		}
		return list;
	}
}
