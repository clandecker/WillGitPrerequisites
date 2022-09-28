import java.util.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.*;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Tree {
	ArrayList <String> list;
	String s;
	
	
	public Tree(ArrayList<String> list) throws NoSuchAlgorithmException, IOException {
		this.list = list;
		s = filename();
		writeToFile(s);
	}
	
	
	public String listToString() {
		String temp = "";
		for (int i = 0; i < list.size(); i++) {
			temp+= list.get(i);
			if(i!=list.size()-1) {
				temp+="\n";
			}
		}
		return temp;
	}
	
	public String filename() throws NoSuchAlgorithmException, IOException {
		System.out.println("list:\n"+listToString());
		return sha1Code(listToString());
	}
	
	public String sha1Code(String st) throws IOException {
		String str=st;
		String SHA="";
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
	        digest.reset();
	        digest.update(str.getBytes("utf8"));
	        SHA = String.format("%040x", new BigInteger(1, digest.digest()));
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return SHA;
		
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
			File file = new File("objects/" + s);
		    BufferedWriter bf = null;
	        try {
	            bf = new BufferedWriter(new FileWriter(file));
	            for (int i = 0; i < list.size(); i++) {
	                bf.write(list.get(i));
	                if (i != list.size() - 1) {
	                	bf.newLine();
	            	}
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
	 
	 public static void main(String[]args) throws NoSuchAlgorithmException, IOException, FileNotFoundException {
			ArrayList <String> listy = new ArrayList<String>();
			listy.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f");
			listy.add("blob : 01d82591292494afd1602d175e165f94992f6f5f");
			listy.add("blob : f1d82236ab908c86ed095023b1d2e6ddf78a6d83");
			listy.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
			listy.add("tree : e7d79898d3342fd15daf6ec36f4cb095b52fd976");
			Tree tree = new Tree(listy);
			//File file = new File("objects/dd4840f48a74c1f97437b515101c66834b59b1be");
		 
	 }
	 
	 
	
}
