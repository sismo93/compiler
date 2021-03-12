import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;






/**
 * Class that allow us to transform an arithmetic expression  
 * to a more usable one (SY algo)
 */
public class Arithmetic {
    private List<String> stackNumber;
    private List<String> stackOperator;
    private List<String> operator;
    private List<String> logicalGate;

    public Arithmetic(ParseTree tree){
        operator = Arrays.asList ("+","-","*","/");
        logicalGate = Arrays.asList("or","xor","and");
        stackNumber = new ArrayList<String>();
        stackOperator = new ArrayList<String>();
        transformToSYA(tree);
        clearStackOperator();

    }


    /**
    * Function that add or pop in the stack regarding the priority of the 
    * expression. We will go through all the arithmetic expression
    * @param tree tree of the instruction
    */
    private void transformToSYA(ParseTree tree){
        String value;
        switch (tree.getRule()){
            case 14:
            case 15:
            case 20:
            case 22:
            case 31: 
            case 34: 
            case 37: 
                for (ParseTree son : tree.getChildren()){
                    transformToSYA(son);
                }

                break;
            case 16: // -5 --> (0 - 5)
                stackOperator.add("("); 
                stackNumber.add("0"); // add value 0
                stackOperator.add("-");

                transformToSYA(tree.getChildren().get(1)); // call for 18 ATOM

                closingBracket();

                break;
            case 17: // Varname
            case 18: //Number
                stackNumber.add(tree.getChildren().get(0).getLabel().getValue().toString());
                break;
            case 19: // case where we have bracket (..)

                stackOperator.add("(");
                transformToSYA(tree.getChildren().get(1)); // recusirve for 14 <exprArith>

                // closing bracket , we add everything betweet (..)
                closingBracket();
                break;

            case 24:
            case 25:
            case 26:
            case 27:
                value = tree.getChildren().get(0).getLabel().getValue().toString();
                int topStack = stackOperator.size()-1;

                if (stackOperator.size()!=0 && operator.contains(stackOperator.get(topStack))){ // if we have something on the stack + top of stack not a "(" or operator  

                    comparePriority(value); 
                } 
                else{ 
                    stackOperator.add(value); 
                }
                break;

            case 32: // and
            case 35: // or
                value = tree.getChildren().get(0).getLabel().getValue().toString();
                if (stackOperator.size()>0){ // we have AND OR  and there is something on the stack, we need to clear it first
                    clearStackKeepLogic(); // 

                }

                
                if (stackOperator.size()>0){ // AND have a bigger priority then OR so we need to check first 
                	comparePriority(value);
                }else{stackOperator.add(value);}


                transformToSYA(tree.getChildren().get(1));
                if (tree.getChildren().size()>2){ // meaning we have another and / or 
                	transformToSYA(tree.getChildren().get(2));
                }
                break;

            case 38: // not is like having 1 xor something

                if (stackOperator.size()>0){ // we have AND OR  and there is something on the stack, we need to clear it first
                    clearStackKeepLogic(); 

                }
                stackOperator.add("xor");
                stackOperator.add("1");
                transformToSYA(tree.getChildren().get(1));
                break;
            case 39: 
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
                String comp = tree.getChildren().get(0).getLabel().getValue().toString();
                if (stackOperator.size()>0){ 
                    clearStackKeepLogic(); 
                }
                stackOperator.add(comp);

                break;

        }
        
    }

    /**
     * We clear the stack but we keep the top priority on the stack (and or not)
     */
    private void clearStackKeepLogic(){
        int pos = stackOperator.size()-1;
        boolean end = false;
        String value ;
        while(pos>=0 && !end){ // add to the stack
            value = stackOperator.get(pos);
            if (logicalGate.contains(value) || value.equals("1")) { // 1 mean we have a "not" we cant remove it 
                end = true;
            }
            else{
                stackNumber.add(stackOperator.get(pos));
                stackOperator.remove(pos);
                pos--;
            }
        }
    }

    /**
     * Function that will add every operator between (...)
     */
    private void closingBracket(){
        int lastpos = stackOperator.size()-1;
        while (!stackOperator.get(lastpos).equals("(")){
            stackNumber.add(stackOperator.get(lastpos));
            stackOperator.remove(lastpos);
            lastpos--;
        }
        stackOperator.remove(lastpos); // remove the "(""

    }

    /**
     * clear the stackOperator, we need it when we have comp or at the end there can be some operator
     */
    private void clearStackOperator(){
                    
        for (int lastpos = stackOperator.size()-1;lastpos>=0;lastpos--){ // add to the stack
            stackNumber.add(stackOperator.get(lastpos));
        }
        stackOperator.clear();
    }


    /**
    * Compare the priority between 2 operator
    * @param op operator to check with
    */
    private void comparePriority(String op){ // check priority for the expression
        int topStack = stackOperator.size()-1;
        if (getPriority(op) > getPriority(stackOperator.get(topStack)) ){
            stackOperator.add(op);
        }
        else if (getPriority(op) == getPriority(stackOperator.get(topStack))){ // same priority
            stackNumber.add(stackOperator.get(topStack));
            stackOperator.remove(topStack);
            stackOperator.add(op);
        }
        else{ // not same priority
            stackNumber.add(stackOperator.get(topStack));
            stackOperator.remove(topStack);
            stackOperator.add(op);

        }
        

    }

    /**
     * Give us the priority of the operation
     * @param  op is the op we want to know the priority
     * @return    his priority 
     */
    private int getPriority(String op){
        if ((op.equals("+")) || (op.equals("-"))) {
            return 1;
        }
        else if ((op.equals("*")) || (op.equals("/"))) {
            return 2;
        }
        else if (op.equals("or")) {
        	return 3;
        }
        else if (op.equals("and")) {
        	return 4;
        }
        else{
        	return 5;
        }
    }


    public List<String> getStack(){
        return stackNumber;
    }


}
