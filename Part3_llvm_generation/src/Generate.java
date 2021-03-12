import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;






/**
 * Class that allow us to go through 
 * all the tree and add all the
 * necessary instruction for LLVM
 */
public class Generate {
    private ArrayList<String> listInstructions = new ArrayList<String>();
    private ArrayList<String> listFunctionName = new ArrayList<String>();

    private ScopeHandler scopeHandler;
    private List<String> list;
    private int defaultFuncName=0;


    /**
     * Function that will call our buildCode if there is some code
     * It will also allow us to add the name if there is one, else
     * we will give it a default name
     * @param tree Program tree
     * @return list of all the LLVM instructions
     */
    public List<String> generateInstruct(ParseTree tree) {
        initListInstruction();
        List<ParseTree> childs = tree.getChildren();
        ParseTree functNameTree = childs.get(1);
        ParseTree codeRule = childs.get(1); 
        String functName = "defaultFunctionName"+defaultFuncName;
        defaultFuncName++;
        // check if we have a name for the program
        if (functNameTree.getRule() == 52){ 
            functName = functNameTree.getChildren().get(0).getLabel().getValue().toString();
            codeRule = childs.get(2);
        }


		// we cant have 2 program having the same name (we can have 2 programs whitout name
		// they will receive 2 diffrent name)       	
       	if (!listFunctionName.contains(functName)){listFunctionName.add(functName);}
       	else{throw new RuntimeException ("Function " + functName + " has been declared several times");}
        
        listInstructions.add("define void @"+functName+"(){");
        
        if (codeRule.getRule() == 3){ // we have some code
            scopeHandler = new ScopeHandler(listInstructions); 

            buildCode(codeRule.getChildren().get(0)); // we always have only one child  <instList>


        }
		listInstructions.add("ret void");
        listInstructions.add("}\n");

        // check if we have another function or not
        if (childs.get(childs.size()-1).getRule() == 1 ){
            generateInstruct(childs.get(childs.size()-1));
        }
         
        return listInstructions;
    }


    /**
    * Add the main function
    * call each function created even if they dont 
    * have instruction inside	
    */
    public void writeMain(){
	    listInstructions.add("define i32 @main(){");
	    for (String name : listFunctionName){
	    	listInstructions.add("call void @"+name+"()"); // call each function on the main function
	    }
	    listInstructions.add("ret i32 0\n}");
    }


    /**
    * output the LLVM intermediary code by default 
    * will write on a file/execute the instruction if asked by the user
    * @param commandLine is all the demande put by the user 
    */
    public void handleArgsDemande(CommandLine commandLine) throws IOException {
    	if (!commandLine.isLLfile()){
    		// simple output of the instruction 
    		for (String instru : listInstructions){
    			System.out.println(instru);
    		}
    	}else{
    		String fileName = commandLine.getLLFile();
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bwTree = new BufferedWriter(fileWriter);
            for (String inst:listInstructions){
                    bwTree.write(inst);
                    bwTree.write("\n");

            }
            bwTree.close();
            fileWriter.close();

    	}
    }


    /**
    * Go through all the instruction
    * @param tree a tree containing all the instru
    */
    private void buildCode(ParseTree tree){
        for (ParseTree son: tree.getChildren()){
            buildInstruction(son); 
        }
    }


    /**
    * call the right instruction in order to 
    * write the LLVM code
    * @param tree a tree containing all the instru
    */
    private void buildInstruction(ParseTree tree){

	    switch (tree.getRule()) {
            case 4:
                buildCode(tree);
                break;
            case 5:
                buildCode(tree.getChildren().get(1)); 
                break;
	        case 7:
	            buildAssign(tree);
                break;
	        case 8:
	            buildIF(tree.getChildren().get(0));
                break;
	        case 9:
	            buildWhile(tree.getChildren().get(0));
                break;
	        case 10:
	            buildFor(tree.getChildren().get(0));
                break;
	        case 11:
	            buildPrint(tree.getChildren().get(0));
                break;
	        case 12:
	            buildRead(tree.getChildren().get(0));
                break;
	    }
    }



    /**
    * write the assign instruction into LLVM code
    * @param instru a tree containing the assign instruction
    */
    private void buildAssign(ParseTree instru){
    	ParseTree assign = instru.getChildren().get(0); 
    	ParseTree exprArith = assign.getChildren().get(2); // the arithmetic expression
      
    	List<String> stack = new Arithmetic(exprArith).getStack(); // Stack contain the expression to give to the LLVM writer
    	String varnameStr =  assign.getChildren().get(0).getLabel().getValue().toString(); // variable name

        assignVarVal(varnameStr,stack);
    	
    }

  


    /**
     * We will go to the ifCode statement if the cond is true 
     * else we go to elsecode statement.
     *
     * - calcul condition
     * - if condition true jump to true else jump to false
     *
     * true : 
     *     code
     *     jump to end
     * false:
     *    code
     * end:
     *   the rest of the code
     * @param tree tree of the if statement
     */
    private void buildIF(ParseTree tree){
        Boolean alreadyDone = false;
        List<ParseTree> childList = tree.getChildren();

        ParseTree cond = childList.get(1);
        List<String> stack = new Arithmetic(cond).getStack(); 

        ArithExpression expression = new ArithExpression(listInstructions,stack,scopeHandler);        
        expression.calcul();

        String ifCode = "ifCode"+scopeHandler.getCount();
        String elseCode = "elseCode"+scopeHandler.getCount();
        String restCode = "restCode"+scopeHandler.getCount();


        listInstructions.add("br i1 "+scopeHandler.getLastVar()+", label %"+ifCode + ", label %"+elseCode);
        listInstructions.add("br label %"+ifCode);
        listInstructions.add(ifCode+":");


        scopeHandler.increment(); 
        // code inside if statement
        ParseTree codeTree = childList.get(3);
        if (codeTree.getRule()== 3){ // we need to check because sometimes we can just have if x=3 then endif 
            buildCode(childList.get(3)); 

        }
        scopeHandler.increment();

        listInstructions.add("br label %"+restCode); // jump to the rest of the code


        
        //else statement 
        listInstructions.add("br label %"+elseCode);
        listInstructions.add(elseCode+":");

        //elif statement
        ParseTree ifType = childList.get(childList.size()-1); // we take the last elem 
        if (ifType.getRule() == 53){ // we check if its an ELIF 
            buildIF(ifType);
            ifType = ifType.getChildren().get(ifType.getChildren().size()-1);
            if (ifType.getRule() == 30){
                alreadyDone = true;
            }
        }

        if (!alreadyDone){ // allow us to not write the else statement multiple time 
            if (ifType.getRule() == 30 && ifType.getChildren().get(1).getRule() == 3 ){ // meaning we have an else statement AND there is some code 
                buildCode(ifType.getChildren().get(1)); // call the code for the else statement
            }      
        }
        listInstructions.add("br label %"+restCode); // jump to the rest of the code
        listInstructions.add(restCode+":");      



    }


    /**
     * while <cond> do <code>
     * WhileCond: 
     *     if <cond> jump whilecode else go to whileRest
     * whileCode:
     *     some code
     *     jump whileCond
     *whileRest:
     *    do the rest of the code outside the loop
     * @param tree the while tree
     */
    private void buildWhile(ParseTree tree){
        String whileCond = "whileCond"+scopeHandler.getCount();
        String whileBloc = "whileCode"+scopeHandler.getCount();
        String whileRest = "whileRest"+scopeHandler.getCount();

        listInstructions.add("br label %"+whileCond);
        listInstructions.add(whileCond+":");

        ParseTree cond = tree.getChildren().get(1);
        List<String> stack = new Arithmetic(cond).getStack();
        ArithExpression expression = new ArithExpression(listInstructions,stack,scopeHandler);
        expression.calcul();

        listInstructions.add("br i1 "+scopeHandler.getLastVar()+", label %"+whileBloc + ", label %"+whileRest);
        listInstructions.add("br label %"+whileBloc);
        listInstructions.add(whileBloc+":");

        scopeHandler.increment();
        // code
        ParseTree code = tree.getChildren().get(3);
        buildCode(code);

        scopeHandler.increment();

        listInstructions.add("br label %"+whileCond);
        listInstructions.add("br label %"+whileRest);
        listInstructions.add(whileRest+":");


    }



    /**
     * For x From 10 by 2 to 15 do
     *
     * ForCond: 
     *    load X value 
     *    load 10 to a register
     *    compare X and 10 register
     *    if truejump forCode else ForOutside
     *forCode:
     *   some code
     *   load X value
     *   load 2 to register
     *   add 2 register and X
     *   jump ForCond
     * forOutside:
     *      rest of the code
     * @param tree the for tree
     */
    private void buildFor(ParseTree tree){
        String varname = tree.getChildren().get(1).getLabel().getValue().toString();

        String forCond = "forCond"+scopeHandler.getCount();
        String forCode = "forCode"+scopeHandler.getCount();
        String forOut = "forOut"+scopeHandler.getCount();


        ParseTree startTree= tree.getChildren().get(3);
        List<String> stack = new Arithmetic(startTree).getStack();


        if(!scopeHandler.isVariableDeclared(varname)){ // check if the variable exist or not 
            scopeHandler.declareVariable(varname); // if not , we have to init his value to 0 
            scopeHandler.assignVariable(varname,"0"); 

        }
        assignVarVal(varname,stack);

        // Condition to stay in the loop
        listInstructions.add("br label %"+forCond);
        listInstructions.add(forCond+":");
        ParseTree maxValTree= tree.getChildren().get(7);
        List<String> stackMaxVal = new Arithmetic(maxValTree).getStack();
        String compMemory = "comp"+scopeHandler.getCount();
        scopeHandler.declareVariable(compMemory);
        assignVarVal(compMemory,stackMaxVal);

        scopeHandler.increment();

        // save information for the cond
        scopeHandler.loadVariable(varname) ;
        String saveCounter = scopeHandler.getLastVar();
        scopeHandler.increment();

        scopeHandler.loadVariable(compMemory) ;
        String saveCompPos = scopeHandler.getLastVar();
        scopeHandler.increment();


        // First we need to know if we will go forward or backward 
        // meaning we have a positiv/negativ value to go by
        String forward = "forward" + scopeHandler.getCount();
        String backward = "backward" + scopeHandler.getCount();

        // if the value is positif , we need to forward
        listInstructions.add(scopeHandler.getLastVar()+" = icmp sgt i32 "+saveCompPos+", 0");
        listInstructions.add("br i1 "+scopeHandler.getLastVar()+", label %"+forward + ", label %"+backward);
        listInstructions.add("br label %"+forward);
        listInstructions.add(forward+":");
        scopeHandler.increment();
        scopeHandler.increment();

        // if the value is negativ we need to go backward 
        listInstructions.add(scopeHandler.getLastVar()+" = icmp sle i32 "+saveCounter+", "+saveCompPos);
        listInstructions.add("br i1 "+scopeHandler.getLastVar()+", label %"+forCode + ", label %"+forOut);
        scopeHandler.increment();

        listInstructions.add(backward+":");
        listInstructions.add(scopeHandler.getLastVar()+" = icmp sge i32 "+saveCounter+", "+saveCompPos);


        // cond for the for loop
        listInstructions.add("br i1 "+scopeHandler.getLastVar()+", label %"+forCode + ", label %"+forOut);
        listInstructions.add("br label %"+forCode);
        listInstructions.add(forCode+":");
        scopeHandler.increment();


        // here the code in the for loop
        ParseTree codeTree= tree.getChildren().get(9);
        buildCode(codeTree);

        // we need to increment our variable 
        ParseTree valueToAddTree= tree.getChildren().get(5);
        List<String> valueToAddStack = new Arithmetic(valueToAddTree).getStack();
        String valAddMemory = "addVal"+scopeHandler.getCount();
        scopeHandler.declareVariable(valAddMemory);
        assignVarVal(valAddMemory,valueToAddStack);
        scopeHandler.increment();
        scopeHandler.loadVariable(valAddMemory) ;
        String save = scopeHandler.getLastVar();
        scopeHandler.increment();


        // load the variable in case it was changed inside the loop code
        scopeHandler.loadVariable(varname) ;
        String saveCounterVar = scopeHandler.getLastVar(); 
        scopeHandler.increment();


        listInstructions.add(scopeHandler.getLastVar()+" = add i32 "+saveCounterVar+", "+save);
        scopeHandler.assignVariable(varname,scopeHandler.getLastVar());
        listInstructions.add("br label %"+forCond);

        //end of loop
        listInstructions.add(forOut+":");




    }


    /**
    * Add the print syntax to the LLVM code 
    * @param tree tree of the print instructio
    */
    private void buildPrint(ParseTree tree){
        String varname = tree.getChildren().get(2).getLabel().getValue().toString();
        scopeHandler.increment();
        scopeHandler.loadVariable(varname);
        scopeHandler.addPrintInstruction();
        
    }

    /**
    * Add the read syntax to the LLVM code 
    * @param tree tree of the read instructio
    */
    private void buildRead(ParseTree tree){
        String varname = tree.getChildren().get(2).getLabel().getValue().toString();
        scopeHandler.readInstruction(varname);
    }



    /**
     * Assign a varname with his value
     * @param variable varname
     * @param stack    list containing all the operation/number to do in order to have
     * the right value 
     */
    private void assignVarVal(String variable,List<String> stack){

        if (stack.size()>1){ // need some calcul
            ArithExpression expression = new ArithExpression(listInstructions,stack,scopeHandler);
            expression.calcul();
            scopeHandler.declareVariable(variable);
            scopeHandler.assignVariable(variable,scopeHandler.getLastVar());

        }else{ // simple assignation b:= 3 but we need to make the distinction between b:=3 and b:=a 
            try { // either a number or a varname
                Integer.valueOf(stack.get(0));
                scopeHandler.declareVariable(variable);
                scopeHandler.assignVariable(variable,stack.get(0));

            }catch (NumberFormatException ign){
                scopeHandler.declareVariable(variable);
                scopeHandler.simpleAssignationVarname(stack.get(0),variable);

            }


        }
    }


    /**
     * Init the list with the print/read instruction
     * taken from courses
     */
    private void initListInstruction(){
        list = Arrays.asList("@.strR = private unnamed_addr constant [3 x i8] c\"%d\\00\", align 1",
        "define i32 @readInt() {",
        "  %x = alloca i32, align 4",
        "  %1 = call i32 (i8*, ...) @__isoc99_scanf(i8* getelementptr inbounds ([3 x i8], [3 x i8]* @.strR, i32 0, i32 0), i32* %x)",
        "  %2 = load i32, i32* %x, align 4",
        "  ret i32 %2",
        "}",
        "declare i32 @__isoc99_scanf(i8*, ...)",
        "@.strP = private unnamed_addr constant [4 x i8] c\"%d\\0A\\00\", align 1",
        "define void @println(i32 %x) {",
        "  %1 = alloca i32, align 4",
        "  store i32 %x, i32* %1, align 4",
        "  %2 = load i32, i32* %1, align 4",
        "  %3 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.strP, i32 0, i32 0), i32 %2)",
        "  ret void",
        "}",
        "declare i32 @printf(i8*, ...)");
        if (listInstructions.size()==0){ // case where we have several function
        	listInstructions.addAll(list);
        }

    }

}
