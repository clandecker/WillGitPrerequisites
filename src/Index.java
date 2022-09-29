import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Index {
	private static HashMap<String, String> hashMap;

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		Index myGit = new Index();
		myGit.add("test1.txt");
		myGit.add("test2.txt");
	}
	
	public Index () throws IOException {
		hashMap = new HashMap<String, String>();
		init();	
	}
	
	public static void clearMap() {
		hashMap=new HashMap<String, String>();
	}
	
	public void init () throws IOException {
		createFile();
		createDirectory();
	}
	
	public void createFile() throws IOException {
		File file = new File("index.txt");
		file.createNewFile();
	}
	
	public void createDirectory() {
		new File("/objects/").mkdirs();
	}

	public void add(String fileName) throws NoSuchAlgorithmException, IOException {
		Blob toAdd = new Blob(fileName);
		hashMap.put(fileName, toAdd.sha1Code(fileName));
		updateIndexFile();
	}
	
	public void remove(String fileName) throws FileNotFoundException {
		String sha1 = hashMap.get(fileName);
		hashMap.remove(fileName);
		deleteFile("objects/" + sha1);
		updateIndexFile();
	}
	
	public void deleteFile(String fileName) {
		File file = new File(fileName); 
	    file.delete();
	}
	
	public void updateIndexFile() throws FileNotFoundException {
		PrintWriter writer = new PrintWriter(System.getProperty("user.dir") + "/index.txt");
		for (Map.Entry<String, String> entry : hashMap.entrySet()) {
		    writer.println(entry.getKey() + " : " + entry.getValue());
		}
		writer.close();
	}
	
	
}
