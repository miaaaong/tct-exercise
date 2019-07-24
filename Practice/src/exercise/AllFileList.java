package exercise;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AllFileList {

	//특정 위치에서 검색어를 포함한 파일만 모두 찾기
	public static void main(String[] args) {
		File root = new File("D:\\EA_DEV\\git-repository");
		String search = "pmml";
		List<String> list = new ArrayList<>();
		List<String> allFiles = getAllFiles(root, search, list);
		for(String name : allFiles) {
			System.out.println(name);
		}
		
	}

	public static List<String> getAllFiles(File root, String search, List<String> list) {
		if(root.isDirectory()) {
			File[] listFiles = root.listFiles();
			for(File file : listFiles) {
				list = getAllFiles(file, search, list);
			}
		} else {
			if(root.getName().indexOf(search) > -1) {
				list.add(root.getName());
			}
		}
		return list;
	}
	
}
