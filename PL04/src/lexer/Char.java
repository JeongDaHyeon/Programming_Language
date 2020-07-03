package lexer;


class Char {
	private final char value;
	private final CharacterType type;

	enum CharacterType {
		LETTER, DIGIT, SPECIAL_CHAR, WS, END_OF_STREAM,
	}
	
	// Constructor of class Char
	static Char of(char ch) {
		return new Char(ch, getType(ch));
	}
	
	static Char end() {
		return new Char(Character.MIN_VALUE, CharacterType.END_OF_STREAM);
	}
	
	private Char(char ch, CharacterType type) {
		this.value = ch;
		this.type = type;
	}
	
	char value() {
		return this.value;
	}
	
	CharacterType type() {
		return this.type;
	}
	
	// ch : syllable
	private static CharacterType getType(char ch) {
		int code = (int)ch; // change Character to Integer
		 //letter가 되는 조건식을 알맞게 채우기 alphabet이나 question mark
		if ((code == '?') || Character.isLetter(ch)) { // ch가 alphabet이나 question mark일 때
			return CharacterType.LETTER;
		}
		
		if ( Character.isDigit(ch) ) { // ch가 숫자 일 때
			return CharacterType.DIGIT;
		}
		
		switch ( ch ) { // ch가 Special Character일 때
			case '-': case '+': case '*': case '/':
			case '(': case ')':
			case '<': case '=': case '>':
			case '#': case '\'':
				return CharacterType.SPECIAL_CHAR;
		}
		
		// Character이 공백일 때
		if ( Character.isWhitespace(ch) ) {
			return CharacterType.WS;
		}
		
		throw new IllegalArgumentException("input=" + ch);
	}
}
