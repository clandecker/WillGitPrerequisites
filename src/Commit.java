import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

public class Commit {
	private Commit nextPointer = null;
	private Commit previousPointer = null;
	private String pTree = null;
	
	private String summary = null;
	private String author = null;
	private String date = null;
	
	public static void main (String [] args) throws IOException {
		Commit commit1 = new Commit("./objects/59c4dd553b054c2028eb5179b3d2c3238f9ae84a", "Booblah", "WillSherwood", null);
		commit1.generateFile();
		Commit commit2 = new Commit("./objects/59c4de553b054c2028eb5179b3d2c3238f9ae84a", "Welcome", "Charles", commit1);
		commit2.generateFile();
		commit1.generateFile();
	}
	
	public Commit(String pTree, String summary, String author, Commit previousPointer) {
		this.pTree = pTree;
		this.summary = summary;
		this.author = author;
		this.date = getDate();
		
		this.previousPointer = previousPointer;
		if (previousPointer != null) {
			this.previousPointer.setNextPointer(this);
		}
	}
	
	public void setNextPointer(Commit newNextPointer) {
		nextPointer = newNextPointer;
	}
	
	public String getDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
		LocalDateTime now = LocalDateTime.now();  
		return dtf.format(now);  
	}
	
	private static String getSha1(String starter)
	{
	    String sha1 = "";
	    try
	    {
	        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	        crypt.reset();
	        crypt.update(starter.getBytes("UTF-8"));
	        sha1 = byteToHex(crypt.digest());
	    }
	    catch(NoSuchAlgorithmException e)
	    {
	        e.printStackTrace();
	    }
	    catch(UnsupportedEncodingException e)
	    {
	        e.printStackTrace();
	    }
	    return sha1;
	}
	
	public void generateFile() throws IOException {
		String combo = summary + date + author + previousPointer;
		String fileName = getSha1(combo);
		File file = new File("./objects/" + fileName);
		file.createNewFile();
		PrintWriter writer = new PrintWriter("./objects/" + fileName);
		writer.println(pTree);
		writer.println(previousPointer);
		writer.println(nextPointer);
		writer.println(author);
		writer.println(date);
		writer.println(summary);
		writer.close();
	}

	private static String byteToHex(final byte[] hash)
	{
	    Formatter formatter = new Formatter();
	    for (byte b : hash)
	    {
	        formatter.format("%02x", b);
	    }
	    String result = formatter.toString();
	    formatter.close();
	    return result;
	}
}
