package interpreter;

import java.io.File;
import java.util.Scanner;

import parser.ast.*;
import parser.parse.*;
import parser.ast.FunctionNode.FunctionType;

public class CuteInterpreter {
    boolean fill = true;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ClassLoader cloader = ParserMain.class.getClassLoader();
        File file = new File(cloader.getResource("interpreter/as07.txt").getFile());
        CuteParser cuteParser = new CuteParser(file);
        CuteInterpreter interpreter = new CuteInterpreter();
        Node parseTree = cuteParser.parseExpr();
        Node resultNode = interpreter.runExpr(parseTree);
        NodePrinter nodePrinter = new NodePrinter(resultNode);
        nodePrinter.prettyPrint();
    }

    private void errorLog(String err) {
        System.out.println(err);
    }

    public Node runExpr(Node rootExpr) {
        if (rootExpr == null)
            return null;
        if (rootExpr instanceof IdNode)
            return rootExpr;
        else if (rootExpr instanceof IntNode)
            return rootExpr;
        else if (rootExpr instanceof BooleanNode)
            return rootExpr;
        else if (rootExpr instanceof ListNode)
            return runList((ListNode) rootExpr);
        else
            errorLog("run Expr error");
        return null;
    }

    private Node runList(ListNode list) {
        list = (ListNode) stripList(list);
        if (list.equals(ListNode.EMPTYLIST))
            return list;
        if (list.car() instanceof FunctionNode) {
            if (false)
                return list;
            else {
                return runFunction((FunctionNode) list.car(), list.cdr());
            }
        }

        if (list.car() instanceof BinaryOpNode) {
            if (false) // Quote 내부의 리스트의 경우 계산하지 않음
                return list;
            else
                return runBinary(list);
        }
        return list;
    }

    // operator: list.car()
    /**
     *
     * @param operator : list.car()
     * @param operand  : list.cdr()
     * @return
     */
    private Node runFunction(FunctionNode operator, ListNode operand) {
//		System.out.println("runFunction");
//
//		System.out.println(operator.funcType);


        switch (operator.funcType) {
            case CAR:
                if(((ListNode) operand.car()).car() instanceof QuoteNode) { // check the Quote exists
                    // if QuoteNode exist execute the code
                    return ((ListNode)((ListNode)operand.car()).cdr().car()).car(); // return the result
                }
            case CDR:
                if(((ListNode) operand.car()).car() instanceof QuoteNode) { // check the Quote exists
                    // if QuoteNode exist execute the code
                    return ((ListNode)((ListNode) operand.car()).cdr().car()).cdr();
                }
            case CONS:
                Node cons_head = operand.car(); // head
                ListNode cons_tail = (ListNode) ((ListNode) operand.cdr().car()).cdr().car(); // tail
                return ListNode.cons(cons_head, cons_tail); // join the head and tail

            case NULL_Q:
                // if(((ListNode) operand.car()).cdr().cdr().car() instanceof ListNode)
                // System.out.println(((ListNode) operand.car()).cdr().cdr().car()); // null
                // System.out.println(((ListNode) operand.car()).cdr().cdr().cdr()); // null
                // check the
                if (((ListNode) operand.car()).car() instanceof QuoteNode  && ((ListNode) operand.car()).cdr().car() instanceof ListNode) { // check if the node start with QuoteNode
                    // check the object address
                    if(((ListNode)((ListNode) operand.car()).cdr().car()).equals(((ListNode) operand.car()).cdr().cdr())) { // check it is list
                        return BooleanNode.TRUE_NODE; // return true
                    }
                    else
                    {
                        return BooleanNode.FALSE_NODE; // if the list are not null
                    }
                }
                else
                {
                    return BooleanNode.FALSE_NODE; // if it is not list
                }
            case ATOM_Q:
                // null list -> true
//			System.out.println(((ListNode) operand.car()).car() instanceof QuoteNode);
//			System.out.println(((ListNode) operand.car()).cdr().cdr() instanceof ListNode);
                if (((ListNode) operand.car()).car() instanceof QuoteNode  && ((ListNode) operand.car()).cdr().car() instanceof ListNode) { // check if the node start with QuoteNode
                    // check the object address
                    if(((ListNode)((ListNode) operand.car()).cdr().car()).equals(((ListNode) operand.car()).cdr().cdr())) {
                        return BooleanNode.TRUE_NODE; // return true
                    }
                    else
                    {
                        return BooleanNode.FALSE_NODE;
                    }
                }
                else {
                    return BooleanNode.TRUE_NODE; // return true

                }
            case EQ_Q:
                Node param1 = ((ListNode) operand.car()).cdr().car(); // parameter 1
                Node param2 = ((ListNode)operand.cdr().car()).cdr().car(); // parameter 2

                if(param1 instanceof ListNode) // if the pAram1 is ListNode
                {
                    return BooleanNode.FALSE_NODE; // return FALSE_NODE
                }
                // if eq? (list) (atom)
                if(param2 instanceof ListNode) // if the param2 is ListNode
                {
                    return BooleanNode.FALSE_NODE; // return FALSE_NODE
                }

                if(param1.equals(param2)) // if the Object is same
                {
                    return BooleanNode.TRUE_NODE; // return TRUE_NODE
                }
                else // if the Object is different
                {
                    return BooleanNode.FALSE_NODE; // return FALSE_NODE
                }
            case NOT:
                if(operand.car().equals(BooleanNode.TRUE_NODE)) { // if TRUE_NODE
                    return BooleanNode.FALSE_NODE; // return FALSE_NODE
                }
                else if(operand.car().equals(BooleanNode.FALSE_NODE)) // if FALSE_NODE
                {
                    return BooleanNode.TRUE_NODE; // return TRUE_NODE
                }
                else
                {
                    // If more calculation is need, call runExpr and calcuate it
                    return runFunction(operator, ListNode.cons(runExpr(ListNode.cons(operand.car(), operand.cdr())), ListNode.EMPTYLIST));
                }
            case COND:

                Node cond_param1 = ((ListNode) operand.car()).car(); // condition
                Node cond_param2 = ((ListNode) operand.car()).cdr().car(); // return Node

                if(cond_param1 instanceof BooleanNode) {
                    if (cond_param1.equals(BooleanNode.TRUE_NODE)) {
                        return cond_param2;
                    }
                }

                Node r = null;
                if(cond_param1 instanceof ListNode)
                {
                    if(runList((ListNode) cond_param1).equals(BooleanNode.TRUE_NODE)) return cond_param2;
                }

                return runList(ListNode.cons(operator, operand.cdr()));

            default:
                break;

        }
        return null;
    }

    private Node stripList(ListNode node) {
        if (node.car() instanceof ListNode && node.cdr().car() == null) {
            Node listNode = node.car();
            return listNode;
        } else {
            return node;
        }
    }

    // recursion
    private Node runBinary(ListNode list) {
        BinaryOpNode operator = (BinaryOpNode) list.car();
        ListNode operand = list.cdr();
        switch (operator.binType) {
            // Use recursion if it is ListNode
            case PLUS:
                if((operand.car() instanceof ListNode) && (operand.cdr().car() instanceof ListNode)) // if both are ListNode
                {
                    return new IntNode(Integer.toString(((IntNode) runBinary((ListNode) operand.car())).getValue() + ((IntNode) runBinary(((ListNode)operand.cdr().car()))).getValue()));
                }
                else if(operand.car() instanceof ListNode)
                {
                    return new IntNode(Integer.toString(((IntNode) runBinary((ListNode) operand.car())).getValue() + ((IntNode) operand.cdr().car()).getValue()));

                }
                else if(operand.cdr().car() instanceof ListNode)
                {
                    return new IntNode(Integer.toString(((IntNode) operand.car()).getValue() + ((IntNode) runBinary(((ListNode)operand.cdr().car()))).getValue()));
                }
                else {
                    return new IntNode(Integer.toString(((IntNode) operand.car()).getValue() + ((IntNode) operand.cdr().car()).getValue()));
                }
            case MINUS:
                if((operand.car() instanceof ListNode) && (operand.cdr().car() instanceof ListNode)) // if both are ListNode
                {
                    return new IntNode(Integer.toString(((IntNode) runBinary((ListNode) operand.car())).getValue() - ((IntNode) runBinary(((ListNode)operand.cdr().car()))).getValue()));
                }
                else if(operand.car() instanceof ListNode) // if first parameter is ListNode
                {
                    return new IntNode(Integer.toString(((IntNode) runBinary((ListNode) operand.car())).getValue() - ((IntNode) operand.cdr().car()).getValue()));

                }
                else if(operand.cdr().car() instanceof ListNode) // if second parameter is ListNode
                {
                    return new IntNode(Integer.toString(((IntNode) operand.car()).getValue() - ((IntNode) runBinary(((ListNode)operand.cdr().car()))).getValue()));
                }
                else  // Both operator and operand are IntNode
                {
                    return new IntNode(Integer.toString(((IntNode) operand.car()).getValue() - ((IntNode) operand.cdr().car()).getValue()));
                }
            case TIMES:
                if((operand.car() instanceof ListNode) && (operand.cdr().car() instanceof ListNode)) // if both are ListNode
                {
                    // * ListNode ListNode
                    return new IntNode(Integer.toString(((IntNode) runBinary((ListNode) operand.car())).getValue() * ((IntNode) runBinary(((ListNode)operand.cdr().car()))).getValue()));
                }
                else if(operand.car() instanceof ListNode)  // if first parameter is ListNode
                {
                    // * ListNode IntNode
                    return new IntNode(Integer.toString(((IntNode) runBinary((ListNode) operand.car())).getValue() * ((IntNode) operand.cdr().car()).getValue()));
                }
                else if(operand.cdr().car() instanceof ListNode) // if second parameter is ListNode
                {
                    // * IntNode ListNode
                    return new IntNode(Integer.toString(((IntNode) operand.car()).getValue() * ((IntNode) runBinary(((ListNode)operand.cdr().car()))).getValue()));
                }
                else  // Both operator and operand are IntNode
                {
                    // * IntNode IntNode
                    return new IntNode(Integer.toString(((IntNode) operand.car()).getValue() * ((IntNode) operand.cdr().car()).getValue()));
                }
            case DIV:
                if((operand.car() instanceof ListNode) && (operand.cdr().car() instanceof ListNode)) // if both are ListNode
                {
                    // * ListNode ListNode
                    return new IntNode(Integer.toString(((IntNode) runBinary((ListNode) operand.car())).getValue() / ((IntNode) runBinary(((ListNode)operand.cdr().car()))).getValue()));
                }
                else if(operand.car() instanceof ListNode) // if first parameter is ListNode
                {
                    // * ListNode IntNode
                    return new IntNode(Integer.toString(((IntNode) runBinary((ListNode) operand.car())).getValue() / ((IntNode) operand.cdr().car()).getValue()));
                }
                else if(operand.cdr().car() instanceof ListNode) // if second parameter is ListNode
                {
                    // * IntNode ListNode
                    return new IntNode(Integer.toString(((IntNode) operand.car()).getValue() / ((IntNode) runBinary(((ListNode)operand.cdr().car()))).getValue()));
                }
                else  // Both operator and operand are IntNode
                {
                    // * IntNode IntNode
                    return new IntNode(Integer.toString(((IntNode) operand.car()).getValue() / ((IntNode) operand.cdr().car()).getValue()));
                }
            case LT: // less then
                if((operand.car() instanceof ListNode) && (operand.cdr().car() instanceof ListNode)) // if both are ListNode
                {
                    // * ListNode ListNode
                    if(((IntNode) runBinary((ListNode) operand.car())).getValue() < ((IntNode) runBinary(((ListNode)operand.cdr().car()))).getValue()) // if first parameter is less than second parameter
                    {
                        return BooleanNode.TRUE_NODE; // return true
                    }
                    else
                    {
                        return BooleanNode.FALSE_NODE; // return false
                    }
                }
                else if(operand.car() instanceof ListNode) // if first parameter is ListNode
                {
                    if( ((IntNode) runBinary((ListNode) operand.car())).getValue() < ((IntNode) operand.cdr().car()).getValue() ) // if first parameter is less than second parameter
                    {
                        return BooleanNode.TRUE_NODE; // return true
                    }
                    else
                    {
                        return BooleanNode.FALSE_NODE; // return false
                    }
                }
                else if(operand.cdr().car() instanceof ListNode) // if second parameter is ListNode
                {
                    if( ((IntNode) operand.car()).getValue() < ((IntNode) runBinary(((ListNode)operand.cdr().car()))).getValue() ) // if first parameter is less than second parameter
                    {
                        return BooleanNode.TRUE_NODE; // return true
                    }
                    else
                    {
                        return BooleanNode.FALSE_NODE; // return false
                    }
                }
                else // Both are not ListNode
                {
                    // * IntNode IntNode
                    if( ((IntNode) operand.car()).getValue() < ((IntNode) operand.cdr().car()).getValue() ) // if first parameter is less than second parameter
                    {
                        return BooleanNode.TRUE_NODE; // return true
                    }
                    else
                    {
                        return BooleanNode.FALSE_NODE; // return false

                    }
                }

            case GT:
                if((operand.car() instanceof ListNode) && (operand.cdr().car() instanceof ListNode)) // if both are ListNode
                {
                    // * ListNode ListNode
                    if(((IntNode) runBinary((ListNode) operand.car())).getValue() > ((IntNode) runBinary(((ListNode)operand.cdr().car()))).getValue()) // if first parameter is greater than second parameter
                    {
                        return BooleanNode.TRUE_NODE; // return true
                    }
                    else
                    {
                        return BooleanNode.FALSE_NODE; // return false
                    }
                }
                else if(operand.car() instanceof ListNode) // if first parameter is ListNode
                {
                    if( ((IntNode) runBinary((ListNode) operand.car())).getValue() > ((IntNode) operand.cdr().car()).getValue() ) // if first parameter is greater than second parameter
                    {
                        return BooleanNode.TRUE_NODE; // return true
                    }
                    else
                    {
                        return BooleanNode.FALSE_NODE; // return false
                    }
                }
                else if(operand.cdr().car() instanceof ListNode) // if second parameter is ListNode
                {
                    if( ((IntNode) operand.car()).getValue() > ((IntNode) runBinary(((ListNode)operand.cdr().car()))).getValue() ) // if first parameter is greater than second parameter
                    {
                        return BooleanNode.TRUE_NODE; // return true
                    }
                    else
                    {
                        return BooleanNode.FALSE_NODE; // return false
                    }
                }
                else // Both are not ListNode
                {
                    // * IntNode IntNode
                    if( ((IntNode) operand.car()).getValue() > ((IntNode) operand.cdr().car()).getValue() ) // if first parameter is greater than second parameter
                    {
                        return BooleanNode.TRUE_NODE; // return true
                    }
                    else
                    {
                        return BooleanNode.FALSE_NODE; // return false
                    }
                }
            case EQ:

                if((operand.car() instanceof ListNode) && (operand.cdr().car() instanceof ListNode)) // if both are ListNode
                {
                    // * ListNode ListNode

                    if(((IntNode) runBinary((ListNode) operand.car())).getValue().equals(((IntNode) runBinary(((ListNode)operand.cdr().car()))).getValue()))// if both parameters are same
                    {
                        return BooleanNode.TRUE_NODE; // return true
                    }
                    else
                    {
                        return BooleanNode.FALSE_NODE; // return false
                    }
                }
                else if(operand.car() instanceof ListNode) // if first parameter is ListNode
                {
                    if( ((IntNode) runBinary((ListNode) operand.car())).getValue().equals(((IntNode) operand.cdr().car()).getValue() ) ) // if both parameters are same
                    {
                        return BooleanNode.TRUE_NODE; // return true
                    }
                    else
                    {
                        return BooleanNode.FALSE_NODE; // return false
                    }
                }
                else if(operand.cdr().car() instanceof ListNode) // if second parameter is ListNode
                {
                    if( ((IntNode) operand.car()).getValue().equals(((IntNode) runBinary(((ListNode)operand.cdr().car()))).getValue()) ) // if both parameters are same
                    {
                        return BooleanNode.TRUE_NODE; // return true
                    }
                    else
                    {
                        return BooleanNode.FALSE_NODE; // return false
                    }
                }
                else // Both are not ListNode
                {
                    // * IntNode IntNode
                    if( ((IntNode) operand.car()).getValue().equals(((IntNode) operand.cdr().car()).getValue()) ) // if both parameters are same
                    {
                        return BooleanNode.TRUE_NODE; // return true
                    }
                    else
                    {
                        return BooleanNode.FALSE_NODE; // return false
                    }
                }

            default:

                break;
        }
        return null;
    }


}
