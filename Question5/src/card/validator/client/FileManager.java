package card.validator.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

	public static void saveInspectionResult(String empId, String time, String result) {
		File dir = new File("../" + empId);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		String fileName = empId + "_" + time + ".TXT";
		File file = new File(dir, fileName);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try (FileWriter fw = new FileWriter(file, true);
			BufferedWriter bw = new BufferedWriter(fw)) {
			bw.write(result);
			bw.write(System.lineSeparator());
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static File[] getInspectionFiles(String empId) {
		File dir = new File("../" + empId);
		if(dir.exists()) {
			File[] listFiles = dir.listFiles();
			return listFiles;
		}
		return new File[]{};
	}
	
	public static void backupInspectionFiles(String empId) {
		File[] inspectionFiles = getInspectionFiles(empId);
		File backupDir = new File("../BACKUP");
		if(!backupDir.exists()) {
			backupDir.mkdirs();
		}
		for(File originFile: inspectionFiles) {
			File targetFile = new File(backupDir, originFile.getName());
			moveFile(originFile, targetFile);
		}
	}
	
	public static void backupInspectionFile(File originFile) {
		File backupDir = new File("../BACKUP");
		if(!backupDir.exists()) {
			backupDir.mkdirs();
		}
		File targetFile = new File(backupDir, originFile.getName());
		moveFile(originFile, targetFile);
	}
	
	private static void moveFile(File original, File target) {
		byte[] buffer = new byte[1024];
		try (FileInputStream fis = new FileInputStream(original);
				FileOutputStream fos = new FileOutputStream(target)) {
			int readLength = 0;
			while((readLength = fis.read(buffer)) != -1) {
				fos.write(buffer, 0, readLength);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//remove original
		original.delete();
	}
}
