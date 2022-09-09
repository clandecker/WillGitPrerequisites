import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Blob {
	
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        Blob fileHash = new Blob("/Users/willsherwood/eclipse-workspace/GitPrerequisites/test.txt");
	}
	
	public Blob (String fileName) throws NoSuchAlgorithmException, IOException {
//		zipFile(fileName);
//		fileName = fileName + ".zip";
		generateFile(fileName, sha1Code(fileName));
	}
	
	public void generateFile(String fileName, String sha1) throws IOException {
		File source = new File(fileName);
		File dest = new File("objects/" + sha1);
		copyFileUsingStream(source, dest);
	}
	
	public String sha1Code(String fileName) throws IOException, NoSuchAlgorithmException {
        FileInputStream fileInputStream = new FileInputStream(fileName);
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        DigestInputStream digestInputStream = new DigestInputStream(fileInputStream, digest);
        byte[] bytes = new byte[1024];
        // read all file content
        while (digestInputStream.read(bytes) > 0);

//        digest = digestInputStream.getMessageDigest();
        digestInputStream.close();
        byte[] resultByteArry = digest.digest();
        return bytesToHexString(resultByteArry);
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
	 
	 private static void copyFileUsingStream(File source, File dest) throws IOException {
		    InputStream is = null;
		    OutputStream os = null;
		    try {
		        is = new FileInputStream(source);
		        os = new FileOutputStream(dest);
		        byte[] buffer = new byte[1024];
		        int length;
		        while ((length = is.read(buffer)) > 0) {
		            os.write(buffer, 0, length);
		        }
		    }
		    finally {
		    	is.close();
		        os.close();
		    }
		}
	 
//	public void zipFile(String fileName) throws IOException {
//			StringBuilder sb = new StringBuilder();
//			sb.append("Test String");
//	
//			File file = new File(fileName + ".zip");
//			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(file));
//			ZipEntry e = new ZipEntry(fileName);
//			out.putNextEntry(e);
//	
//			byte[] data = sb.toString().getBytes();
//			out.write(data, 0, data.length);
//			out.closeEntry();
//	
//			out.close();
//	}
}
