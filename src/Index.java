import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Index {
	private HashMap<Integer, Double> indexes;

	public static void main(String[] args) throws IOException {
		Index myGit = new Index();
	}
	
	public Index () throws IOException {
		indexes = new HashMap<Integer, Double>();
		init();	
	}
	
	public void init () throws IOException {
		createFile();
		createDirectory();
	}
	
	public void createFile() throws IOException {
		System.out.println("Trying to create file");
		File file = new File(System.getProperty("user.dir") + "/index.txt");
		file.createNewFile();
	}
	
	public void createDirectory() {
		System.out.println("Trying to create folder");
		new File(System.getProperty("user.dir")+ "/objects/").mkdirs();
	}

}
