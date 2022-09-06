import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class Index {
	private HashMap<String, String> hashMap;

	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		Index myGit = new Index();
		myGit.add("/Users/willsherwood/eclipse-workspace/GitPrerequisites/test.txt");
	}
	
	public Index () throws IOException {
		hashMap = new HashMap<String, String>();
		init();	
	}
	
	public void init () throws IOException {
		createFile();
		createDirectory();
	}
	
	public void createFile() throws IOException {
		File file = new File(System.getProperty("user.dir") + "/index.txt");
		file.createNewFile();
	}
	
	public void createDirectory() {
		new File(System.getProperty("user.dir")+ "/objects/").mkdirs();
	}

	public void add(String fileName) throws NoSuchAlgorithmException, IOException {
		Blob toAdd = new Blob(fileName);
		System.out.println(fileName);
		System.out.println(toAdd.sha1Code(fileName));
		hashMap.put(fileName, toAdd.sha1Code(fileName));
		System.out.println(hashMap);
		updateIndexFile();
	}
	
	public void remove(String fileName) throws FileNotFoundException {
		hashMap.remove(fileName);
		deleteFile(fileName);
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
