import java.io.File;
import java.io.IOException;


public class Main {

	public static void main(String[] args) throws IOException {
		String output = "output/";
		String input = "input/";
		// TODO Auto-generated method stub
		System.out.println(new File(".").getAbsolutePath());
		File test = new File("output/teststtt.txt");
		test.createNewFile();
		System.out.println(test.exists());
	}

}
