import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LexicalAnalyzer {

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
	
	
	HashMap<String, Boolean> sTable = new HashMap<String, Boolean>();
	
	BufferedReader in;
	
	
	public LexicalAnalyzer(BufferedReader in) {
		super();
		this.in = in;
	}

	public void runLex() throws IOException {
		BufferedReader in = new BufferedReader(new FileReader("test.txt"));
		int lineNo = 1;

		String nextLine = in.readLine();
		ArrayList<Token> tokens = new ArrayList<Token>();

		int state = 0;
		while (nextLine != null) {
			int forwardP = 0;
			nextLine = nextLine + "   ";
			char[] source = new char[nextLine.length() + 1];
			source = nextLine.toCharArray();
			int lexStart = 0;
			char forward = source[0];
			String lex = "";
			for (; forwardP < source.length - 1;) {
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
							tokens.add(new Token(SEMI,";"));
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
					for (int j = lexStart; j < forwardP; j++) {
						lex = lex + source[j];
					}
					forwardP--;
					tokens.add(getToken(lex));
					lex = "";
					state = 0;
					break;
				case 3:
					if (Character.isDigit(forward)) {
						break;
					} else if (Character.isLetter(forward)) {
						String faildLex = " ";
						for (int j = lexStart; j < forwardP + 1; j++) {
							faildLex = faildLex + source[j];
						}
						String failMsg = "Number values cannot contain letters: Line " + lineNo + faildLex;
						System.out.println(failMsg);
						for(int j = 1;j<failMsg.length();j++) {
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
					} else  {
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
						// start comment
						state = 12;
					} else {
						tokens.add(new Token(MULTOP, "Divide"));
						state = 0;
						forwardP--;
					}
					break;
				case 12:
					if(forward == '*') {
						state = 13;
					}
					break;
				case 13:
					if(forward =='/') {
						state = 0;
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

	public void loadKeywords() {

		sTable.put("if", true);
		sTable.put("int", true);
		sTable.put("else", true);
		sTable.put("while", true);
		sTable.put("total", false);
	}

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
