package parser.parse;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;

import parser.ast.*;
import parser.ast.BinaryOpNode.BinType;
import lexer.Scanner;
import lexer.ScannerMain;
import lexer.Token;
import lexer.TokenType;

public class CuteParser {
	private Iterator<Token> tokens;

	public CuteParser(File file) {
		try {
			tokens = Scanner.scan(file); // lexer에서 구현한 Scanner을 이용해서 토큰화를 해 줌
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Token getNextToken() {
		// 다음 토큰이 없다면 null을 리턴
		if (!tokens.hasNext())
			return null;
		// 다음 토큰이 있다면 다음 토큰을 리턴
		return tokens.next();
	}

	// 토큰들이 어떤 타입의 노드인지 정해준다 
	public Node parseExpr() {
		// 다음 토큰
		Token t = getNextToken();
		// 토큰이 없다면 null을 리턴
		if (t == null) {
			System.out.println("No more token");
			return null;
		}
		TokenType tType = t.type();
		String tLexeme = t.lexme();

		switch (tType) {
		case ID:
			IdNode idNode = new IdNode();
			idNode.value = tLexeme;
			return idNode;
		case INT:
			IntNode intNode = new IntNode();
			if (tLexeme == null)
				System.out.println("???");
			intNode.value = new Integer(tLexeme);
			return intNode;

		// BinaryOpNode에 대하여 작성
		// +, -, /, *가 해당
		case DIV:
		case EQ:
		case MINUS:
		case GT:
		case PLUS:
		case TIMES:
		case LT:
			BinaryOpNode binaryOpNode = new BinaryOpNode();
			binaryOpNode.setValue(tType); // tType을 사용하여 BinType 설정
			return binaryOpNode;
		

		// FunctionNode에 대하여 작성
		// 키워드가 FunctionNode에 해당
		case ATOM_Q:
		case CAR:
		case CDR:
		case COND:
		case CONS:
		case DEFINE:
		case EQ_Q:
		case LAMBDA:
		case NOT:
		case NULL_Q:
			/*
			여기 채움!
			 */
			FunctionNode functionNode = new FunctionNode();
			functionNode.setValue(tType);
			return functionNode;

		// BooleanNode에 대하여 작성
		case FALSE:
			BooleanNode falseNode = new BooleanNode();
			falseNode.value = false;
			return falseNode;
		case TRUE:
			BooleanNode trueNode = new BooleanNode();
			trueNode.value = true;
			return trueNode;

		// case L_PAREN일 경우와 case R_PAREN일 경우에 대해서 작성
		// L_PAREN일 경우 parseExprList()를 호출하여 처리
		// 왼쪽 괄호
		case L_PAREN:
			/*
			여기 채움!
			 */
			ListNode listNode = new ListNode();
			listNode.value = parseExprList(); // Node의 리스트 생성
			return listNode; // list 리턴

		case R_PAREN:
			return null;

		default:
			// head의 next를 만들고 head를 반환하도록 작성
			System.out.println("Parsing Error!");
			return null;
		}

	}

	// List의 value를 생성하는 메소드
	private Node parseExprList() {
		Node head = parseExpr(); // parseExpr 이용하여 어떤 타입의 Node인지
		// head의 next 노드를 set하시오.
		if (head == null) // if next token is RPAREN
			return null;
		head.setNext(parseExprList()); // 다음 노드에 대하여 parseExprList 실행.
		return head; // 노드 리턴
	}
}
