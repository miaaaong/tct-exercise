package file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileIO {

	public static void main(String[] args) {
		String base = System.getProperty("user.dir") + File.separator + "data";
		String path = base + File.separator + "INPUT";
		File root = new File(path);
		
		String outputPath = base + File.separator + "OUTPUT";
		File target = new File(outputPath);
		if(!target.exists()) {
			target.mkdirs();
		}
		
		int bfsize = 512;
		File[] listFiles = root.listFiles();
		for(File child : listFiles) {
			System.out.println(child.getName() + ", " + child.length());
			if(child.length() > 2048L) {
				String newPath = outputPath + File.separator + child.getName();
				int readLength = 0;
				try (InputStream is = new FileInputStream(child);
						OutputStream os = new FileOutputStream(newPath)) {
					byte[] buffer = new byte[bfsize];
					while((readLength = is.read(buffer)) != -1) {
						os.write(buffer, 0, readLength);
					}
					
					is.close();
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
