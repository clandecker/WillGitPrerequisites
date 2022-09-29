import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
import java.util.ArrayList;
import java.util.Formatter;

//change commit to create a tree object
//remove pTree from constructor 
public class Commit {
	ArrayList<String> treeContents=new ArrayList<String>();
	
	private String nextPointer = "";
	private String previousPointer = "";
	private Tree currentTree;
	
	private String summary = null;
	private String author = null;
	private String date = null;

	
	public static void main (String [] args) throws IOException, NoSuchAlgorithmException {
		Commit com1=new Commit( "summary 1", "Casey Landecker", "");		
		Commit com2=new Commit( "summary 2", "Casey Landecker", "cf37aaabc98dee8b19722ac3099f29aae1179f74");
		//Commit com3=new Commit( "summary 3", "Casey Landecker", "86e928c992896dfc55e4b67b31b96acfa23a36d7");
	}
	
	public Commit( String summary, String author, String previousPointer) throws IOException, NoSuchAlgorithmException {
		//Initialize all variables
		
		this.summary = summary;
		this.author = author;
		this.date = getDate();
		this.previousPointer = previousPointer;
		
		
		//change parent file if needed 		
		if (!previousPointer.equals("")) {
			changeParentFile(previousPointer);
		}		
		//create tree
		setTreeContents();
		currentTree=new Tree(treeContents);	
		
		//generateCommit 
		generateFile();
		
		//clear Index
		clearIndex();
		
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
	
	public void generateFile() throws IOException, NoSuchAlgorithmException {
		String fileName = getSha1(sha1Contents());
		File file = new File("./objects/" + fileName);
		file.createNewFile();
		PrintWriter writer = new PrintWriter("./objects/" + fileName);
		writer.println(currentTree.filename()); //gets currentTree filename for first line
		if (!previousPointer.equals("")) {
			writer.println("objects/"+previousPointer);
		}
		else {
			writer.println();
		}		
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
	
	public void setTreeContents() throws IOException{
		getIndexContents();	    
	    //adds previousTree if one exists
	    if (!previousPointer.equals("")) {
	    	treeContents.add("tree : "+ getPreviousTree(previousPointer));
	    }
	    
 	}
	
	public void getIndexContents() throws IOException {
		//adds in blobs from index
				BufferedReader buff=new BufferedReader(new FileReader("index.txt"));
				String indexLine;
			    while ((indexLine = buff.readLine()) != null) {
			    	String fileName="";
			    	int i=0;
			    	//while you are still reading in the file name
			    	while(!indexLine.substring(i,i+3).equals(" : ")) {	
			    		fileName+=indexLine.charAt(i);
			    		i++;
			    	}
			    	
			    	//getting fileHash from index
			    	String fileHash=indexLine.substring(i+3);
			    	//creating correctly formatted treeLine
			    	String treeLine="blob : "+fileHash+ " "+ fileName;
			    	System.out.println(treeLine+ "\n");
			    	//adds treeLine to treeContents
			    	treeContents.add(treeLine);	 
			    				    	
			    }
			    buff.close();
	}
	
	public String getPreviousTree(String fileName) throws IOException {
		BufferedReader buff=new BufferedReader(new FileReader("objects/"+previousPointer));
		String previousTreeSha=buff.readLine();
		buff.close();
		return previousTreeSha; 		
	}
	
	public void clearIndex() throws IOException {
		File file = new File("index.txt");
		file.delete();
		file.createNewFile();
	}
		
	
	
}
