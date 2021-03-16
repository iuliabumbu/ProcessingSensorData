package fileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileOperations {
	
	public static OutputStream deschidereFisier(Path path) {
		OutputStream output = null;

		try {
			output = Files.newOutputStream(path);
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		return output;

	}	

	public static void scriereFisier(OutputStream output,String s) {

		byte[] strToBytes = s.getBytes();
		try {
			output.write(strToBytes);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public static void inchidereFisier(OutputStream output) {
		try {
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
