import java.util.Iterator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.io.BufferedWriter;
import java.io.FileWriter;




/**
 * Recursive descent LL(1) parser
 */
public class Parser{


	private Symbol tokenAhead;
	private List<Integer> usedRule;
	private Iterator<Symbol> symbolIter;
    private CommandLine commandLine;

    
    public Parser(Iterator<Symbol> iterator, CommandLine commandLine) { 
    	this.symbolIter = iterator;
    	this.tokenAhead = iterator.next();
    	this.usedRule = new ArrayList<Integer>();
        this.commandLine = commandLine;

    }


    /**
     * Start the parsing and display the rules used
     * Read the argument passed in command line and display
     * more information about the rules. Create the derivation tree if asked
     * @throws IOException     
     * @throws HandleException in case of non respect of the grammar
     */
    public void startParser() throws IOException,HandleException{ 

    	ParseTree res = program();
       
    
        if (commandLine.isLatex()){
            FileWriter fileWriter = new FileWriter(commandLine.getLatexFile());
            BufferedWriter bwTree = new BufferedWriter(fileWriter);
            bwTree.write(res.toLaTeXLua());
            bwTree.close();
            fileWriter.close();
        }

        
        
        String rules="";
        for(int rule : usedRule){
            rules += Integer.toString(rule) + " " ;
        } 

        
        System.out.println("Rules used : " + rules);
        if (commandLine.isVerboseOption()){
            System.out.println("Here are all the grammar used with more detail : \n");
            for(int rule : usedRule){
                System.out.println(Grammar.getRule(rule) + "\n");
            } 

        }

    }
    
    /**
     * Check if the next token match the lexicalUnit
     * @param lexical expected token type 
     * @return ParseTree with single terminal child 
     * @throws IOException     
     * @throws HandleException in case of non respect of the grammar
     */
    private ParseTree match (LexicalUnit lexical) throws IOException,HandleException{ 
			
    	if (tokenAhead.getType() == lexical ) {

    		ParseTree res = new ParseTree(tokenAhead);
            if (tokenAhead.getType() != LexicalUnit.END_OF_STREAM){ // end of file
        		tokenAhead = this.symbolIter.next();}
    		return res ;
    	}
        throw new HandleException(tokenAhead,Arrays.asList(lexical));
    	
    }





    /**
     * Method for the state <Program>
     */
    private ParseTree program() throws IOException ,HandleException{ 
    	usedRule.add(1);  // First Rule  : <Program> -> begin <Code> end
        return new ParseTree("Program", Arrays.asList(match(LexicalUnit.BEG), code(),match(LexicalUnit.END), match(LexicalUnit.END_OF_STREAM)));

    }

    /** 
     * Method for the state <Code>
     */
    private ParseTree code() throws IOException,HandleException{

        switch(tokenAhead.getType()) { 

        //<Code> -> <InstList>
		case VARNAME:
		case IF:
		case WHILE:
		case FOR:
		case PRINT:
		case READ:
    		usedRule.add(3);
    		return new ParseTree("Code", Arrays.asList(instList())); 
        //<Code> -> EPS
        default: 
        	usedRule.add(2);
            return new ParseTree("Code", Arrays.asList(new ParseTree(new Symbol(LexicalUnit.EPSILON)))); 

        }
    
    }

    /** 
     * Method for the state <InstList>
     */
    private ParseTree instList() throws IOException,HandleException{
    	//<InstList> -> <Instruction> InstList_prim
        
    	switch (tokenAhead.getType()) {
		case VARNAME:
		case IF:
		case WHILE:
		case FOR:
		case PRINT:
		case READ:
			usedRule.add(4);
			return new ParseTree("instList", Arrays.asList(instruction(),instList_prim()));

		default:

            throw new HandleException(tokenAhead,Arrays.asList(LexicalUnit.VARNAME,
                                      LexicalUnit.IF,LexicalUnit.WHILE,
                                      LexicalUnit.FOR,LexicalUnit.PRINT,
                                      LexicalUnit.READ));

    	}

    }

    /** 
     * Method for the state <InstListPrim>
     */
    private ParseTree instList_prim() throws IOException,HandleException {
        
    	//<InstListPrim> -> SEMICOLON  <instList> 

    	switch(tokenAhead.getType()) {
		case SEMICOLON:
			usedRule.add(5);
			return new ParseTree("instListPrim",Arrays.asList(match(LexicalUnit.SEMICOLON),instList()));
        //<InstListPrim> -> EPS
        case END: // Can't have ; before and "END / ENDWHILE / ENDIF / ELSE" 
        case ENDWHILE:
        case ENDIF:
        case ELSE:
            usedRule.add(6);
            return new ParseTree("instListPrim",Arrays.asList(new ParseTree(new Symbol(LexicalUnit.EPSILON))));
        default:
            throw new HandleException(tokenAhead,Arrays.asList(LexicalUnit.SEMICOLON,
                                      LexicalUnit.END,LexicalUnit.ENDWHILE,LexicalUnit.ENDIF));
        }
    }

    /** 
     * Method for the state <Instruction>
     */
    private ParseTree instruction() throws IOException,HandleException {
    	switch(tokenAhead.getType()){
    	// <Instruction> -> <Assign>
		case VARNAME:
			usedRule.add(7);
			return new ParseTree("instruction",Arrays.asList(assign()));
		// <Instruction> -> <IF>
		case IF:
			usedRule.add(8);
			return new ParseTree("instruction",Arrays.asList(ifState()));
		// <Instruction> -> <WHILE>
		case WHILE:
			usedRule.add(9);
			return new ParseTree("instruction",Arrays.asList(whileState()));
		// <Instruction> -> <FOR>
		case FOR:
			usedRule.add(10);
			return new ParseTree("instruction",Arrays.asList(forState()));
		// <Instruction> -> <PRINT>
		case PRINT:
			usedRule.add(11);
			return new ParseTree("instruction",Arrays.asList(printState()));
		// <Instruction> -> <READ>
		case READ:
			usedRule.add(12);
			return new ParseTree("instruction",Arrays.asList(readState()));
		default:
            throw new HandleException(tokenAhead,Arrays.asList(LexicalUnit.VARNAME,
                                     LexicalUnit.IF,LexicalUnit.WHILE,LexicalUnit.FOR,
                                     LexicalUnit.PRINT,LexicalUnit.READ));
    	}
    }

    /** 
     * Method for the state <Assign>
     */
    private ParseTree assign() throws IOException,HandleException{
        // Assign -> VARNAME ASSIGN ExprArith
        usedRule.add(13);
        return new ParseTree("assign",Arrays.asList(match(LexicalUnit.VARNAME),match(LexicalUnit.ASSIGN),exprArith()));
    }




 
 

 


    /** 
     * Method for the state <ExprArith>
     */
    private ParseTree exprArith() throws IOException,HandleException{
        //  <ExprArith> -> <Term> <ExprArith_prim>
        usedRule.add(14);
        return new ParseTree("exprArith",Arrays.asList(term(),exprArith_prim()));
    }

    /** 
     * Method for the variable <Term>
     */
    private ParseTree term() throws IOException,HandleException{
        //  <Term> -> <Atom> <prodPrim>
        usedRule.add(15);
        return new ParseTree("term",
                             Arrays.asList(
                                           atom(),
                                           prodPrim()
                                           ));
    }
    /** 
     * Method for the state <Atom>
     */
    private ParseTree atom() throws IOException,HandleException{
        switch(tokenAhead.getType()) {
            // <Atom> -> <MINUS> <Atom>
        case MINUS:
        	usedRule.add(16);
            return new ParseTree("atom",
                                 Arrays.asList(
                                               match(LexicalUnit.MINUS),
                                               atom()
                                               ));
            //<Atom> -> <VARNAME>
        case VARNAME:
        	usedRule.add(17);
            return new ParseTree("atom",
                                 Arrays.asList(match(LexicalUnit.VARNAME)));
            // <Atom> -> <NUMBER>
        case NUMBER:
        	usedRule.add(18);
            return new ParseTree("atom",
                                 Arrays.asList(match(LexicalUnit.NUMBER)));
            // <Atom> -> <LEFT_PARENTHESIS> <ExprArith> <RIGHT_PARENTHESIS>
        case LEFT_PARENTHESIS:
        	usedRule.add(19);
            return new ParseTree("atom",
                                 Arrays.asList(
                                               match(LexicalUnit.LEFT_PARENTHESIS),
                                               exprArith(),
                                               match(LexicalUnit.RIGHT_PARENTHESIS)
                                               ));
        default:
            throw new HandleException(tokenAhead,Arrays.asList(LexicalUnit.MINUS,
                                      LexicalUnit.VARNAME,LexicalUnit.NUMBER,
                                      LexicalUnit.LEFT_PARENTHESIS));
        }
    }

 

    /** 
     * Method for the state <ExprArith_prim>
     */
    private ParseTree exprArith_prim() throws IOException,HandleException{
        switch(tokenAhead.getType()) {
            // <ExprArith_prim> -> <AddOp> <Term> <ExprArith_prim>
            case PLUS:
            case MINUS:
            	usedRule.add(20);
                return new ParseTree("exprArithPrim",
                                     Arrays.asList(
                                                   addOp(),
                                                   term(),
                                                   exprArith_prim()
                                                   ));
                // <ExprArith_prim> -> EPSILON
            default:
            	usedRule.add(21);
                return new ParseTree("exprArithPrim", Arrays.asList(new ParseTree(new Symbol(LexicalUnit.EPSILON))));
        }
    }

    /** 
     * Method for the state <prodPrim>
     */
    private ParseTree prodPrim() throws IOException,HandleException{
        switch(tokenAhead.getType()) {
            // <prodPrim> -> <MultOp> <Atom> <prodPrim>
        case TIMES:
        case DIVIDE:
        	usedRule.add(22);
            return new ParseTree("prodPrim",
                                 Arrays.asList(
                                               multOp(),
                                               atom(),
                                               prodPrim()
                                               ));
            // <prodPrim> -> EPSILON
        default:
        	usedRule.add(23);
            return new ParseTree("prodPrim", Arrays.asList(new ParseTree(new Symbol(LexicalUnit.EPSILON))));
        }
    }

    /** 
     * Method for the state <addOp>
     */
    private ParseTree addOp() throws IOException,HandleException{
        switch(tokenAhead.getType()) {
        case PLUS:
        	// <addOp> -> +
        	usedRule.add(24);
            return new ParseTree("addOp", Arrays.asList(match(LexicalUnit.PLUS)));
        case MINUS:
        	// <addOp> -> -
        	usedRule.add(25);
            return new ParseTree("addOp", Arrays.asList(match(LexicalUnit.MINUS)));
        default:
            throw new HandleException(tokenAhead,Arrays.asList(LexicalUnit.PLUS,LexicalUnit.MINUS));
        }
    }

    /** 
     * Method for the state <multOp>
     */
    private ParseTree multOp() throws IOException,HandleException{
        switch(tokenAhead.getType()) {
        case TIMES:
        	// <multOp> -> *
        	usedRule.add(26);
            return new ParseTree("multOp", Arrays.asList(match(LexicalUnit.TIMES)));
        case DIVIDE:
        	// <multOp> -> /
        	usedRule.add(27);
            return new ParseTree("multOp", Arrays.asList(match(LexicalUnit.DIVIDE)));
        default:
            throw new HandleException(tokenAhead,Arrays.asList(LexicalUnit.TIMES,LexicalUnit.DIVIDE));
        }
    }

    /** 
     * Method for the state <If>
     */
    private ParseTree ifState() throws IOException,HandleException{
    	// If -> if <cond> then <code> <ifPrim> 
        usedRule.add(28);
        return new ParseTree("if", Arrays.asList(match(LexicalUnit.IF),cond(),match(LexicalUnit.THEN)
        	,code(), ifPrim()));
    }

    /** 
     * Method for the state <ifPrim>
     */
    private ParseTree ifPrim() throws IOException,HandleException {
        switch (tokenAhead.getType()) {
        // ifPrim -> endif
        case ENDIF:
            usedRule.add(29);
            return new ParseTree("ifPrim", Arrays.asList(match(LexicalUnit.ENDIF)));
        // ifPrim -> else  <code> endif
        case ELSE:
            usedRule.add(30);
            return new ParseTree("ifPrim", Arrays.asList(match(LexicalUnit.ELSE),
                    code(), match(LexicalUnit.ENDIF)));
        default:
            throw new HandleException(tokenAhead,Arrays.asList(LexicalUnit.ENDIF,LexicalUnit.ELSE));
        }
    }

    /** 
     * Method for the state <Cond>
     */
    private ParseTree cond() throws IOException,HandleException{
    	// <Cond> -> <andCond> <condPrim>
    	usedRule.add(31);
    	return new ParseTree("cond", Arrays.asList(andCond(), condPrim()));
    }


    /** 
     * Method for the state <condPrim>
     */
    private ParseTree condPrim() throws IOException,HandleException {
    	// condPrim -> or andCond condPrim
        switch(tokenAhead.getType()) {
        case OR:
           usedRule.add(32);
           return new ParseTree("condPrim", Arrays.asList(match(LexicalUnit.OR), andCond(), condPrim()));
        default:
        	// condPrim -> EPSILON
	        usedRule.add(33);
    	    return new ParseTree("condPrim", Arrays.asList(new ParseTree(new Symbol(LexicalUnit.EPSILON))));
        }
    }

    /** 
     * Method for the state <andCond>
     */
    private ParseTree andCond()  throws IOException ,HandleException{
    	// <andCond> -> <simpleCond> <andCondPrim>
        usedRule.add(34);
        return new ParseTree("andCond", Arrays.asList(simpleCond(), andCondPrim()));
    }

    /** 
     * Method for the state <andCondPrim>
     */
    private ParseTree andCondPrim()  throws IOException,HandleException {

    	switch(tokenAhead.getType()){
    		// <andCondPrim> -> and <simpleCond> <andCondPrim>
    	case AND:
    		usedRule.add(35);
            return new ParseTree("andCondPrim",
                    Arrays.asList(match(LexicalUnit.AND), simpleCond(), andCondPrim()));
        default:
        	// andCondPrim -> EPSILON
        	usedRule.add(36);
        	return new ParseTree("andCondPrim", Arrays.asList(new ParseTree(new Symbol(LexicalUnit.EPSILON))));
    	}
    }

    /** 
     * Method for the state <simpleCond>
     */
    private ParseTree simpleCond()  throws IOException,HandleException {
        if (tokenAhead.getType() != LexicalUnit.NOT) {
           // <simpleCond> -> <exprArith> <comp> <exprArith>
           usedRule.add(37);
           return new ParseTree("simpleCond", Arrays.asList(exprArith(), comp(), exprArith()));

        } else {
        	// <simpleCond> -> not <simpleCond>
            usedRule.add(38);
            return new ParseTree("simpleCond", Arrays.asList(match(LexicalUnit.NOT), simpleCond()));
        }
    }

    /** 
     * Method for the state <Comp>
     */
    private ParseTree comp() throws IOException,HandleException{
        switch(tokenAhead.getType()) {
            // Comp -> = 
        case EQUAL:
        	usedRule.add(39);
            return new ParseTree("comp", Arrays.asList(match(LexicalUnit.EQUAL)));
            // Comp -> >=
        case GREATER_EQUAL:
        	usedRule.add(40);
            return new ParseTree("comp", Arrays.asList(match(LexicalUnit.GREATER_EQUAL)));
            // Comp -> >
        case GREATER:
        	usedRule.add(41);
            return new ParseTree("comp", Arrays.asList(match(LexicalUnit.GREATER)));
            // Comp -> <=
        case SMALLER_EQUAL:
        	usedRule.add(42);
            return new ParseTree("comp", Arrays.asList(match(LexicalUnit.SMALLER_EQUAL)));
            // Comp -> <
        case SMALLER:
        	usedRule.add(43);
            return new ParseTree("comp", Arrays.asList(match(LexicalUnit.SMALLER)));
            // Comp -> /=
        case DIFFERENT:
        	usedRule.add(44);
            return new ParseTree("comp", Arrays.asList(match(LexicalUnit.DIFFERENT)));
        default:
            throw new HandleException(tokenAhead,Arrays.asList(LexicalUnit.EQUAL,
                                     LexicalUnit.GREATER_EQUAL,LexicalUnit.GREATER,
                                     LexicalUnit.SMALLER_EQUAL,LexicalUnit.SMALLER,
                                     LexicalUnit.DIFFERENT));
        }
    }

    /** 
     * Method for the state <while>
     */
    private ParseTree whileState() throws IOException,HandleException{
    	// <while> -> while <Cond> do <code> endwhile
        usedRule.add(45);
        return new ParseTree("while",
                Arrays.asList(match(LexicalUnit.WHILE), cond(), match(LexicalUnit.DO),code(), match(LexicalUnit.ENDWHILE)));
    }

    /** 
     * Method for the state <For>
     */
    private ParseTree forState() throws IOException,HandleException {
    	// <For> -> for [Varname] from <exprArith> by <ExprArith> to <exprArith> do <Code> endwhile
        usedRule.add(46);
        return new ParseTree("for",
                Arrays.asList(match(LexicalUnit.FOR), match(LexicalUnit.VARNAME),match(LexicalUnit.FROM),exprArith(), match(LexicalUnit.BY), exprArith(), match(LexicalUnit.TO),
                        exprArith(),match(LexicalUnit.DO),code(), match(LexicalUnit.ENDWHILE)));
    }

    /** 
     * Method for the state <Print>
     */
    private ParseTree printState() throws IOException ,HandleException{
    	// <Print> -> print([Varname])
        usedRule.add(47);
        return new ParseTree("print", Arrays.asList(match(LexicalUnit.PRINT), match(LexicalUnit.LEFT_PARENTHESIS), 
        	match(LexicalUnit.VARNAME),match(LexicalUnit.RIGHT_PARENTHESIS)));
    }

    /** 
     * Method for the state <Read>
     */
    private ParseTree readState() throws IOException,HandleException {
    	//<Read> -> read([Varname])
        usedRule.add(48);
        return new ParseTree("read", Arrays.asList(match(LexicalUnit.READ), match(LexicalUnit.LEFT_PARENTHESIS), 
        	match(LexicalUnit.VARNAME),match(LexicalUnit.RIGHT_PARENTHESIS)));
    }
}
