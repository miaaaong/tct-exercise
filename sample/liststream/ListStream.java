package sample.liststream;

import java.util.ArrayList;
import java.util.List;

public class ListStream {
	
	public void getValue() {
		// max
		List<Data> dataList = new ArrayList<>();
		dataList.stream().mapToInt(Data::getMovingAvg).max().orElse(0);
		
		// average
		List<Integer> intList = new ArrayList<>();
		intList.stream().mapToInt(Integer::intValue).average().orElse(0.0);
	}

	private class Data {
		private String systemId;
		private int sysRate;
		private int movingAvg;

		public String getSystemId() {
			return systemId;
		}

		public void setSystemId(String systemId) {
			this.systemId = systemId;
		}

		public int getSysRate() {
			return sysRate;
		}

		public void setSysRate(int sysRate) {
			this.sysRate = sysRate;
		}

		public int getMovingAvg() {
			return movingAvg;
		}

		public void setMovingAvg(int movingAvg) {
			this.movingAvg = movingAvg;
		}

	}

}
