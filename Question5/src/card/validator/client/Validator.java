package card.validator.client;

import java.text.ParseException;

import card.validator.utils.CardUtility;

public class Validator {

	private String empId;
	private String busId;
	private String time;
	
	public Validator(String empId, String busId, String time) {
		this.empId = empId;
		this.busId = busId;
		this.time = time;
	}
	
	//[카드ID(8)][버스ID(7)][승차/하차 코드(1)][최근 승차시각(14)] 
	// ex) CARD_001BUS_001N20171019093610
	public void inspection(String cardData) {
		String currentBusId = cardData.substring(8,15);
		String code = cardData.substring(15,16);
		String latestTime = cardData.substring(16);
		
		String currTime = CardUtility.getCurrentDateTimeString();
		
		//판단
		String resultCode = "R1";
		if(!this.busId.equals(currentBusId)) {
			//check bus id
			resultCode = "R2";
		} else if(!code.equals("N")) {
			//check code
			resultCode = "R3";
		} else {
			//check time
			try {
				long gap = CardUtility.hourDiff(currTime, latestTime);
				if(gap > 3L) {
					resultCode = "R4";
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		//result message
		//[검사원ID]#[버스ID]#[카드데이터]#[검사 결과 코드]#[검사시각]
		//ex) INSP_001#BUS_001# CARD_001BUS_001N20171019093610#R1#20171023164905
		StringBuffer sb = new StringBuffer();
		sb.append(this.empId).append("#").append(this.busId).append("#")
			.append(cardData).append("#").append(resultCode).append("#").append(currTime);
		
		FileManager.saveInspectionResult(this.empId, this.time, sb.toString());
		
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getBusId() {
		return busId;
	}

	public void setBusId(String busId) {
		this.busId = busId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
