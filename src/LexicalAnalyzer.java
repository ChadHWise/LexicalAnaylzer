import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
		LexicalAnalyzer lex = new LexicalAnalyzer();
		BufferedReader in = new BufferedReader(new FileReader("test.txt"));
		char[] source = new char[100];
		lex.loadKeywords();
		int lineNo = 0;
		int state = 0;

		Token total = lex.getToken("total");
		lex.installId("price");
		Token price = lex.getToken("price");
		Token key = lex.getToken("if");

		System.out.println(total.lexeme + " " + total.tokenType);
		System.out.println(price.lexeme + " " + price.tokenType);
		System.out.println(key.lexeme + " " + key.tokenType);

		String nextLine = in.readLine();
		while (nextLine != null) {
			lineNo++;
			source = nextLine.toCharArray();
			for(int i = 0; i < source.length; i++) {
				System.out.print(source[i]);
			}
			
			
			
			nextLine = in.readLine();
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
		
		
		if(table.get(value) == null) {
			return null;
		}else if (table.get(value)) {
			return new Token(100, value);
		} else {
			return new Token(111, value);
		} 
	}

	public void installId(String value) {
		if (table.get(value) == null) {
			table.put(value, false);
		}
	}

	public class Token {

		int tokenType;
		String lexeme;

		public Token(int tokenType, String lexeme) {
			this.tokenType = tokenType;
			this.lexeme = lexeme;
		}

	}
}
