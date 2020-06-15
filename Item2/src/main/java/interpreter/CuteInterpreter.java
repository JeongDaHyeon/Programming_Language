package interpreter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.List;

import parser.ast.*;
import parser.parse.*;

public class CuteInterpreter {
    boolean fill = true;
    public static Hashtable<String, Node> symbolTable = new Hashtable<String, Node>(); // symbol table 을 만듦

    public static void main(String[] args) throws Exception {
        ClassLoader cloader = ParserMain.class.getClassLoader();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                System.out.print("> ");
                String string = bufferedReader.readLine(); // 문자열을 입력 받음
                CuteInterpreter interpreter = new CuteInterpreter();
                CuteParser cuteParser = new CuteParser(string);
                Node parseTree = cuteParser.parseExpr();
                Node resultNode = interpreter.runExpr(parseTree);
                NodePrinter nodePrinter = new NodePrinter(resultNode);
                System.out.print(". ");
                nodePrinter.prettyPrint();
            } catch (Exception e) {
                System.out.println("Wrong Input"); // 잘못된 입력을 처리
            }
        }

    }

    // 테이블에 id가 value 임을 저장하는 함수
    public void insertTable(String id, Node value) {
        symbolTable.put(id, value);
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
     * @param operator : list.car()
     * @param operand  : list.cdr()
     * @return
     */
    private Node runFunction(FunctionNode operator, ListNode operand) {

        switch (operator.funcType) {
            case CAR:
                if(operand.car() instanceof IdNode) // 변수에 저장된 리스트를 불러 올때
                {
                    // 리스트를 불러오고, 다시 runExpr를 호출
                    return runExpr(ListNode.cons(operator, ListNode.cons(symbolTable.get(operand.car().toString()), ListNode.EMPTYLIST)));
                }
                else if (((ListNode) operand.car()).car() instanceof QuoteNode) { // check the Quote exists
                    // if QuoteNode exist execute the code
                    return ((ListNode) ((ListNode) operand.car()).cdr().car()).car(); // return the result
                }
            case CDR:
                if(operand.car() instanceof IdNode) // 변수에 저장된 리스트를 불러 올때
                {
                    // 리스트를 불러오고, 다시 runExpr를 호출
                    return runExpr(ListNode.cons(operator, ListNode.cons(symbolTable.get(operand.car().toString()), ListNode.EMPTYLIST)));
                }
                else if (((ListNode) operand.car()).car() instanceof QuoteNode) { // check the Quote exists
                    // if QuoteNode exist execute the code
                    return ((ListNode) ((ListNode) operand.car()).cdr().car()).cdr();
                }
            case CONS:
                Node operand1 = operand.car(); // 첫 번째 인자
                Node operand2 = operand.cdr().car(); // 두 번째 인자

                // 변수가 포함되어 있을 때
                if (operand1 instanceof IdNode || operand2 instanceof IdNode) {
                    // 변수의 값을 불러옴
                    if (operand1 instanceof IdNode) {
                        operand1 = symbolTable.get(operand1.toString());

                    }
                    if (operand2 instanceof IdNode) {
                        operand2 = symbolTable.get(operand2.toString());
                    }
                    // operand 변수의 값을 변경한 list
                    ListNode list = ListNode.cons(operand2, ListNode.EMPTYLIST);
                    list = ListNode.cons(operand1, list);
                    list = ListNode.cons(operator, list);
                    // 변경한 값들로 계산
                    return runExpr(list);
                }

                Node cons_head = operand.car(); // head
                ListNode cons_tail = (ListNode) ((ListNode) operand.cdr().car()).cdr().car(); // tail
                return ListNode.cons(cons_head, cons_tail); // join the head and tail

            case NULL_Q:
                // 인자가 변수인 경우
                if(operand.car() instanceof IdNode)
                {
                    // 값을 불러와서 처리
                    return runExpr(ListNode.cons(operator, ListNode.cons(symbolTable.get(operand.car().toString()), ListNode.EMPTYLIST)));
                }
                if (((ListNode) operand.car()).car() instanceof QuoteNode && ((ListNode) operand.car()).cdr().car() instanceof ListNode) { // check if the node start with QuoteNode
                    // check the object address
                    if (((ListNode) ((ListNode) operand.car()).cdr().car()).equals(((ListNode) operand.car()).cdr().cdr())) { // check it is list
                        return BooleanNode.TRUE_NODE; // return true
                    } else {
                        return BooleanNode.FALSE_NODE; // if the list are not null
                    }
                } else {
                    return BooleanNode.FALSE_NODE; // if it is not list
                }
            case ATOM_Q:
                // 인자가 하나인 경우
                if(operand.car() instanceof IdNode)
                {
                    // 변수를 변수의 값으로 변경해 줌
                    return runExpr(ListNode.cons(operator, ListNode.cons(symbolTable.get(operand.car().toString()), ListNode.EMPTYLIST)));
                }

                // null list -> true
                if (((ListNode) operand.car()).car() instanceof QuoteNode && ((ListNode) operand.car()).cdr().car() instanceof ListNode) { // check if the node start with QuoteNode
                    // check the object address
                    if (((ListNode) ((ListNode) operand.car()).cdr().car()).equals(((ListNode) operand.car()).cdr().cdr())) {
                        return BooleanNode.TRUE_NODE; // return true
                    } else {
                        return BooleanNode.FALSE_NODE;
                    }
                } else {
                    return BooleanNode.TRUE_NODE; // return true

                }
            case EQ_Q:
                // 첫 번째 인자
                Node operand1_eq = operand.car();
                // 두 번째 인자
                Node operand2_eq = operand.cdr().car();

                if(operand1_eq instanceof IntNode && operand2_eq instanceof IntNode)
                {
                    if(operand1_eq.equals(operand2_eq)) return BooleanNode.TRUE_NODE;
                    else return BooleanNode.FALSE_NODE;
                }

                if(operand1_eq instanceof IdNode && operand2_eq instanceof IdNode)
                {
                    // 인자의 이름이 동일한 경우

                    //if(symbolTable.get(operand1_eq.toString()).equals(symbolTable.get(operand2_eq.toString())))
                    if(operand1_eq.equals(operand2_eq))
                    {
                        return BooleanNode.TRUE_NODE; // return TRUE_NODE
                    }
                    else
                    {
                        return BooleanNode.FALSE_NODE; // return FALSE_NODE
                    }
                }



                if(((ListNode)operand.car()).car() instanceof QuoteNode)
                {
                    Node param1 = ((ListNode) operand.car()).cdr().car(); // parameter 1
                    Node param2 = ((ListNode) operand.cdr().car()).cdr().car(); // parameter 2



                    // 리스트 null List일 때
                    if(param1 instanceof ListNode || param2 instanceof ListNode) {
                        if(((ListNode)operand1_eq).cdr().car().equals(((ListNode)operand1_eq).cdr().cdr()) && ((ListNode)operand2_eq).cdr().car().equals(((ListNode)operand2_eq).cdr().cdr())){
                            return BooleanNode.TRUE_NODE;
                        }
                        else
                        {
                            return BooleanNode.FALSE_NODE;
                        }
                    }



//                    if (param1 instanceof ListNode) // if the param1 is ListNode
//                    {
//                        return BooleanNode.FALSE_NODE; // return FALSE_NODE
//                    }
//
//                    // if eq? (list) (atom)
//                    if (param2 instanceof ListNode) // if the param2 is ListNode
//                    {
//                        return BooleanNode.FALSE_NODE; // return FALSE_NODE
//                    }

                    // 문자열을 검사

                    if (param1.equals(param2)) // if the Object is same
                    {
                        return BooleanNode.TRUE_NODE; // return TRUE_NODE
                    }
                    else // if the Object is different
                    {
                        return BooleanNode.FALSE_NODE; // return FALSE_NODE
                    }



                }

            case NOT:
                // 변수인 경우
                if(operand.car() instanceof IdNode)
                {
                    // 만약 불러온 값이 BooleanNode인 경우
                    if(symbolTable.get(operand.car().toString()) instanceof BooleanNode)
                    {
                        // 변수의 값으로 변경한 다음 처리
                        return runExpr(ListNode.cons(operator, ListNode.cons(symbolTable.get(operand.car().toString()), ListNode.EMPTYLIST)));
                    }
                }

                if (operand.car().equals(BooleanNode.TRUE_NODE)) { // if TRUE_NODE
                    return BooleanNode.FALSE_NODE; // return FALSE_NODE
                } else if (operand.car().equals(BooleanNode.FALSE_NODE)) // if FALSE_NODE
                {
                    return BooleanNode.TRUE_NODE; // return TRUE_NODE
                } else {
                    // If more calculation is need, call runExpr and calcuate it
                    return runFunction(operator, ListNode.cons(runExpr(ListNode.cons(operand.car(), operand.cdr())), ListNode.EMPTYLIST));
                }
            case COND:
                // 다른 곳에서 변수 처리를 하였기 때문에
                // 따로 처리를 할 필요가 없음
                Node cond_param1 = ((ListNode) operand.car()).car(); // condition
                Node cond_param2 = ((ListNode) operand.car()).cdr().car(); // return Node

                if (cond_param1 instanceof BooleanNode) {
                    if (cond_param1.equals(BooleanNode.TRUE_NODE)) {
                        return cond_param2;
                    }
                }


                if (cond_param1 instanceof ListNode) {
                    if (runList((ListNode) cond_param1).equals(BooleanNode.TRUE_NODE)) return cond_param2;
                }

                return runList(ListNode.cons(operator, operand.cdr()));
            case DEFINE:
                Node id = operand.car(); // IdNode

                Node value = operand.cdr().car();
                // value가 ListNode 일 때
                if (value instanceof ListNode) {
                    // runExpr로 처리
                    insertTable(id.toString(), runExpr(value));
                } else {
                    // 그 외에는 그냥 저장
                    insertTable(id.toString(), value);
                }
                break;

            case LAMBDA:
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

        Node operand1 = operand.car();
        Node operand2 = operand.cdr().car();
        
        // 둘 중 하나라도 IdNode인 경우
        // 즉, 변수인 경우
        if (operand1 instanceof IdNode || operand2 instanceof IdNode) {
            // operand1이 변수인 경우 value를 불러옴
            if (operand1 instanceof IdNode) {
                operand1 = symbolTable.get(operand1.toString());
            }
            // operand2이 변수인 경우 value를 불러옴
            if (operand2 instanceof IdNode) {
                operand2 = symbolTable.get(operand2.toString());
            }
            // list의 변수 id에 해당하는 값으로 변경
            list = ListNode.cons(operand2, ListNode.EMPTYLIST);
            list = ListNode.cons(operand1, list);
            list = ListNode.cons(operator, list);
            return runExpr(list); // list를 처리
        } else {
            // 기존의 Item1 의 방법과 동일
            switch (operator.binType) {
                // Use recursion if it is ListNode

                case PLUS:
                    if ((operand.car() instanceof ListNode) && (operand.cdr().car() instanceof ListNode)) // if both are ListNode
                    {
                        return new IntNode(Integer.toString(((IntNode) runBinary((ListNode) operand.car())).getValue() + ((IntNode) runBinary(((ListNode) operand.cdr().car()))).getValue()));
                    } else if (operand.car() instanceof ListNode) {
                        return new IntNode(Integer.toString(((IntNode) runBinary((ListNode) operand.car())).getValue() + ((IntNode) operand.cdr().car()).getValue()));

                    } else if (operand.cdr().car() instanceof ListNode) {
                        return new IntNode(Integer.toString(((IntNode) operand.car()).getValue() + ((IntNode) runBinary(((ListNode) operand.cdr().car()))).getValue()));
                    } else {
                        return new IntNode(Integer.toString(((IntNode) operand.car()).getValue() + ((IntNode) operand.cdr().car()).getValue()));
                    }
                case MINUS:
                    if ((operand.car() instanceof ListNode) && (operand.cdr().car() instanceof ListNode)) // if both are ListNode
                    {
                        return new IntNode(Integer.toString(((IntNode) runBinary((ListNode) operand.car())).getValue() - ((IntNode) runBinary(((ListNode) operand.cdr().car()))).getValue()));
                    } else if (operand.car() instanceof ListNode) // if first parameter is ListNode
                    {
                        return new IntNode(Integer.toString(((IntNode) runBinary((ListNode) operand.car())).getValue() - ((IntNode) operand.cdr().car()).getValue()));

                    } else if (operand.cdr().car() instanceof ListNode) // if second parameter is ListNode
                    {
                        return new IntNode(Integer.toString(((IntNode) operand.car()).getValue() - ((IntNode) runBinary(((ListNode) operand.cdr().car()))).getValue()));
                    } else  // Both operator and operand are IntNode
                    {
                        return new IntNode(Integer.toString(((IntNode) operand.car()).getValue() - ((IntNode) operand.cdr().car()).getValue()));
                    }
                case TIMES:
                    if ((operand1 instanceof ListNode) && (operand.cdr().car() instanceof ListNode)) // if both are ListNode
                    {
                        // * ListNode ListNode
                        return new IntNode(Integer.toString(((IntNode) runBinary((ListNode) operand.car())).getValue() * ((IntNode) runBinary(((ListNode) operand.cdr().car()))).getValue()));
                    } else if (operand.car() instanceof ListNode)  // if first parameter is ListNode
                    {
                        // * ListNode IntNode
                        return new IntNode(Integer.toString(((IntNode) runBinary((ListNode) operand.car())).getValue() * ((IntNode) operand.cdr().car()).getValue()));
                    } else if (operand.cdr().car() instanceof ListNode) // if second parameter is ListNode
                    {
                        // * IntNode ListNode
                        return new IntNode(Integer.toString(((IntNode) operand.car()).getValue() * ((IntNode) runBinary(((ListNode) operand.cdr().car()))).getValue()));
                    } else  // Both operator and operand are IntNode
                    {
                        // * IntNode IntNode
                        return new IntNode(Integer.toString(((IntNode) operand.car()).getValue() * ((IntNode) operand.cdr().car()).getValue()));
                    }
                case DIV:
                    if ((operand.car() instanceof ListNode) && (operand.cdr().car() instanceof ListNode)) // if both are ListNode
                    {
                        // * ListNode ListNode
                        return new IntNode(Integer.toString(((IntNode) runBinary((ListNode) operand.car())).getValue() / ((IntNode) runBinary(((ListNode) operand.cdr().car()))).getValue()));
                    } else if (operand.car() instanceof ListNode) // if first parameter is ListNode
                    {
                        // * ListNode IntNode
                        return new IntNode(Integer.toString(((IntNode) runBinary((ListNode) operand.car())).getValue() / ((IntNode) operand.cdr().car()).getValue()));
                    } else if (operand.cdr().car() instanceof ListNode) // if second parameter is ListNode
                    {
                        // * IntNode ListNode
                        return new IntNode(Integer.toString(((IntNode) operand.car()).getValue() / ((IntNode) runBinary(((ListNode) operand.cdr().car()))).getValue()));
                    } else  // Both operator and operand are IntNode
                    {
                        // * IntNode IntNode
                        return new IntNode(Integer.toString(((IntNode) operand.car()).getValue() / ((IntNode) operand.cdr().car()).getValue()));
                    }
                case LT: // less then
                    if ((operand.car() instanceof ListNode) && (operand.cdr().car() instanceof ListNode)) // if both are ListNode
                    {
                        // * ListNode ListNode
                        if (((IntNode) runBinary((ListNode) operand.car())).getValue() < ((IntNode) runBinary(((ListNode) operand.cdr().car()))).getValue()) // if first parameter is less than second parameter
                        {
                            return BooleanNode.TRUE_NODE; // return true
                        } else {
                            return BooleanNode.FALSE_NODE; // return false
                        }
                    } else if (operand.car() instanceof ListNode) // if first parameter is ListNode
                    {
                        if (((IntNode) runBinary((ListNode) operand.car())).getValue() < ((IntNode) operand.cdr().car()).getValue()) // if first parameter is less than second parameter
                        {
                            return BooleanNode.TRUE_NODE; // return true
                        } else {
                            return BooleanNode.FALSE_NODE; // return false
                        }
                    } else if (operand.cdr().car() instanceof ListNode) // if second parameter is ListNode
                    {
                        if (((IntNode) operand.car()).getValue() < ((IntNode) runBinary(((ListNode) operand.cdr().car()))).getValue()) // if first parameter is less than second parameter
                        {
                            return BooleanNode.TRUE_NODE; // return true
                        } else {
                            return BooleanNode.FALSE_NODE; // return false
                        }
                    } else // Both are not ListNode
                    {
                        // * IntNode IntNode
                        if (((IntNode) operand.car()).getValue() < ((IntNode) operand.cdr().car()).getValue()) // if first parameter is less than second parameter
                        {
                            return BooleanNode.TRUE_NODE; // return true
                        } else {
                            return BooleanNode.FALSE_NODE; // return false

                        }
                    }

                case GT:
                    if ((operand.car() instanceof ListNode) && (operand.cdr().car() instanceof ListNode)) // if both are ListNode
                    {
                        // * ListNode ListNode
                        if (((IntNode) runBinary((ListNode) operand.car())).getValue() > ((IntNode) runBinary(((ListNode) operand.cdr().car()))).getValue()) // if first parameter is greater than second parameter
                        {
                            return BooleanNode.TRUE_NODE; // return true
                        } else {
                            return BooleanNode.FALSE_NODE; // return false
                        }
                    } else if (operand.car() instanceof ListNode) // if first parameter is ListNode
                    {
                        if (((IntNode) runBinary((ListNode) operand.car())).getValue() > ((IntNode) operand.cdr().car()).getValue()) // if first parameter is greater than second parameter
                        {
                            return BooleanNode.TRUE_NODE; // return true
                        } else {
                            return BooleanNode.FALSE_NODE; // return false
                        }
                    } else if (operand.cdr().car() instanceof ListNode) // if second parameter is ListNode
                    {
                        if (((IntNode) operand.car()).getValue() > ((IntNode) runBinary(((ListNode) operand.cdr().car()))).getValue()) // if first parameter is greater than second parameter
                        {
                            return BooleanNode.TRUE_NODE; // return true
                        } else {
                            return BooleanNode.FALSE_NODE; // return false
                        }
                    } else // Both are not ListNode
                    {
                        // * IntNode IntNode
                        if (((IntNode) operand.car()).getValue() > ((IntNode) operand.cdr().car()).getValue()) // if first parameter is greater than second parameter
                        {
                            return BooleanNode.TRUE_NODE; // return true
                        } else {
                            return BooleanNode.FALSE_NODE; // return false
                        }
                    }
                case EQ:

                    if ((operand.car() instanceof ListNode) && (operand.cdr().car() instanceof ListNode)) // if both are ListNode
                    {
                        // * ListNode ListNode

                        if (((IntNode) runBinary((ListNode) operand.car())).getValue().equals(((IntNode) runBinary(((ListNode) operand.cdr().car()))).getValue()))// if both parameters are same
                        {
                            return BooleanNode.TRUE_NODE; // return true
                        } else {
                            return BooleanNode.FALSE_NODE; // return false
                        }
                    } else if (operand.car() instanceof ListNode) // if first parameter is ListNode
                    {
                        if (((IntNode) runBinary((ListNode) operand.car())).getValue().equals(((IntNode) operand.cdr().car()).getValue())) // if both parameters are same
                        {
                            return BooleanNode.TRUE_NODE; // return true
                        } else {
                            return BooleanNode.FALSE_NODE; // return false
                        }
                    } else if (operand.cdr().car() instanceof ListNode) // if second parameter is ListNode
                    {
                        if (((IntNode) operand.car()).getValue().equals(((IntNode) runBinary(((ListNode) operand.cdr().car()))).getValue())) // if both parameters are same
                        {
                            return BooleanNode.TRUE_NODE; // return true
                        } else {
                            return BooleanNode.FALSE_NODE; // return false
                        }
                    } else // Both are not ListNode
                    {
                        // * IntNode IntNode
                        if (((IntNode) operand.car()).getValue().equals(((IntNode) operand.cdr().car()).getValue())) // if both parameters are same
                        {
                            return BooleanNode.TRUE_NODE; // return true
                        } else {
                            return BooleanNode.FALSE_NODE; // return false
                        }
                    }

                default:
                    break;
            }
        }
        return null;
    }


}
