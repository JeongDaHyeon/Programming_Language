package parser.parse;

import java.io.File;


public class ParserMain {
	public static final void main(String... args) throws Exception {
        ClassLoader cloader = ParserMain.class.getClassLoader();
		File file = new File(cloader.getResource("parser/as05.txt").getFile()); // 파일을 입력 받음
          
		CuteParser cuteParser = new CuteParser(file); // CuteParser 객체 생성
		
		// 토큰화 한 것을 프린트 해줌
		// ListNode의 객체를 이용하여 새로운 객체 생성
		NodePrinter nodePrinter = new NodePrinter(cuteParser.parseExpr());
		nodePrinter.prettyPrint();

    }
}
