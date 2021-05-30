package sample.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCalculation {

	// 문자형 날짜에 시간 더하기
	private String getTimeString(String time, int sec) {
		// HH는 24시간 표기, hh는 12시간 기준에 am/pm 추가 표기 
		// MM은 월, mm은 분으로 주의
		SimpleDateFormat formatter = new SimpleDateFormat ("yyyy.MM.dd HH:mm:ss");
		String timeString = null;
		try {
			Date targetDate = formatter.parse(time);
			long arrivalTime = targetDate.getTime() + sec*1000;
			timeString = formatter.format(new Date(arrivalTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timeString;
	}
	
	// 문자형 날짜 사이 간격 계산
	private int getTimeGap(String busTime, String targetTime) {
		SimpleDateFormat formatter = new SimpleDateFormat ("HH:mm:ss");
		int gap = 0;
		try {
			Date busDate = formatter.parse(busTime);
			Date targetDate = formatter.parse(targetTime);
			gap = (int) ((targetDate.getTime() - busDate.getTime())/1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return gap;
	}
}
