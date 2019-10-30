import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class defining a Lexical Analyzer holding a buffered reader and a symbol
 * table
 * 
 * @author Chad & Tom
 *
 */
public class LexicalAnalyzer {

	// Token types
	public static final int ID = 100;
	public static final int NUM = 101;
	public static final int ADDOP = 102;
	public static final int ASSIGNOP = 103;
	public static final int MULTOP = 104;
	public static final int LPAREN = 105;
	public static final int RPAREN = 106;
	public static final int LBRAC = 107;
	public static final int RBRAC = 108;
	public static final int SEMI = 109;
	public static final int RELOP = 110;
	public static final int KEYWORD = 111;
	public static final int INCOP = 112;

	// Symbol Table
	HashMap<String, Boolean> sTable = new HashMap<String, Boolean>();
	BufferedReader in;

	/*
	 * Constructor taking in a buffered reader
	 */
	public LexicalAnalyzer(BufferedReader in) {
		super();
		this.in = in;
	}

	/*
	 * Method that identifies lexemes in a source code
	 */
	public void runLex() throws IOException {
		int lineNo = 1; // Reference to line number current on in file
		String nextLine = in.readLine(); // Next line to be read
		ArrayList<Token> tokens = new ArrayList<Token>(); // Stores tokens as they appear in the file

		int state = 0; // Initializes start state to 0

		// Loops through the file by lines
		while (nextLine != null) {
			int forwardP = 0; // Index of the next char
			nextLine = nextLine + "   "; // Insures the char array ends in a space
			char[] source = new char[nextLine.length()]; // Allocates an array the size of the nextLine string
			source = nextLine.toCharArray(); // Breaks the new line into a char array
			int lexStart = 0; // Reference to the beginning of a lexeme
			char forward = source[0]; // Reference of next char in file initialized to first char of line
			String lex = ""; // Empty string to be added to

			// Loops over char array till forward is out of the array
			for (; forwardP < source.length - 1;) {

				// Determines the next state
				switch (state) {
				case 0:
					if (Character.isLetter(forward)) {
						state = 1;
						lexStart = forwardP;
						break;
					} else if (Character.isDigit(forward)) {
						state = 3;
						lexStart = forwardP;
						break;
					} else {
						switch (forward) {
						case ' ':
							lexStart++;
							break;
						case '\t':
							lexStart++;
							break;
						case '=':
							state = 5;
							break;
						case '!':
							state = 6;
							break;
						case '<':
							state = 7;
							break;
						case '>':
							state = 8;
							break;
						case '+':
							state = 9;
							break;
						case '-':
							state = 10;
							break;
						case '(':
							tokens.add(new Token(LPAREN, "("));
							state = 0;
							break;
						case ')':
							tokens.add(new Token(RPAREN, ")"));
							state = 0;
							break;
						case '{':
							tokens.add(new Token(LBRAC, "{"));
							state = 0;
							break;
						case '}':
							tokens.add(new Token(RBRAC, "}"));
							state = 0;
							break;
						case '*':
							tokens.add(new Token(MULTOP, "Mult"));
							state = 0;
							break;
						case '/':
							state = 11;
							break;
						case ';':
							tokens.add(new Token(SEMI, ";"));
							break;

						}
					}
					break;
				case 1:
					if (Character.isLetter(forward) || Character.isDigit(forward)) {
						break;
					} else {
						state = 2;
						break;
					}
				case 2:
					forwardP--;

					// Builds the Lexeme
					for (int j = lexStart; j < forwardP; j++) {
						lex = lex + source[j];
					}
					forwardP--; // Back up the forward pointer
					tokens.add(getToken(lex));
					lex = "";
					state = 0;
					break;
				case 3:
					if (Character.isDigit(forward)) {
						break;
					} else if (Character.isLetter(forward)) {

						// Builds the filed lexeme to show where error occured

						String faildLex = " ";
						for (int j = lexStart; j < forwardP + 1; j++) {
							faildLex = faildLex + source[j];
						}
						String failMsg = "Number values cannot contain letters: Line " + lineNo + faildLex;
						System.out.println(failMsg);
						for (int j = 1; j < failMsg.length(); j++) {
							System.out.print(" ");
						}
						System.out.println("^");
						System.exit(-1);
					} else {
						state = 4;
						break;
					}
				case 4:
					forwardP--;
					for (int j = lexStart; j < forwardP; j++) {
						lex = lex + source[j];
					}
					forwardP--;
					tokens.add(new Token(NUM, lex));
					lex = "";
					state = 0;
					break;
				case 5:
					if (forward == '=') {
						tokens.add(new Token(RELOP, "Is Equal To"));
						state = 0;
					} else {
						tokens.add(new Token(ASSIGNOP, "Equals"));
						forwardP--;
						state = 0;
					}
					break;
				case 6:
					if (forward == '=') {
						tokens.add(new Token(RELOP, "Not Equal To"));
						state = 0;
					} else {
						tokens.add(new Token(RELOP, "Not Value"));
						forwardP--;
						state = 0;
					}
					break;
				case 7:
					if (forward == '=') {
						tokens.add(new Token(RELOP, "Less Than or Equal to"));
						state = 0;
					} else {
						tokens.add(new Token(RELOP, "Less Than"));
						forwardP--;
						state = 0;
					}
					break;
				case 8:
					if (forward == '=') {
						tokens.add(new Token(RELOP, "Greater Than or Equal to"));
						state = 0;
					} else {
						tokens.add(new Token(RELOP, "Greater Than"));
						forwardP--;
						state = 0;
					}
					break;
				case 9:
					if (forward == '+') {
						tokens.add(new Token(INCOP, "Increment"));
						state = 0;
					} else {
						tokens.add(new Token(ADDOP, "Add"));
						forwardP--;
						state = 0;
					}
					break;
				case 10:
					if (forward == '-') {
						tokens.add(new Token(INCOP, "Decreament"));
						state = 0;
					} else {
						tokens.add(new Token(ADDOP, "Sub"));
						forwardP--;
						state = 0;
					}
					break;
				case 11:
					if (forward == '/') {
						// ignore line
						state = 0;
						forwardP = -1;
						nextLine = in.readLine();
						lineNo++;
						source = nextLine.toCharArray();
					} else if (forward == '*') {
						// Block comment started move states
						state = 12;
					} else {
						tokens.add(new Token(MULTOP, "Divide"));
						state = 0;
						forwardP--;
					}
					break;
				case 12:
					if (forward == '*') {
						// Look for start of end for the block comment
						state = 13;
					}
					// If not found move on
					break;
				case 13:
					if (forward == '/') {
						// End has been found start state to 0
						state = 0;
					} else {
						// End is not found look for the next *
						state = 12;
					}
					break;
				}

				forward = source[++forwardP];
			}

			nextLine = in.readLine();
			lineNo++;
		}
		in.close();

		for (int i = 0; i < tokens.size(); i++) {
			Token token = (Token) tokens.get(i);
			System.out.println(token.toString());
		}

	}

	/*
	 * Mehtod used to add the key words into the symbol table
	 */
	public void loadKeywords() {

		sTable.put("if", true);
		sTable.put("int", true);
		sTable.put("else", true);
		sTable.put("while", true);
		sTable.put("total", false);
	}

	/*
	 * Method that checks the symbol table for an ID and will: add new Token if it
	 * does not exist, return a token with a keyword value if the table returns a
	 * value of true, return a token with an ID value if the table returns a value
	 * of false
	 */
	public Token getToken(String value) {
		if (sTable.get(value) == null) {
			sTable.put(value, false);
			return getToken(value);
		} else if (sTable.get(value)) {
			return new Token(KEYWORD, value);
		} else {
			return new Token(ID, value);
		}
	}

}
