
public class Token {
	
	int tokenType;
	String lexeme;
	
	public Token(int tokenType, String lexeme) {
		super();
		this.tokenType = tokenType;
		this.lexeme = lexeme;
	}
	
	public String toString() {
		return this.tokenType + " " + lexeme;
	}
}
