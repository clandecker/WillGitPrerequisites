package Git;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class GitTester {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@Test
	void testCommit() throws IOException, NoSuchAlgorithmException {
	Index index=new Index();
	//commit #1
	index.add("test1.txt");
	index.add("test2.txt");	
	Commit com1=new Commit( "commit 1", "Casey Landecker");
	
	
	//commit #2
	index.add("test3.txt");	
	index.addDeletedFile("test1.txt");
	Commit com2=new Commit( "commit 2", "Casey Landecker");
	
	//commit #3
	index.add("test4.txt");
	index.add("test5.txt");
	Commit com3=new Commit( "commit 3", "Casey Landecker");
	
	//commit #4
	index.add("test6.txt");
	Commit com4=new Commit("commit 4", "Casey Landecker");
	
	//commit #5
	index.add("test7.txt");
	Commit com5=new Commit("commit 5", "Casey Landecker");
	
		
	}

}
