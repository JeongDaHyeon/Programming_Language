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
	private static Node END_OF_LIST = new Node() {
	};

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
		// BinaryOpNode에 대하여 작성
		// +, -, /, *가 해당
		case DIV:
		case EQ:
		case MINUS:
		case GT:
		case PLUS:
		case TIMES:
		case LT:
			/*
			 * 여기 채움!
			 */
			return new BinaryOpNode(tType);

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
			 * 여기 채움!
			 */
			return new FunctionNode(tType);

		case ID:
			return new IdNode(tLexeme);
		case INT:
			if (tLexeme == null)
				System.out.println("???");
			return new IntNode(tLexeme);
		// 새로 구현된 BooleanNode Case
		case FALSE:
			return BooleanNode.FALSE_NODE;
		case TRUE:
			return BooleanNode.TRUE_NODE;
		// 새로 구현된 L_PAREN, R_PAREN Case
		case L_PAREN:
			// ( : list를 생성
			return parseExprList();
		case R_PAREN:
			// ): list의 끝을 표현
			return END_OF_LIST;
		// 새로 추가된 APOSTROPHE, ‘
		case APOSTROPHE:
			// QuoteNode 객체 quoteNode 생성 
			QuoteNode quoteNode = new QuoteNode();

			// 다음 노드.
			Node QuotedNode = parseExpr(); // will be ( a b ) or 2
			
			if (QuotedNode instanceof ListNode) {
				// 다음 노드가  ListNode일 때
				// '(a b)
				((ListNode) QuotedNode).setQuotedIn();
				ListNode listnode = ListNode.cons(QuotedNode, ListNode.ENDLIST);
				// quoteNode와 listnode를 합쳐 새로운 ListNode 생성
				ListNode new_listNode = ListNode.cons(quoteNode, listnode);
				return new_listNode;
			} else if (QuotedNode instanceof QuotableNode) {
				// IntNode일 때
				// IntNode -> extends QuotableNodeImpl
				// QuotableNodeImpl -> implements QuotableNode
				// '2
				((QuotableNode) QuotedNode).setQuoted();
				ListNode li = ListNode.cons(QuotedNode, ListNode.ENDLIST);
				// ListNode 생성
				ListNode listNode = ListNode.cons(quoteNode, li);
				return listNode;
			}
		default:
			System.out.println("Parsing Error!");
			return null;
		}
	}

	// List의 value를 생성하는 메소드
	private ListNode parseExprList() {
		// car
		Node head = parseExpr(); // return Node
		
		if (head == null) // if head is null
			return null;
		if (head == END_OF_LIST) // if next token is RPAREN
			return ListNode.ENDLIST;
		// cdr
		ListNode tail = parseExprList(); // recursion
		if (tail == null)
			return null;
		return ListNode.cons(head, tail);
	}
}
