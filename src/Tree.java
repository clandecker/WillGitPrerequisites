import java.util.*;
import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Tree {
	String input;
	ArrayList <String> listy;
	String s;
	
	
	public Tree() throws NoSuchAlgorithmException, IOException {
		input = "";
		listy = new ArrayList<String>();
		s = "";
		writeToFile(filename());
	}
	
	
	public String listToString() {
		for (int i = 0; i < listy.size(); i++) {
			s+= listy.get(i) + "\n";
		}
		return s;
	}
	
	public String filename() throws NoSuchAlgorithmException, IOException {
		return sha1Code(s);
	}
	
	public String sha1Code(String fileName) throws IOException, NoSuchAlgorithmException {
        FileInputStream fileInputStream = new FileInputStream(fileName);
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        DigestInputStream digestInputStream = new DigestInputStream(fileInputStream, digest);
        byte[] bytes = new byte[1024];
        while (digestInputStream.read(bytes) > 0);
        digestInputStream.close();
        byte[] resultByteArry = digest.digest();
        return bytesToHexString(resultByteArry);
	}
    

	
	 public static String bytesToHexString(byte[] bytes) {
	        StringBuilder sb = new StringBuilder();
	        for (byte b : bytes) {
	            int value = b & 0xFF;
	            if (value < 16) {
	                sb.append("0");
	            }
	            sb.append(Integer.toHexString(value));
	        }
	        return sb.toString();
	 }
	 
	public void writeToFile (String filename) {
			File file = new File("Test/index.txt");
		    BufferedWriter bf = null;
	        try {
	            bf = new BufferedWriter(new FileWriter(file));
	            for (int i = 0; i < listy.size(); i++) {
	                bf.write(listy.get(i));
	                bf.newLine();
	            }
	              bf.flush();
	        }
	        catch (IOException e) {
	            e.printStackTrace();
	        }
	        finally {
	            try {
	                bf.close();
	            }
	            catch (Exception e) {
	            }
	        }
		}
	
}
