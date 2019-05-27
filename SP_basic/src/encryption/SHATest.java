package encryption;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHATest {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		String text = "This is a Base64 test.";
		System.out.println(getSHA256(text));
		
	}
	
	private static String getSHA256(String input) throws NoSuchAlgorithmException {
		MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
		byte[] digest = mDigest.digest(input.getBytes());
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<digest.length; i++) {
			sb.append(Integer.toString((digest[i] & 0xFF) + 0x100, 16).substring(1));
		}
		
		return sb.toString();
	}

}
