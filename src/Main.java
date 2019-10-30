import java.io.BufferedReader;
import java.io.FileReader;

public class Main {

	public static void main(String args[]) {
		if(args.length == 0) {
			System.out.println("Enter in File name");
			System.exit(-1);
		}
		try {
		BufferedReader in = new BufferedReader(new FileReader(args[0]));
		LexicalAnalyzer lexAn = new LexicalAnalyzer(in);
		
		lexAn.loadKeywords();
		
		lexAn.runLex();
		
		} catch(Exception e) {
			System.out.println("File not found");
		}
	}
	
}
