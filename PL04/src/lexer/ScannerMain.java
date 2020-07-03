package lexer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Stream;

public class ScannerMain {
    public static final void main(String... args) throws Exception {
        ClassLoader cloader = ScannerMain.class.getClassLoader();
        File file = new File(cloader.getResource("lexer/as04.txt").getFile());
        testTokenStream(file); // ScannerMain의 함수
    }
    
    // use tokens as a Stream 
    // file을 입력 받으면 Scanner의 
    private static void testTokenStream(File file) throws IOException {
    	// Stream의 타입은 Token
        Stream<Token> tokens = Scanner.stream(file);
        // map의 forEach를 이용하여 각각 프린트를 함
        // tokens.map(ScannerMain::toString).forEach(System.out::println);

        
        // Write to hw04.txt
        // pw : Object of PrintWriter
        PrintWriter pw = new PrintWriter(".\\src\\lexer\\hw04.txt");
        try {
        	// write at File
        	// hw04.txt 파일에 저장
        	tokens.map(ScannerMain::toString).forEach(pw::println); 
        	pw.close();
        	
        } catch(Exception e) {
        	e.printStackTrace();
        }
        
    }    
    
    private static String toString(Token token) {
        return String.format("%-3s: %s", token.type().name(), token.lexme());
    }
    
}
