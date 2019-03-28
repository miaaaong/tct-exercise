package com.lgcns.test.sourceanalyzer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SourceAnalyzer {
	
	private int javaFileCount;
	private int problemFileCount;
	private int totalLineCount;
	private int patternCount;
	
    /**
     * 전체 소스 개수를 반환하는 메소드.
     * @return
     */
    public int getJavaFileCount() {
        return javaFileCount;
    }
    
    /**
     * 문제 소스 개수를 반환하는 메소드.
     * @return
     */
    public int getProblemFileCount() {
        return problemFileCount;
    }
    
    /**
     * 소스 전체 Line 수를 반환하는 메소드.
     * @return
     */
    public int getTotalLineCount() {
        return totalLineCount;
    }
    
    /**
     * 검색된 패턴 수를 반환하는 메소드.
     * @return
     */
    public int getPatternCount() {
        return patternCount;
    }
    
	/**
	 * 디렉토리 하위 java 파일을 분석하는 메소드.
	 * 패턴을 검색할 소스코드는 주석을 포함한다.
	 * 
	 * @param dirName 분석 대상 디렉토리
	 * @param pattern 분석 대상 패턴
	 */
	public void analyze(String dirName, String pattern) {
		File[] listFiles = new File(dirName).listFiles();
		String newPattern = pattern.replaceAll(" ","").replaceAll("\\n\\r", "");
		for(File file : listFiles) {
			if(file.getName().endsWith(".java")) {
				//java file count
				javaFileCount++;
				
				List<String> readFile = readFile(file);
				List<String> contents = new ArrayList<>(); 
				int emptyLine = 0;
				for(String line : readFile) {
					String newLine = line.replaceAll(" ","").replaceAll("\\n\\r", "");
					if(newLine.equals("")) {
						emptyLine++;
					}
					contents.add(newLine);
				}
				//total line count
				totalLineCount += readFile.size() - emptyLine;
				
				//pattern match count
				int countPattern = countPattern(contents, newPattern);
				if(countPattern > 0) {
					System.out.println(file.getName());
					problemFileCount++;
					patternCount += countPattern;
				}
			}
		}
	}
	
	private int countPattern(List<String> contents, String pattern) {
		int count = 0;
		for(int i=0; i<contents.size(); i++) {
			String line = contents.get(i);
			if(pattern.startsWith(line)) {
				String target = "";
				int patternLength = pattern.length();
				int targetLength = 0;
				int index = i;
				while(targetLength < patternLength && index < contents.size() ) {
					String value = contents.get(index);
					target += value;
					targetLength += value.length();
					index++;
				}
				if(target.indexOf(pattern) > -1) {
					count++;
					i = index-1;
				}
			}
		}
		return count;
	}
	
	/**
	 * 제공되는 파일의 내용을 line 단위로 읽어 String List로 리턴하는 메소드(제공).
	 * @param file
	 * @return
	 */
	private List<String> readFile(File file) {
		List<String> strList = new ArrayList<String>();

		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(new FileReader(file));
			String line;
			while ((line = buffer.readLine()) != null) {
				strList.add(line);
			}
		} catch (IOException e) {
			System.out.println("IOException occurred. " + e.getMessage());
		} finally {
			if (buffer != null) {
				try {
					buffer.close();
				} catch (Exception e) {
					System.out.println("Error occurred while closing buffer.");
				}
			}
		}
		return strList;
	}
}
