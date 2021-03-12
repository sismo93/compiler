import java.util.ArrayList;
import java.util.List;






/**
 * Class that calcul the expression
 * using the LLVM syntax
 */
public class ArithExpression {
    private List<String> listInstructions = new ArrayList<String>();
    private List<String> stack = new ArrayList<String>();

    private List<String> operator = new ArrayList<String>(){
        {add("+");add("-");add("*");add("/");
        add("=");add(">");add("<");
        add(">=");add("<=");add("/=");
        add("xor");add("and");add("or");}
    };


    private ScopeHandler scope;

    private List<Boolean> varnameAddCount = new ArrayList<Boolean>();

    public ArithExpression(List<String> listInstructions,List<String> stack, ScopeHandler scope){
        this.listInstructions = listInstructions;
        this.stack = stack;
        this.scope = scope;
        scope.increment();
        

    }


    /**
     * Function that will read our stack and do 
     * the operation for the 2 precedent elements
     */
    public void calcul(){
        int pos = 0;
        while (!operator.contains(stack.get(pos))){ // while we dont have an operator/comp
            pos++; 
        }
        // we have an operator so we need to take pos - 1 and pos -2 for the value 
        String left = variableOrNumber(stack.get(pos-2));
        ajustCounter();
        String right = variableOrNumber(stack.get(pos-1));
        ajustCounter();

        String op = getLLop(stack.get(pos));
        if (op.equals("xor i1 ")){ // unique case when we have a XOR , we need to swap the right and left value
            String tmp = left;
            left = right;
            right = tmp;
        }

        addInstruction(left,right,op);
        stack.remove(pos);
        stack.remove(pos-1);
        stack.remove(pos-2);

        stack.add(pos-2,"%"+scope.getCount()); // put the variable of the operation
        
        if (stack.size() != 1){ 
            scope.increment();
            calcul();
        }
    }

    /**
     * function that ajust the counter because sometimes we have 
     * a varname that has to be load 
     */
    private void ajustCounter(){
        scope.addCountValue(varnameAddCount.size()); // add the counter when its > 0 --> when we have variable inside expression 
        varnameAddCount.clear();

    }

    /**
     * allow us to check if its a variable/number/register
     * @param  value variable/number/register
     * @return the right format
     */
    private String variableOrNumber(String value){
        if (value.charAt(0) == '%'){
            return value ; // already a register
        }
        try { // either a number or a varname
            Integer.valueOf(value);
            return value;

        }catch (NumberFormatException ign){
            scope.loadVariable(value);
            varnameAddCount.add(true);
            return scope.getLastVar();
        }
    }

    /**
     * add the instruction
     * @param  left first value
     * @param  right second value
     * @param  op operator
     */
    private void addInstruction(String left,String right,String op){
        listInstructions.add("%"+scope.getCount()+" = "+op+left+", "+right);
        
    }

    /**
    * @param op a given operator 
	* @return the right LLVM operator
    */
    private String getLLop(String op){
        switch (op){
            case "+":
                return "add i32 ";
            case "-":
                return "sub i32 ";
            case "*":
                return "mul i32 ";
            case "/":
                return "sdiv i32 ";
            case "=":
                return "icmp eq i32 ";
            case ">=":
                return "icmp sge i32 ";
            case ">":
                return "icmp sgt i32 ";
            case "<":
                return "icmp slt i32 ";
            case "<=":
                return "icmp sle i32 ";
            case "/=":
                return "icmp ne i32 ";
            case "xor":
                return "xor i1 ";
            case "and":
                return "and i1 ";
            case "or":
                return "or i1 ";
            default:
                throw new RuntimeException ("error in the Arithmetic expression");
        }
    }


}
