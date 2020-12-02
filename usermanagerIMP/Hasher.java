package usermanagerIMP;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hasher {
	
	public static String hash(String input) {
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] bytesPassword = md.digest(input.getBytes(StandardCharsets.UTF_8));
			
			BigInteger no = new BigInteger(1, bytesPassword);
			
			String hashedPassword = no.toString(16);
			
			while(hashedPassword.length() < 32) {
				hashedPassword = "0" + hashedPassword;
			}
			
			return hashedPassword;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
