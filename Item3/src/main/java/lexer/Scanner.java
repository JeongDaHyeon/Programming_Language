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
    public static Iterator<Token> scan(String string) throws Exception {
        ScanContext context = new ScanContext(string);
        return new TokenIterator(context);
    }

    // return tokens as a Stream
    public static Stream<Token> stream(String str) throws Exception {
        Iterator<Token> tokens = scan(str);
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(tokens, Spliterator.ORDERED), false);
    }


}