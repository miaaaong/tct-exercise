package card.validator.server;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class CardServerLauncher {
    public static void main(String[] args) throws IOException, InterruptedException {
	    SocketServer socketServer = new SocketServer();
	    Thread thread = new Thread(socketServer);
	    thread.start();
	    
	    Scanner scan = new Scanner(System.in);
	    String input = "";
	    while(!input.equals("QUIT")) {
	    	input = scan.nextLine();
	    	if(input.equals("REPORT")) {
	    		Report.analyze();
	    		System.out.println("REPORT FINISH");
	    	} else if(!input.equals("QUIT")) {
	    		SimpleDateFormat dayTime = new SimpleDateFormat("yyyyMMdd");
	    		try {
					Date date = dayTime.parse(input);
					String day = dayTime.format(date);
					if(input.endsWith(" C")) {
						Report.printSortedReport(day);
					} else {
						Report.printReport(day);
					}
				} catch (ParseException e) {
					//입력값이 날짜가 아님
					e.printStackTrace();
					continue;
				}
	    	}
	    }
	    
	    //quit
	    socketServer.stopListener();
    }
}