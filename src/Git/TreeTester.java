package Git;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TreeTester {
	@BeforeAll
	static void setUpBeforeClass() throws IOException{
        Path p = Paths.get("junit.txt");
        try {
            Files.writeString(p, "Will Tester", StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	@AfterAll
	
	static void tearDownAfterClass() throws IOException{
		File f = new File("junit.txt");
		f.delete();
	}
	
	@Test
	void testTree() throws NoSuchAlgorithmException, IOException {
		ArrayList <String> listy = new ArrayList<String>();
		listy.add("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f");
		listy.add("blob : 01d82591292494afd1602d175e165f94992f6f5f");
		listy.add("blob : f1d82236ab908c86ed095023b1d2e6ddf78a6d83");
		listy.add("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b");
		listy.add("tree : e7d79898d3342fd15daf6ec36f4cb095b52fd976");
		Tree tree = new Tree(listy);
		File file = new File("objects/dd4840f48a74c1f97437b515101c66834b59b1be");
		assertTrue(file.exists());
		Scanner s = new Scanner(file);
		assertTrue(s.nextLine().equals("blob : 81e0268c84067377a0a1fdfb5cc996c93f6dcf9f"));
		assertTrue(s.nextLine().equals("blob : 01d82591292494afd1602d175e165f94992f6f5f"));
		assertTrue(s.nextLine().equals("blob : f1d82236ab908c86ed095023b1d2e6ddf78a6d83"));
		assertTrue(s.nextLine().equals("tree : bd1ccec139dead5ee0d8c3a0499b42a7d43ac44b"));
		assertTrue(s.nextLine().equals("tree : e7d79898d3342fd15daf6ec36f4cb095b52fd976"));
		file.delete();


	}

}
