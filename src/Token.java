/**
 * Class defining a Token
 * 
 * @author Chad & Tom
 *
 */
public class Token {

	int tokenType; // Identifies the type of the Token
	String lexeme;

	/*
	 * Main constructor that builds a token
	 */
	public Token(int tokenType, String lexeme) {
		super();
		this.tokenType = tokenType;
		this.lexeme = lexeme;
	}

	/*
	 * Prints a tokens values in human readable form
	 */
	public String toString() {
		return this.tokenType + " " + lexeme;
	}
}
