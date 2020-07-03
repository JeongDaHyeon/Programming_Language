package parser.parse;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import parser.ast.*;

public class NodePrinter {
    private final String OUTPUT_FILENAME = "output06.txt";
    private StringBuffer sb = new StringBuffer();
    private Node root;

    public NodePrinter(Node root) {
        this.root = root;
    }

    // list 노드
    private void printList(ListNode listNode) {
    	if (listNode == ListNode.EMPTYLIST) {
    		return;
    		}
        if (listNode == ListNode.ENDLIST) {
        	return;
        }
        // 첫번째 노드 출력
        printNode(listNode.car());
        // 그 다음 리스트로 출력
        printList(listNode.cdr());
    }

    // Node를 출력
    private void printNode(Node node) {
    	// 아무 노드가 없을 때 리턴
        if (node == null)
            return;
        // node가 ListNode일 때
        if(node instanceof ListNode){
            ListNode listNode = (ListNode) node;
            // QuoteNode일 때
            // 이 처리를 해주지 않으면 List -> Quote -> List에서 맨 처음 List까지 출력이 됨
            if(((ListNode) node).car() instanceof QuoteNode) {
            	// ListNode 출력
            	printList(listNode);
            } else {
            	sb.append("( "); // 왼쪽 괄호 추가
            	// Node 출력
            	printList(listNode);
            	sb.append(") "); // 모든 데이터가 추가되면 오른쪽 괄호 추가            	
            }
        }
        // QuoteNode의 객체일 때
        else if(node instanceof QuoteNode) {
        	QuoteNode quoteNode = (QuoteNode) node;
        	// QuoteNode 출력
        	printQuoteNode(quoteNode);
        }
        else {
        	// Node의 string 추가
            sb.append("[" + node.toString() + "] ");
        }
    }
    // QuotNode일 때 문자열에 ' 추가
    private void printQuoteNode(QuoteNode quoteNode) {
    	sb.append("\'");
    }
    

    public void prettyPrint() {
        printNode(root);
        
        System.out.println(sb);
        try (FileWriter fw = new FileWriter(OUTPUT_FILENAME);
        	PrintWriter pw = new PrintWriter(fw)) {
            pw.write(sb.toString()); // 문자열로 변환한 값을 출력
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
