package lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Scanner {
    // return tokens as an Iterator
    public static Iterator<Token> scan(File file) throws FileNotFoundException {
        ScanContext context = new ScanContext(file);
        // context: object of ScanContext class
        return new TokenIterator(context); // return object of TokenIterator 
    }

    // return tokens as a Stream 
    // file : as04.txt
    public static Stream<Token> stream(File file) throws FileNotFoundException {
    	// scan: function of Scanner
        Iterator<Token> tokens = scan(file); // 입력 받은 파일을 넘김
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(tokens, Spliterator.ORDERED), false);
    }
}