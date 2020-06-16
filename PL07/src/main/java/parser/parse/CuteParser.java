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
			tokens = Scanner.scan(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Token getNextToken() {
		if (!tokens.hasNext())
			return null;
		return tokens.next();
	}

	public Node parseExpr() {
		Token t = getNextToken();
		if (t == null) {
			System.out.println("No more token");
			return null;
		}
		TokenType tType = t.type();
		String tLexeme = t.lexme();

		switch (tType) {
			case DIV:
			case EQ:
			case MINUS:
			case GT:
			case PLUS:
			case TIMES:
			case LT:
				/*
				 * ���� ä��!
				 */
				return new BinaryOpNode(tType);

			// FunctionNode�� ���Ͽ� �ۼ�
			// Ű���尡 FunctionNode�� �ش�
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

				return new FunctionNode(tType);

			case ID:
				return new IdNode(tLexeme);
			case INT:
				if (tLexeme == null)
					System.out.println("???");
				return new IntNode(tLexeme);
			// ���� ������ BooleanNode Case
			case FALSE:
				return BooleanNode.FALSE_NODE;
			case TRUE:
				return BooleanNode.TRUE_NODE;
			// ���� ������ L_PAREN, R_PAREN Case
			case L_PAREN:
				// ( : list�� ����
				return parseExprList();
			case R_PAREN:
				// ): list�� ���� ǥ��
				return END_OF_LIST;
			// ���� �߰��� APOSTROPHE, ��
			case APOSTROPHE:
				QuoteNode quoteNode = new QuoteNode();

				Node QuotedNode = parseExpr(); // will be ( a b ) or 2

				if (QuotedNode instanceof ListNode) {
					((ListNode) QuotedNode).setQuotedIn();
					ListNode listnode = ListNode.cons(QuotedNode, ListNode.ENDLIST);
					ListNode new_listNode = ListNode.cons(quoteNode, listnode);
					return new_listNode;
				} else if (QuotedNode instanceof QuotableNode) {

					((QuotableNode) QuotedNode).setQuoted();
					ListNode li = ListNode.cons(QuotedNode, ListNode.ENDLIST);
					// ListNode ����
					ListNode listNode = ListNode.cons(quoteNode, li);
					return listNode;
				}
			default:
				System.out.println("Parsing Error!");
				return null;
		}
	}

	// List�� value�� �����ϴ� �޼ҵ�
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
