package parser.parse;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import parser.ast.*;

public class NodePrinter {
    private final String OUTPUT_FILENAME = "output07.txt";
    private StringBuffer sb = new StringBuffer();
    private Node root;

    public NodePrinter(Node root) {
        this.root = root;
    }


    private void printList(ListNode listNode) {
        if (listNode == ListNode.EMPTYLIST) {
            return;
        }
        if (listNode == ListNode.ENDLIST) {
            return;
        }
        printNode(listNode.car());
        printList(listNode.cdr());
    }

    private void printNode(Node node) {
        if (node == null)
            return;
        if(node instanceof ListNode){
            ListNode listNode = (ListNode) node;
            if(((ListNode) node).car() instanceof QuoteNode) {
                printList(listNode);
            } else {
                sb.append("( ");
                printList(listNode);
                sb.append(") ");
            }
        }
        else if(node instanceof QuoteNode) {
            QuoteNode quoteNode = (QuoteNode) node;
            printQuoteNode(quoteNode);
        }
        else {
            sb.append("" + node.toString() + " ");
        }
    }
    private void printQuoteNode(QuoteNode quoteNode) {
        // sb.append("\'");
    }


    public void prettyPrint() {
        printNode(root);
        System.out.println(sb);
        try (FileWriter fw = new FileWriter(OUTPUT_FILENAME);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
