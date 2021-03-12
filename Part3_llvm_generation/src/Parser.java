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
     * more information about the rules. Create the derivation tree
     * if asked
     */
    public ParseTree startParser() throws IOException,HandleException{ 

    	ParseTree res = program();
       

       // part 2 print rules / write the parser tree
    
        /* if (commandLine.isLatex()){
            FileWriter fileWriter = new FileWriter(commandLine.getLatexFile());
            BufferedWriter bwTree = new BufferedWriter(fileWriter);
            bwTree.write(res.toLaTeXLua());
            bwTree.close();
            fileWriter.close();
        }

        
        
        String rules=""; // No need to print that 
        for(int rule : usedRule){
            rules += Integer.toString(rule) + " " ;
        } 

        
        System.out.println("Rules used : " + rules);
        if (commandLine.isVerboseOption()){
            System.out.println(" Here are all the grammar used with more detail : \n");
            for(int rule : usedRule){
                System.out.println(Grammar.getRule(rule) + "\n");
            } 

        }*/
        return res;

    }

    /**
     * Check if the next token match the lexicalUnit
     * @exception HandleException if the token does not correspond with
     *                                     input
     * @param lexical expected token type
     * @return ParseTree with single terminal child 
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
     * Method for the variable <Program>
     */
    private ParseTree program() throws IOException ,HandleException{ 
    	usedRule.add(1);  // First Rule  : <Program> -> begin <Code> end
    	   
        return new ParseTree("Program", Arrays.asList(match(LexicalUnit.BEG),progname(), code(), match(LexicalUnit.END), end()),1);//match(LexicalUnit.END_OF_STREAM)),1);

    }

    /** 
     * Method for the variable <end>
     */
    private ParseTree progname() throws IOException, HandleException {
    	switch (tokenAhead.getType()){
    		case PROGNAME:
		        return new ParseTree("52 PROGNAME",Arrays.asList(match(LexicalUnit.PROGNAME)),52);

    		default:
    			return null;
    	}
    }

    /** 
     * Method for the variable <end>
     */
    private ParseTree end() throws IOException, HandleException{
    	switch (tokenAhead.getType()){
    		// End -> END_OF_STREAM
    		case END_OF_STREAM:
            	return null;

    		// end -> Beg
    		case BEG:
    			return program();
    		default:
    			throw new HandleException(tokenAhead,Arrays.asList(LexicalUnit.BEG,
                                      LexicalUnit.END_OF_STREAM));
    	}
    }

    /** 
     * Method for the variable <Code>
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
    		return new ParseTree("3 Code", Arrays.asList(instList()),3); 
        //<Code> -> EPS
        default: 
        	usedRule.add(2);
            //return new ParseTree("Code", Arrays.asList(new ParseTree(new Symbol(LexicalUnit.EPSILON))),2); 
            return null; // EPS
        }
    
    }

    /** 
     * Method for the variable <InstList>
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
			return new ParseTree("4 instList", Arrays.asList(instruction(),instList_prim()),4);

		default:

            throw new HandleException(tokenAhead,Arrays.asList(LexicalUnit.VARNAME,
                                      LexicalUnit.IF,LexicalUnit.WHILE,
                                      LexicalUnit.FOR,LexicalUnit.PRINT,
                                      LexicalUnit.READ));

    	}

    }

    /** 
     * Method for the variable <InstListPrim>
     */
    private ParseTree instList_prim() throws IOException,HandleException {
        
    	//<InstListPrim> -> SEMICOLON  <instList> 

    	switch(tokenAhead.getType()) {
		case SEMICOLON:
			usedRule.add(5);
			return new ParseTree("5 instListPrim",Arrays.asList(match(LexicalUnit.SEMICOLON),instList()),5);
        //<InstListPrim> -> EPS
        case END: // Can't have ; before and "END / ENDWHILE / ENDIF / ELSE" 
        case ENDWHILE:
        case ENDIF:
        case ELSE:
        case ELIF:
            usedRule.add(6);
        	   
            return null;//new ParseTree("instListPrim",Arrays.asList(new ParseTree(new Symbol(LexicalUnit.EPSILON))),6);
        default:
            throw new HandleException(tokenAhead,Arrays.asList(LexicalUnit.SEMICOLON,
                                      LexicalUnit.END,LexicalUnit.ENDWHILE,LexicalUnit.ENDIF));
        }
    }

    /** 
     * Method for the variable <Instruction>
     */
    private ParseTree instruction() throws IOException,HandleException {
    	switch(tokenAhead.getType()){
    	// <Instruction> -> <Assign>
		case VARNAME:
			usedRule.add(7);
			return new ParseTree("7 instruction",Arrays.asList(assign()),7);
		// <Instruction> -> <IF>
		case IF:
			usedRule.add(8);
			return new ParseTree("8 instruction",Arrays.asList(ifState()),8);
		// <Instruction> -> <WHILE>
		case WHILE:
			usedRule.add(9);
			return new ParseTree("9 instruction",Arrays.asList(whileState()),9);
		// <Instruction> -> <FOR>
		case FOR:
			usedRule.add(10);
			return new ParseTree("10 instruction",Arrays.asList(forState()),10);
		// <Instruction> -> <PRINT>
		case PRINT:
			usedRule.add(11);
			return new ParseTree("11 instruction",Arrays.asList(printState()),11);
		// <Instruction> -> <READ>
		case READ:
			usedRule.add(12);
			return new ParseTree("12 instruction",Arrays.asList(readState()),12);
		default:
            throw new HandleException(tokenAhead,Arrays.asList(LexicalUnit.VARNAME,
                                     LexicalUnit.IF,LexicalUnit.WHILE,LexicalUnit.FOR,
                                     LexicalUnit.PRINT,LexicalUnit.READ));
    	}
    	 
    }

    /** 
     * Method for the variable <Assign>
     */
    private ParseTree assign() throws IOException,HandleException{
        // Assign -> VARNAME ASSIGN ExprArith
        usedRule.add(13);
           
        return new ParseTree("13 assign",Arrays.asList(match(LexicalUnit.VARNAME),match(LexicalUnit.ASSIGN),exprArith()),13);
    }




 
 

 


    /** 
     * Method for the variable <ExprArith>
     */
    private ParseTree exprArith() throws IOException,HandleException{
        //  <ExprArith> -> <Term> <ExprArith_prim>
        usedRule.add(14);
        return new ParseTree("14 exprArith",Arrays.asList(term(),exprArith_prim()),14);
    }

    /** 
     * Method for the variable <Term>
     */
    private ParseTree term() throws IOException,HandleException{
        //  <Term> -> <Atom> <prodPrim>
        usedRule.add(15);
        return new ParseTree("15 term",
                             Arrays.asList(
                                           atom(),
                                           prodPrim()
                                           ),15);
    }
    /** 
     * Method for the variable <Atom>
     */
    private ParseTree atom() throws IOException,HandleException{
        switch(tokenAhead.getType()) {
         
            // <Atom> -> <MINUS> <Atom>
        case MINUS:
        	usedRule.add(16);
            return new ParseTree("16 atom",
                                 Arrays.asList(
                                               match(LexicalUnit.MINUS),
                                               atom()
                                               ),16);
            //<Atom> -> <VARNAME>
        case VARNAME:
        	usedRule.add(17);
            return new ParseTree("17 atom",
                                 Arrays.asList(match(LexicalUnit.VARNAME)),17);
            // <Atom> -> <NUMBER>
        case NUMBER:
        	usedRule.add(18);
            return new ParseTree("18 atom",
                                 Arrays.asList(match(LexicalUnit.NUMBER)),18);
            // <Atom> -> <LEFT_PARENTHESIS> <ExprArith> <RIGHT_PARENTHESIS>
        case LEFT_PARENTHESIS:
        	usedRule.add(19);
            return new ParseTree("19 atom",
                                 Arrays.asList(
                                               match(LexicalUnit.LEFT_PARENTHESIS),
                                               exprArith(),
                                               match(LexicalUnit.RIGHT_PARENTHESIS)
                                               ),19);
        default:
            throw new HandleException(tokenAhead,Arrays.asList(LexicalUnit.MINUS,
                                      LexicalUnit.VARNAME,LexicalUnit.NUMBER,
                                      LexicalUnit.LEFT_PARENTHESIS));

        }

    }

 

    /** 
     * Method for the variable <ExprArith_prim>
     */
    private ParseTree exprArith_prim() throws IOException,HandleException{
        switch(tokenAhead.getType()) {
            // <ExprArith_prim> -> <AddOp> <Term> <ExprArith_prim>
            case PLUS:
            case MINUS:
            	usedRule.add(20);

                return new ParseTree("20 exprArithPrim",
                                     Arrays.asList(
                                                   addOp(),
                                                   term(),
                                                   exprArith_prim()
                                                   ),20);
                // <ExprArith_prim> -> EPSILON
            default:
            	usedRule.add(21);
                return null;//new ParseTree("exprArithPrim", Arrays.asList(new ParseTree(new Symbol(LexicalUnit.EPSILON))),21);
        }
    }

    /** 
     * Method for the variable <prodPrim>
     */
    private ParseTree prodPrim() throws IOException,HandleException{
        switch(tokenAhead.getType()) {
            // <prodPrim> -> <MultOp> <Atom> <prodPrim>
        case TIMES:
        case DIVIDE:
        	usedRule.add(22);
            return new ParseTree("22 prodPrim",
                                 Arrays.asList(
                                               multOp(),
                                               atom(),
                                               prodPrim()
                                               ),22);
            // <prodPrim> -> EPSILON
        default:
        	usedRule.add(23);
            return null;//new ParseTree("prodPrim", Arrays.asList(new ParseTree(new Symbol(LexicalUnit.EPSILON))),23);
        }
    }

    /** 
     * Method for the variable <addOp>
     */
    private ParseTree addOp() throws IOException,HandleException{
    	   
        switch(tokenAhead.getType()) {
        
        case PLUS:
        	// <addOp> -> +
        	usedRule.add(24);
            return new ParseTree("24 addOp", Arrays.asList(match(LexicalUnit.PLUS)),24);
        case MINUS:
        	// <addOp> -> -
        	usedRule.add(25);
            return new ParseTree("25 addOp", Arrays.asList(match(LexicalUnit.MINUS)),25);
        default:
            throw new HandleException(tokenAhead,Arrays.asList(LexicalUnit.PLUS,LexicalUnit.MINUS));
        }
    }

    /** 
     * Method for the variable <multOp>
     */
    private ParseTree multOp() throws IOException,HandleException{
    	   
        switch(tokenAhead.getType()) {
        
        case TIMES:
        	// <multOp> -> *
        	usedRule.add(26);
            return new ParseTree("26 multOp", Arrays.asList(match(LexicalUnit.TIMES)),26);
        case DIVIDE:
        	// <multOp> -> /
        	usedRule.add(27);
            return new ParseTree("27 multOp", Arrays.asList(match(LexicalUnit.DIVIDE)),27);
        default:
            throw new HandleException(tokenAhead,Arrays.asList(LexicalUnit.TIMES,LexicalUnit.DIVIDE));
        }
    }

    /** 
     * Method for the variable <If>
     */
    private ParseTree ifState() throws IOException,HandleException{
    	// If -> if <cond> then <code> <ifPrim> 
        usedRule.add(28);
           
        return new ParseTree("28 if", Arrays.asList(match(LexicalUnit.IF),cond(),match(LexicalUnit.THEN)
        	,code(), ifPrim()),28);
    }

    /** 
     * Method for the variable <ifPrim>
     */
    private ParseTree ifPrim() throws IOException,HandleException {
    	   
        switch (tokenAhead.getType()) {
        
        // ifPrim -> endif
        case ENDIF:
            usedRule.add(29);
            return new ParseTree("29 ifPrim", Arrays.asList(match(LexicalUnit.ENDIF)),29);
        // ifPrim -> else  <code> endif
        case ELSE:
            usedRule.add(30);
            return new ParseTree("30 ifPrim", Arrays.asList(match(LexicalUnit.ELSE),
                    code(), match(LexicalUnit.ENDIF)),30);
        // ifPrim -> elif <cond> then <code> <ifPrim> 
        case ELIF:
            usedRule.add(53);
            return new ParseTree("53 elif", Arrays.asList(match(LexicalUnit.ELIF),cond(),match(LexicalUnit.THEN)
                ,code(), ifPrim()),53);
        default:
            throw new HandleException(tokenAhead,Arrays.asList(LexicalUnit.ENDIF,LexicalUnit.ELSE));
        }
    }

    /** 
     * Method for the variable <Cond>
     */
    private ParseTree cond() throws IOException,HandleException{
    	// <Cond> -> <andCond> <condPrim>
    	usedRule.add(31);
    	return new ParseTree("31 cond", Arrays.asList(andCond(), condPrim()),31);
    }


    /** 
     * Method for the variable <condPrim>
     */
    private ParseTree condPrim() throws IOException,HandleException {
    	// condPrim -> or andCond condPrim
        switch(tokenAhead.getType()) {
        case OR:
           usedRule.add(32);
              
           return new ParseTree("32 condPrim", Arrays.asList(match(LexicalUnit.OR), andCond(), condPrim()),32);
        default:
        	// condPrim -> EPSILON
	        usedRule.add(33);
    	    return null;//new ParseTree("condPrim", Arrays.asList(new ParseTree(new Symbol(LexicalUnit.EPSILON))),33);
        }
    }

    /** 
     * Method for the variable <andCond>
     */
    private ParseTree andCond()  throws IOException ,HandleException{
    	// <andCond> -> <simpleCond> <andCondPrim>
        usedRule.add(34);
        return new ParseTree("34 andCond", Arrays.asList(simpleCond(), andCondPrim()),34);
    }

    /** 
     * Method for the variable <andCondPrim>
     */
    private ParseTree andCondPrim()  throws IOException,HandleException {

    	switch(tokenAhead.getType()){
    		// <andCondPrim> -> and <simpleCond> <andCondPrim>
    	case AND:
    		usedRule.add(35);
    		   
            return new ParseTree("35 andCondPrim",
                    Arrays.asList(match(LexicalUnit.AND), simpleCond(), andCondPrim()),35);
        default:
        	// andCondPrim -> EPSILON
        	usedRule.add(36);
        	return null;//new ParseTree("andCondPrim", Arrays.asList(new ParseTree(new Symbol(LexicalUnit.EPSILON))),36);
    	}
    }

    /** 
     * Method for the variable <simpleCond>
     */
    private ParseTree simpleCond()  throws IOException,HandleException {
        if (tokenAhead.getType() != LexicalUnit.NOT) {
           // <simpleCond> -> <exprArith> <comp> <exprArith>
           usedRule.add(37);
           return new ParseTree("37 simpleCond", Arrays.asList(exprArith(), comp(), exprArith()),37);

        } else {
        	// <simpleCond> -> not <simpleCond>
            usedRule.add(38);
               
            return new ParseTree("38 simpleCond", Arrays.asList(match(LexicalUnit.NOT), simpleCond()),38);
        }
    }

    /** 
     * Method for the variable <Comp>
     */
    private ParseTree comp() throws IOException,HandleException{
    	   
        switch(tokenAhead.getType()) {
        
            // Comp -> = 
        case EQUAL:
        	usedRule.add(39);
            return new ParseTree("39 comp", Arrays.asList(match(LexicalUnit.EQUAL)),39);
            // Comp -> >=
        case GREATER_EQUAL:
        	usedRule.add(40);
            return new ParseTree("40 comp", Arrays.asList(match(LexicalUnit.GREATER_EQUAL)),40);
            // Comp -> >
        case GREATER:
        	usedRule.add(41);
            return new ParseTree("41 comp", Arrays.asList(match(LexicalUnit.GREATER)),41);
            // Comp -> <=
        case SMALLER_EQUAL:
        	usedRule.add(42);
            return new ParseTree("42 comp", Arrays.asList(match(LexicalUnit.SMALLER_EQUAL)),42);
            // Comp -> <
        case SMALLER:
        	usedRule.add(43);
            return new ParseTree("43 comp", Arrays.asList(match(LexicalUnit.SMALLER)),43);
            // Comp -> /=
        case DIFFERENT:
        	usedRule.add(44);
            return new ParseTree("44 comp", Arrays.asList(match(LexicalUnit.DIFFERENT)),44);
        default:
            throw new HandleException(tokenAhead,Arrays.asList(LexicalUnit.EQUAL,
                                     LexicalUnit.GREATER_EQUAL,LexicalUnit.GREATER,
                                     LexicalUnit.SMALLER_EQUAL,LexicalUnit.SMALLER,
                                     LexicalUnit.DIFFERENT));
        }
    }

    /** 
     * Method for the variable <while>
     */
    private ParseTree whileState() throws IOException,HandleException{
    	// <while> -> while <Cond> do <code> endwhile
        usedRule.add(45);
           
        return new ParseTree("45 while",
                Arrays.asList(match(LexicalUnit.WHILE), cond(), match(LexicalUnit.DO),code(), match(LexicalUnit.ENDWHILE)),45);
    }

    /** 
     * Method for the variable <For>
     */
    private ParseTree forState() throws IOException,HandleException {
    	// <For> -> for [Varname] from <exprArith> by <ExprArith> to <exprArith> do <Code> endwhile
        usedRule.add(46);
           
        return new ParseTree("46 for",
                Arrays.asList(match(LexicalUnit.FOR), match(LexicalUnit.VARNAME),match(LexicalUnit.FROM),exprArith(), match(LexicalUnit.BY), exprArith(), match(LexicalUnit.TO),
                        exprArith(),match(LexicalUnit.DO),code(), match(LexicalUnit.ENDWHILE)),46);
    }

    /** 
     * Method for the variable <Print>
     */
    private ParseTree printState() throws IOException ,HandleException{
    	// <Print> -> print([Varname])
        usedRule.add(47);
           
        return new ParseTree("47 print", Arrays.asList(match(LexicalUnit.PRINT), match(LexicalUnit.LEFT_PARENTHESIS), 
        	match(LexicalUnit.VARNAME),match(LexicalUnit.RIGHT_PARENTHESIS)),47);
    }

    /** 
     * Method for the variable <Read>
     */
    private ParseTree readState() throws IOException,HandleException {
    	//<Read> -> read([Varname])
        usedRule.add(48);
           
        return new ParseTree("48 read", Arrays.asList(match(LexicalUnit.READ), match(LexicalUnit.LEFT_PARENTHESIS), 
        	match(LexicalUnit.VARNAME),match(LexicalUnit.RIGHT_PARENTHESIS)),48);
    }
}
