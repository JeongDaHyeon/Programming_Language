package lexer;

import java.io.File;
import java.io.FileNotFoundException;


// String
class ScanContext {
	private final CharStream input;
	private StringBuilder builder;
	
	ScanContext(File file) throws FileNotFoundException {
		this.input = CharStream.from(file); // return new object of CharStream class
		this.builder = new StringBuilder();
	}
	
	// return Character
	CharStream getCharStream() {
		return input;
	}
	
	String getLexime() {
		String str = builder.toString(); // String
		builder.setLength(0); // initialize String
		return str; // return String
	}
	
	// append Character
	void append(char ch) {
		builder.append(ch);
	}
}
