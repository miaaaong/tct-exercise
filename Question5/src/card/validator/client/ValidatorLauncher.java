package card.validator.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import card.validator.utils.CardUtility;

public class ValidatorLauncher {
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		//검사원 정보 로드
		Map<String, String> userInfo = loadUserInfo();

		Scanner scan = new Scanner(System.in);
		String input = "";
		
		//현재 validator의 검사원 id
		String empId = null;
		
		//login
		boolean isValid = false;
		while (!isValid) {
			input = scan.nextLine();
			String[] split = input.split(" ");
			String id = split[0];
			String pw = CardUtility.passwordEncryption(split[1]);

			if (userInfo.containsKey(id)) {
				String realPw = userInfo.get(id);
				if (realPw.equals(pw)) {
					isValid = true;
					empId = id;
				}
			}

			if (isValid) {
				System.out.println("LOGIN SUCCESS");
			} else {
				System.out.println("LOGIN FAIL");
			}
		}
		
		//login 성공
		
		while(!input.equals("LOGOUT")) {
			input = scan.nextLine();
			if(!input.equals("LOGOUT")) {
				// 검사 시작
				
				//현재 버스
				String busId = input;
				String time = CardUtility.getCurrentDateTimeString();
				Validator validator = new Validator(empId, busId, time);
				
				while(!input.equals("DONE")) {
					input = scan.nextLine();
					if(!input.equals("DONE")) {
						//카드 판독
						String cardData = input;
						validator.inspection(cardData);
					}
				}
				
				//현재 버스에 대해 DONE
			}
		}
		
		//logout
		logout(empId);
	}
	
	private static void logout(String empId) {
		try {
			SocketClient.sendInspectionFiles(empId);
			FileManager.backupInspectionFiles(empId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static Map<String, String> loadUserInfo() {
		Map<String, String> map = new HashMap<String, String>();
		String path = "../CLIENT/INSPECTOR.TXT";
		try (FileReader fr = new FileReader(path);
				BufferedReader br = new BufferedReader(fr)) {
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] split = line.split(" ");
				String id = split[0];
				String pw = split[1];
				map.put(id, pw);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return map;
	}
}