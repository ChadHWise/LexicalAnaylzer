import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Main class used to run program
 * 
 * @author Chad & Tom
 *
 */
public class Main {

	/*
	 * Main method Creates a lexical analyzer passing in a buffered reader with the
	 * desired path of the file
	 * 
	 */
	public static void main(String args[]) {

		// If no run parameters are entered program will not run and ask for a file name
		// to be entered
		if (args.length == 0) {
			System.out.println("Enter in File name into run arguments");
			System.exit(-1);
		}

		// Will throw an exception if file is not found and alert user
		try {
			BufferedReader in = new BufferedReader(new FileReader(args[0]));
			LexicalAnalyzer lexAn = new LexicalAnalyzer(in);

			lexAn.loadKeywords();

			lexAn.runLex();

		} catch (Exception e) {
			System.out.println("File not found");
		}
	}

}
