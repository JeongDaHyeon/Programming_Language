package lexer;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;


class TokenIterator implements Iterator<Token> {
	private final ScanContext context;
	private Optional<Token> nextToken;
	
	TokenIterator(ScanContext context) {
		this.context = context;
		nextToken = readToNextToken(context); // 각각 Token마다 readToNextToken을 함
	}

	@Override
	public boolean hasNext() {
		return nextToken.isPresent();
	}

	@Override
	public Token next() {
		if ( !nextToken.isPresent() ) {
			throw new NoSuchElementException();
		}
		
		Token token = nextToken.get();
		nextToken = readToNextToken(context);
		
		return token;
	}

	// 상태 매치?
	private Optional<Token> readToNextToken(ScanContext context) {
		State current = State.START;
		while ( true ) {
			// transit: State에 구현 되어있음
			TransitionOutput output = current.transit(context);
			if ( output.nextState() == State.MATCHED ) {
				return output.token();
			}
			else if ( output.nextState() == State.FAILED ) {
				throw new ScannerException();
			}
			else if ( output.nextState() == State.EOS ) {
				return Optional.empty();
			}
			
			current = output.nextState();
		}
	}
}
