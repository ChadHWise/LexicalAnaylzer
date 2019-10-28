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
	public static final int RPAREM = 106;
	public static final int LBRAC = 107;
	public static final int RBRAC = 108;
	public static final int SEMI = 109;
	public static final int RELOP = 110;
	public static final int KEYWORD = 111;

	HashMap<String, Boolean> table = new HashMap<String, Boolean>();

	public static void main(String[] args) throws IOException {
		LexicalAnalyzer lexAn = new LexicalAnalyzer();
		BufferedReader in = new BufferedReader(new FileReader("test.txt"));
		char[] source = new char[100];
		lexAn.loadKeywords();
		int lineNo = 1;
		int state = 0;
		int forwardP = 0;
//		Token total = lex.getToken("total");
//		Token price = lex.getToken("price");
//		Token key = lex.getToken("if");
//
//		System.out.println(total.lexeme + " " + total.tokenType);
//		System.out.println(price.lexeme + " " + price.tokenType);
//		System.out.println(key.lexeme + " " + key.tokenType);

		String nextLine = in.readLine();
		ArrayList<Token> tokens = new ArrayList<Token>();

		while (nextLine != null) {
			source = nextLine.toCharArray();
			int lexStart = 0;
			char forward = source[0];
			for (int i = 0; i < source.length; i++) {
				forwardP++;
				switch (state) {
				case 0:
					if (Character.isAlphabetic(forward)) {
						state = 1;
						break;
					} else if (Character.isDigit(forward)) {
						state = 3;
						break;
					} else {
						switch (forward) {
						case ' ':
							break;
						case '\t':
							break;
						case '=':
							state = 5;
							break;
						case '!':
							state = 8;
							break;
						case '<':
							state = 11;
							break;
						case '>':
							state = 14;
							break;
						case '+':
							state = 17;
							break;
						case '-':
							state = 18;
							break;
						case '*':
							state = 19;
							break;
						case '/':
							state = 20;
							break;
						case '(':
							state = 21;
							break;
						case ')':
							state = 22;
							break;
						case '{':
							state = 23;
						case '}':
							state = 24;

						}
					}
				case 1:
					if (Character.isAlphabetic(forward) || Character.isDigit(forward)) {
						break;
					} else {
						state = 2;
						break;
					}
				case 2:
					String lex = "";
					for (int j = lexStart; j <= --forwardP; j++) {
						lex = lex + source[j];
					}
					tokens.add(lexAn.getToken(lex));
					System.out.println(lexAn.getToken(lex).toString());
				}
				forward = source[forwardP];
			}

			nextLine = in.readLine();
			lineNo++;
		}
		in.close();

	}

	public void loadKeywords() {

		table.put("if", true);
		table.put("int", true);
		table.put("else", true);
		table.put("while", true);
		table.put("total", false);
	}

	public Token getToken(String value) {

		if (table.get(value) == null) {
			table.put(value, false);
			return getToken(value);
		} else if (table.get(value)) {
			return new Token(111, value);
		} else {
			return new Token(100, value);
		}
	}

	public class Token {

		int tokenType;
		String lexeme;

		public Token(int tokenType, String lexeme) {
			this.tokenType = tokenType;
			this.lexeme = lexeme;
		}

		public String toString() {
			return this.tokenType + " " + this.lexeme;
		}

	}
}
