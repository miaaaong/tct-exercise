package card.validator.server.vo;

public class AnalysisResult implements Comparable<AnalysisResult> {
	private String empId;
	private Integer cardNum;
	private Integer invalidNum;

	@Override
	public int compareTo(AnalysisResult o) {
		return o.getCardNum().compareTo(cardNum);
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public Integer getCardNum() {
		return cardNum;
	}

	public void setCardNum(Integer cardNum) {
		this.cardNum = cardNum;
	}

	public Integer getInvalidNum() {
		return invalidNum;
	}

	public void setInvalidNum(Integer invalidNum) {
		this.invalidNum = invalidNum;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(empId).append(" ").append(cardNum).append(" ").append(invalidNum);
		return sb.toString();
	}
}
