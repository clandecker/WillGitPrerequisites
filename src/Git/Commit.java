package Git;
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
	//array of all blobs you need to add to tree if you delete a file
	ArrayList<String> pastBlobs=new ArrayList<String>();
	
	private String nextPointer = "";
	private String previousPointer = "";
	private Tree currentTree;
	
	private String summary = null;
	private String author = null;
	private String date = null;
	
	boolean filesWereDeleted=false;

	public static void main (String [] args) throws IOException, NoSuchAlgorithmException {
		Index index=new Index();
		//commit #1
		index.add("test1.txt");
		index.add("test2.txt");	
		//index.addEditedFile("edited file");
		Commit com1=new Commit( "commit 1", "Casey Landecker");
		
		
		//commit #2
		index.add("test3.txt");	
		//index.addEditedFile("edited file2");
		//index.addDeletedFile("test2.txt");
		Commit com2=new Commit( "commit 2", "Casey Landecker");
		
		//commit #3
		index.add("test4.txt");
		index.add("test5.txt");
		Commit com3=new Commit( "commit 3", "Casey Landecker");
		
		//commit #4
		index.add("test6.txt");
		Commit com4=new Commit("commit 4", "Casey Landecker");
		
	}
	
	public Commit( String summary, String author) throws IOException, NoSuchAlgorithmException {
		//Initialize all variables
		
		this.summary = summary;
		this.author = author;
		this.date = getDate();
		if (hasParentCommit()) {
			previousPointer=getParentCommit();
			changeParentFile(previousPointer);
		}
				
		//create tree
		setTreeContents();
		currentTree=new Tree(treeContents);	
		System.out.println("pastBlobs:\n");
		for (int i=0; i<pastBlobs.size();i++) {
			System.out.println (pastBlobs.get(i)+"\n");
		}
		
		
		
		//generateCommit 
		generateFile();
		
		//change Head
		changeHead();
		
		//clear Index
		Index.clearMap();
		clearIndex();		
		
	}
	
	public boolean hasParentCommit() throws IOException {
		BufferedReader buff=new BufferedReader(new FileReader("HEAD"));
		String line;
		if ((line = buff.readLine()) != null) {
			return true;	
		}
		return false;	
	}
	
	public String getParentCommit() throws IOException {
		BufferedReader buff=new BufferedReader(new FileReader("HEAD"));
		String line;
		line = buff.readLine();
		return line;	
	}
	
	
	public void changeHead() throws FileNotFoundException {
		File file=new File("HEAD");
		file.delete();
		Path p = Paths.get("HEAD");
        try {
            Files.writeString(p, getSha1(sha1Contents()), StandardCharsets.ISO_8859_1);
            System.out.println(getSha1(sha1Contents()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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
	
	public void addPreviousTreeToTreeContents() throws IOException{
		if (!previousPointer.equals("")) {
	    	treeContents.add("tree : "+ getPreviousTree());
	    }				
	}
	
	public void addPastBlobsToTreeContents() {
		for (int i=0; i<pastBlobs.size(); i++) {
			treeContents.add(pastBlobs.get(i));
			System.out.println("past blobs: " + pastBlobs.get(i));
		}
	}
	
	public void setTreeContents() throws IOException{
		//adds contents from index
		addIndexToTreeContents();	 

	    //adds previousTree if one exists
		if (filesWereDeleted==false) {
			addPreviousTreeToTreeContents();
		}

		    
 	}
	
	
	//must handle deleting and editing separately
	//create new method that edits deleted if it sees that the index entry starts with *
	public void addIndexToTreeContents() throws IOException {
		//adds in blobs from index
				BufferedReader buff=new BufferedReader(new FileReader("index.txt"));
				String indexLine;
			    while ((indexLine = buff.readLine()) != null) {
			    	
			    	//if you are deleting a file
			    	if ((indexLine.charAt(0)=='*')) {
			    		if((indexLine.charAt(1)=='d')) {
			    			//adds old stuff to old stuff array
			    			checkTreeForBlob(getPreviousTree(), indexLine.substring(10));
			    			System.out.println("checking tree for blob");
			    			//adds old stuff array to treeContents array
			    			addPastBlobsToTreeContents();
			    			//changes boolean c
			    			filesWereDeleted=true;
			    		}			    		
			    	}
			    	
			    	//if you are just at a blob 
			    	else {
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

			    	//adds treeLine to treeContents
			    	treeContents.add(treeLine);	 
			    	}
			    				    	
			    }
			    buff.close();
	}
	
	
	//"deletes" a files
	//THE FILENAME IS WRONG
	public boolean checkTreeForBlob(String tree, String fileName) throws IOException {
		System.out.println("fileName: "+ fileName);
		BufferedReader buff=new BufferedReader(new FileReader("objects/"+tree));
		String treeLine;
		while ((treeLine = buff.readLine())!= null) {
			System.out.println("running while loop");
			if (treeLine.substring(0,4).equals("blob") && !treeLine.contains(fileName)) {
				pastBlobs.add(treeLine);
				System.out.println("adding to pastBlobs");
			}
			if (treeLine.contains(fileName)) {
				if (!getPreviousTreeFromTree(tree).equals("no previous tree")) {
					//yup, pastBlobs can also have a tree
					pastBlobs.add(getPreviousTreeFromTree(tree));
					System.out.println("this line should not be printing");
				}				
				return true;					
			}
			//if u are at a blob but no the one you want, add to pastBlobs
			//ERROR IS SOMEWHERE HERE
			
			if(treeLine.substring(0,4).equals("tree")) {// THIS IS NOT THE ERROR
				checkTreeForBlob(treeLine.substring(7,47), fileName);
				System.out.println("trying to go into a past tree");
			}
			
		}
		buff.close();
		return false;		
	}
	
	
	
	
	
	//only works for this specific commit
	public String getPreviousTree() throws IOException {
		BufferedReader buff=new BufferedReader(new FileReader("objects/"+previousPointer));
		String previousTreeSha=buff.readLine();
		buff.close();
		return previousTreeSha; 		
	}
	
	//if u have any commit sha get previous tree
	public String getPreviousTreeFromTree(String treeSHA) throws IOException {
		BufferedReader buff=new BufferedReader(new FileReader("objects/"+treeSHA));
		String treeLine;
		while ((treeLine = buff.readLine()) != null) {
			if(treeLine.charAt(0)==('t')) {
				return treeLine;
			}
		}
		return "no previous tree";
	}
		
	
	public void clearIndex() throws IOException {
		File file = new File("index.txt");
		file.delete();
		file.createNewFile();
	}
			
	
	
}
