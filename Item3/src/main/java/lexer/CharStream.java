package lexer;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

class CharStream {
    // private final Reader reader;

    private String string;
    private Character cache; // Character Object
    private int token = 0; // 다음에 읽을 문자의 순서


    static CharStream from(String string) throws Exception {
        return new CharStream(string);
    }

    // CharStream
    CharStream(String string) { // string : CuteInterpreter에 입력받은 문자열
        this.string = string; // 받아온 문자열
        this.cache = null;
        // this.token = 0;
    }


    Char nextChar() { // State에서 사용

//        if (token < string.length()) { // 문자열의 범위를 넘어 갔을 때
//            char ch = string.charAt(token++); // 문자를 읽음
//            return Char.of(ch); // Object of Char 리턴
//        } else {
//            return Char.end(); // Char Object 리턴
//        }
        if (cache != null) { // 문자 객체가 존재할 때
            char ch = cache;
            cache = null;
            return Char.of(ch); // return Object of Char
        } else {
            if (token < string.length()) { // 문자열의 범위를 넘어 갔을 때
                char ch = string.charAt(token++); // 문자를 읽음
                return Char.of(ch); // Object of Char 리턴
            } else {
                return Char.end(); // Char Object 리턴
            }
        }
    }

    void pushBack(char ch) {
        cache = ch;
    }
}
