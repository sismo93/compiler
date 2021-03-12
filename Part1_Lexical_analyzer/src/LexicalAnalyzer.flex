import java.util.regex.PatternSyntaxException;

%%// Options of the scanner

%class LexicalAnalyzer	//Name
%unicode			//Use unicode
%line         //Use line counter (yyline variable)
%column       //Use character counter by line (yycolumn variable)
%type Symbol  //Says that the return type is Symbol


//Return value of the program
%eofval{
	return new Symbol(LexicalUnit.END_OF_STREAM, yyline, yycolumn);
%eofval}

//Extended Regular Expressions

AlphaLowerCase    = [a-z]
Numeric           = [0-9]
AlphaLowerNumeric = {AlphaLowerCase}|{Numeric}

//We consider here only lowercase alpha
VarName           = ({AlphaLowerCase})({AlphaLowerNumeric})*

Number         = ([1-9][0-9]*)|0

// Comment
ShortComment      = "co"
LongComment       = "CO"
// Space and Endline
EndLine           = "\n"
CarriageReturn    = "\r"
ReturnLine        = ({EndLine}{CarriageReturn}?) | ({CarriageReturn}{EndLine}?)
Space             = (\t | " ")
Ignore            = {Space}|{ReturnLine} 




//Declare exclusive states
%xstate YYINITIAL, SHORTCOMMENT, LONGCOMMENT


%% // Identification of tokens
<YYINITIAL> {

    // Comment
    {ShortComment}  {yybegin(SHORTCOMMENT);} 
    {LongComment}   {yybegin(LONGCOMMENT);} 


    "begin"         {return new Symbol(LexicalUnit.BEG, yyline, yycolumn, yytext());}
    "end"           {return new Symbol(LexicalUnit.END, yyline, yycolumn, yytext());}
    ";"             {return new Symbol(LexicalUnit.SEMICOLON, yyline, yycolumn, yytext());} 
    // Assignation
    ":="            {return new Symbol(LexicalUnit.ASSIGN, yyline, yycolumn, yytext());}
    // Comparison operators
    "="             {return new Symbol(LexicalUnit.EQUAL, yyline, yycolumn, yytext());}
    "/="            {return new Symbol(LexicalUnit.DIFFERENT, yyline, yycolumn, yytext());}
    ">"             {return new Symbol(LexicalUnit.GREATER, yyline, yycolumn, yytext());}
    ">="            {return new Symbol(LexicalUnit.GREATER_EQUAL, yyline, yycolumn, yytext());}
    "<"             {return new Symbol(LexicalUnit.SMALLER, yyline, yycolumn, yytext());}
    "<="            {return new Symbol(LexicalUnit.SMALLER_EQUAL, yyline, yycolumn, yytext());}
    // Parenthesis
    "("             {return new Symbol(LexicalUnit.LEFT_PARENTHESIS, yyline, yycolumn, yytext());}
    ")"             {return new Symbol(LexicalUnit.RIGHT_PARENTHESIS, yyline, yycolumn, yytext());}
    // Conditional 
    "if"            {return new Symbol(LexicalUnit.IF, yyline, yycolumn, yytext());}
    "then"          {return new Symbol(LexicalUnit.THEN, yyline, yycolumn, yytext());}
    "endif"         {return new Symbol(LexicalUnit.ENDIF, yyline, yycolumn, yytext());}
    "else"          {return new Symbol(LexicalUnit.ELSE, yyline, yycolumn, yytext());}
    // IO keywords
    "print"         {return new Symbol(LexicalUnit.PRINT, yyline, yycolumn, yytext());}
    "read"          {return new Symbol(LexicalUnit.READ, yyline, yycolumn, yytext());}
    // OP signs
    "+"             {return new Symbol(LexicalUnit.PLUS, yyline, yycolumn, yytext());}
    "-"             {return new Symbol(LexicalUnit.MINUS, yyline, yycolumn, yytext());}
    "*"             {return new Symbol(LexicalUnit.TIMES, yyline, yycolumn, yytext());}
    "/"             {return new Symbol(LexicalUnit.DIVIDE, yyline, yycolumn, yytext());}
    // Loop keywords
    "while"         {return new Symbol(LexicalUnit.WHILE, yyline, yycolumn, yytext());}
    "do"            {return new Symbol(LexicalUnit.DO, yyline, yycolumn, yytext());}
    "endwhile"      {return new Symbol(LexicalUnit.ENDWHILE, yyline, yycolumn, yytext());}
    "for"           {return new Symbol(LexicalUnit.FOR, yyline, yycolumn, yytext());}
    "from"          {return new Symbol(LexicalUnit.FROM, yyline, yycolumn, yytext());}
    "by"            {return new Symbol(LexicalUnit.BY, yyline, yycolumn, yytext());}
    "to"            {return new Symbol(LexicalUnit.TO, yyline, yycolumn, yytext());}
    // Logical operators
    "and"           {return new Symbol(LexicalUnit.AND, yyline, yycolumn, yytext());}
    "or"            {return new Symbol(LexicalUnit.OR, yyline, yycolumn, yytext());}
    "not"           {return new Symbol(LexicalUnit.NOT, yyline, yycolumn, yytext());}
    {VarName}       {return new Symbol(LexicalUnit.VARNAME, yyline, yycolumn, yytext());}
    // Number
    {Number}     {return new Symbol(LexicalUnit.NUMBER, yyline, yycolumn,Integer.valueOf(yytext()));}


    {Ignore}        {} // ignore the space(s)
    // Error, handle the case of nested comment [ CO CO comment CO CO ] 
    [^]             {throw new PatternSyntaxException("Error, unknow symbol",yytext(),yyline);} 
}

<SHORTCOMMENT>{
   
    {ReturnLine}    {yybegin(YYINITIAL);} // We have a \n / \r so we ended the line. We can go back to YYINITIAL to keep analysing
    [^]				{} // ignore comment
}
<LONGCOMMENT>{
    {LongComment}   {yybegin(YYINITIAL);} // We have another CO so the comment has ended
    <<EOF>>         {throw new PatternSyntaxException("Error, a comment is not closed",yytext(),yyline);}
    [^]				{} // ignore comment

}






















