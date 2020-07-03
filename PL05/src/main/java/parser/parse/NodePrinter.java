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

    // 괄호를 출력해줌
    private void printList(Node head) {
        if (head == null) {
            sb.append("( )");
            return;
        }
        sb.append("("); // 왼쪽 괄호 추가
        printNode(head); // 괄호 안에 값을 추가
        sb.append(")"); // 모든 데이터가 추가되면 오른쪽 괄호 추가
    }

    private void printNode(Node head) {
    	// 아무 노드가 없을 때 리턴
        if (head == null)
            return;

        // ListNode일 때 -> "("로 시작할 때
        if (head instanceof ListNode) {
            ListNode ln = (ListNode) head;
            printList(ln.value);

        } else {
        	// 노드의 값을 저장
            sb.append("[" + head + "]");
        }

        // 노드가 없을 때
        if (head.getNext() != null) {
            sb.append(" ");
        }
        // 다음 노드를 출력
        printNode(head.getNext());
    }

    public void prettyPrint() {
        printNode(root);

        try (FileWriter fw = new FileWriter(OUTPUT_FILENAME);
        	PrintWriter pw = new PrintWriter(fw)) {
            pw.write(sb.toString()); // 문자열로 변환한 값을 출력
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
