package datastructure2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapTest {

//	private static Map<String, Map<String, Double>> map = new HashMap<String, Map<String,Double>>();
	private static Map<String, Map<String, Double>> map = new TreeMap<String, Map<String,Double>>();

	static final String NAME = "name";
	static final String TOTAL = "total";
	static final String PROJECT1 = "project1";
	static final String PROJECT2 = "project2";
	static final String PROJECT3 = "project3";
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		File file = new File("./DS_Sample2.csv");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line = "";
		while((line = br.readLine()) != null) {
			String[] split = line.split(",");
			String no = split[1];
			String name  = split[2];
			
			String empKey = no + "\t" + name;
			
			Double prj1 = Double.parseDouble(split[3]);
			Double prj2 = Double.parseDouble(split[4]);
			Double prj3 = Double.parseDouble(split[5]);
			
			Map<String, Double> data = map.get(empKey);
			if(data == null) {
				data = new HashMap<String, Double>();
				data.put(PROJECT1, 0d);
				data.put(PROJECT2, 0d);
				data.put(PROJECT3, 0d);
				data.put(TOTAL, 0d);
			}
			
			Double totalPrj1 = data.get(PROJECT1);
			totalPrj1 += prj1;
			data.put(PROJECT1, totalPrj1);
			
			Double totalPrj2 = data.get(PROJECT2);
			totalPrj2 += prj2;
			data.put(PROJECT2, totalPrj2);
			
			Double totalPrj3 = data.get(PROJECT3);
			totalPrj3 += prj3;
			data.put(PROJECT3, totalPrj3);
			
			Double total = prj1 + prj2 + prj3;
			Double all = data.get(TOTAL);
			all += total;
			data.put(TOTAL, all);
			
			map.put(empKey, data);

		}
		br.close();
		fr.close();
		
		for(String key : map.keySet()) {
			Map<String, Double> data = map.get(key);
			Double prj1 = data.get(PROJECT1);
			Double prj2 = data.get(PROJECT2);
			Double prj3 = data.get(PROJECT3);
			Double total = data.get(TOTAL);
			
			System.out.println(key + "\t" + getNumber(prj1) + "\t" + getNumber(prj2) + "\t" + getNumber(prj3) + "\t=>\t" + getNumber(total));
		}
	}
	
	private static double getNumber(Double value) {
		double b = Math.round(value*10d) / 10d;
		return b;
	}

}
