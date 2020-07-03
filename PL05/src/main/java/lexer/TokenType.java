package lexer;

public enum TokenType {
	INT, ID, TRUE, FALSE, NOT, PLUS, MINUS, TIMES, DIV, // special charactor
	LT, GT, EQ, APOSTROPHE, // special charactor
	L_PAREN, R_PAREN, QUESTION, // special charactor
	DEFINE, LAMBDA, COND, QUOTE, CAR, CDR, CONS, ATOM_Q, NULL_Q, EQ_Q;

	// special character에서만 채워야 함
	static TokenType fromSpecialCharactor(char ch) {
		// ch에 대해 enum을 리턴하는 함수
		switch (ch) {
		case '+':
			return PLUS;
		// 나머지 Special Character에 대해 토큰을 반환하도록 작성
		case '-':
			return MINUS;
		case '*':
			return TIMES;
		case '/':
			return DIV;
		case '<':
			return LT;
		case '=':
			return EQ;
		case '>':
			return GT;
		case '(':
			return L_PAREN;
		case ')':
			return R_PAREN;
		case '\'':
			return APOSTROPHE;
		
		default:
			throw new IllegalArgumentException("unregistered char: " + ch);
		}
	}
}
