import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

//change commit to create a tree object
//remove pTree from constructor 
public class Commit {
	private String nextPointer = "";
	private String previousPointer = "";
	private String pTree = null;
	
	private String summary = null;
	private String author = null;
	private String date = null;

	
	public static void main (String [] args) throws IOException {
		Commit com1=new Commit("pTree", "summary 1", "Casey Landecker", "");
		Commit com2=new Commit("pTree2", "summary 2", "Casey Landecker", "e541de868790aa5aab328bcfb6071eb61689bddd");
		Commit com3=new Commit("pTree3", "summary 3", "Casey Landecker", "86e928c992896dfc55e4b67b31b96acfa23a36d7");
	}
	
	public Commit(String pTree, String summary, String author, String previousPointer) throws IOException {
		this.pTree = pTree;
		this.summary = summary;
		this.author = author;
		this.date = getDate();
		
		this.previousPointer = previousPointer;
		if (!previousPointer.equals("")) {
			changeParentFile(previousPointer);
		}
		
		generateFile();
	}
	
	public void changeParentFile(String par) throws IOException {
		BufferedReader buff=new BufferedReader(new FileReader("objects/"+par));
		//adding all old contents to a new string
		String parContents="";
		parContents+=buff.readLine()+"\n";
		parContents+=buff.readLine()+"\n";
		//changes third line to be the SHA1 of this current commit (its new child)
		parContents+="objects/"+getSha1(sha1Contents())+"\n";
		buff.readLine();
		parContents+=buff.readLine()+"\n";
		parContents+=buff.readLine()+"\n";
		parContents+=buff.readLine()+"\n";
		
		//rewriting the parent file w/ a child 
		Path p = Paths.get("objects/"+par);
        try {
            Files.writeString(p, parContents, StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }		
		
	}
	
	public String getDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
		LocalDateTime now = LocalDateTime.now();  
		return dtf.format(now);  
	}
	
	public String sha1Contents() {
		String contents="";
		if(!previousPointer.equals("")) {
			contents+=previousPointer+"\n";
		}
		contents+=author+"\n"+date+"\n"+ summary;
		return contents;
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
		String fileName = getSha1(sha1Contents());
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
