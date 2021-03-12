public class Symbol{
	public static final int UNDEFINED_POSITION = -1;
	public static final Object NO_VALUE = null;
	
	private final LexicalUnit type;
	private final Object value;
	private final int line,column;

	public Symbol(LexicalUnit unit,int line,int column,Object value){
    this.type	= unit;
		this.line	= line+1;
		this.column	= column;
		this.value	= value;
	}
	
	public Symbol(LexicalUnit unit,int line,int column){
		this(unit,line,column,NO_VALUE);
	}
	
	public Symbol(LexicalUnit unit,int line){
		this(unit,line,UNDEFINED_POSITION,NO_VALUE);
	}
	
	public Symbol(LexicalUnit unit){
		this(unit,UNDEFINED_POSITION,UNDEFINED_POSITION,NO_VALUE);
	}

	public Symbol(String unit){ 
		this(null,UNDEFINED_POSITION,UNDEFINED_POSITION,unit);
	}
	
	public Symbol(LexicalUnit unit,Object value){
		this(unit,UNDEFINED_POSITION,UNDEFINED_POSITION,value);
	}

	public boolean isTerminal(){
		return this.type != null;
	}
	
	public boolean isNonTerminal(){
		return this.type == null;
	}

	public boolean isEpsilon(){ 
		return this.type == LexicalUnit.EPSILON;
	}

    public boolean isEOS(){ 
        return this.type == LexicalUnit.END_OF_STREAM;
    }

    /** 
     * LEFT_PARENTHESIS and RIGHT_PARENTHESIS will throw an error for latex because of "_" 
     * so we need to change the display
     */
    public boolean isParen(){ 
        return this.type == LexicalUnit.LEFT_PARENTHESIS || this.type == LexicalUnit.RIGHT_PARENTHESIS;
    }

    /** 
     * Greater_equal and smaller_equal will throw an error for latex because of "_" 
     * so we need to change the display
     */
    public boolean isCompEqual(){ 

        return this.type == LexicalUnit.GREATER_EQUAL || this.type == LexicalUnit.SMALLER_EQUAL;
    }


	public LexicalUnit getType(){
		return this.type;
	}
	
	public Object getValue(){
		return this.value;
	}
	
	public int getLine(){
		return this.line;
	}
	
	public int getColumn(){
		return this.column;
	}
	
	@Override
	public int hashCode(){
		final String value	= this.value != null? this.value.toString() : "null";
		final String type		= this.type  != null? this.type.toString()  : "null";
		return new String(value+"_"+type).hashCode();
	}
	
	@Override
	public String toString(){
		if(this.isTerminal()){
			final String value	= this.value != null? this.value.toString() : "null";
			final String type		= this.type  != null? this.type.toString()  : "null";
			return "token: "+value+"\tlexical unit: "+type;
		}
		return "Non-terminal symbol";
	}


}

