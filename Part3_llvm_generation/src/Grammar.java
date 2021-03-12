


/*
Transformation of ALGOL-0 grammar
*/
class Grammar {
    private static String[] rules = new String[] {
        /*  1 */"<Program> -> begin <ProgName> <Code> end",
        /*  2 */"<Code> -> EPSILON",
        /*  3 */"<Code> -> <InstList>",
        /*  4 */"<InstList> -> <Instruction> <InstListPrim>",
        /*  5 */"<InstListPrim> -> SEMICOLON <instList>",
        /*  6 */"<InstListPrim> -> EPSILON",
        /*  7 */"<Instruction> -> <Assign>",
        /*  8 */"<Instruction> -> <If>",
        /*  9 */"<Instruction> -> <While>",
        /*  10 */"<Instruction> -> <For>",
        /* 11 */"<Instruction> -> <Print>",
        /* 12 */"<Instruction> -> <Read>",
        /* 13 */"<Assign> -> [VarName] := <ExprArith>",
        /* 14 */"<ExprArith> -> <Term> <ExprArith_prim>",
        /* 15 */"<Term> -> <Atom> <prodPrim>",
        /* 16 */"<Atom> -> MINUS <Atom>",
        /* 17 */"<Atom> -> VARNAME",
        /* 18 */"<Atom> -> NUMBER",
        /* 19 */"<Atom> -> LEFT_PARENTHESIS <ExprArith> RIGHT_PARENTHESIS",
        /* 20 */"<ExprArith_prim> -> <AddOp> <Term> <ExprArith_prim>",
        /* 21 */"<ExprArith_prim> -> EPSILON",
        /* 22 */"<prodPrim> -> <MultOp> <Atom> <prodPrim>",
        /* 23 */"<prodPrim> -> EPSILON",
        /* 24 */"<addOp> -> +",
        /* 25 */"<addOp> -> -",
        /* 26 */"<multOp> -> *",
        /* 27 */"<multOp> -> /",
        /* 28 */"<If> -> if <cond> then <code> <ifPrim> ",
        /* 29 */"<ifPrim> -> endif",
        /* 30 */"<ifPrim> -> else <Code> endif",
        /* 31 */"<Cond> -> <andCond> <condPrim>",
        /* 32 */"<condPrim> -> or <andCond> <condPrim>",
        /* 33 */"<condPrim> -> EPSILON",
        /* 34 */"<andCond> -> <simpleCond> <andCondPrim>",
        /* 35 */"<andCondPrim> -> and <SimpleCond> <andCondPrim>",
        /* 36 */"<andCondPrim> -> EPSILON",
        /* 37 */"<simpleCond> -> <exprArith> <comp> <exprArith>",
        /* 38 */"<simpleCond> -> not <simpleCond>",
        /* 39 */"<comp> -> =",
        /* 40 */"<comp> -> >=",
        /* 41 */"<comp> -> >",
        /* 42 */"<comp> -> <=",
        /* 43 */"<comp> -> <",
        /* 44 */"<comp> -> /=",
        /* 45 */"<while> -> while <cond> do <code> endwhile",
        /* 46 */"<For> -> for [VarName] from <exprArith> by <exprArith> to <exprArith> do <code> endwhile",
        /* 47 */"<Print> -> print ( <VarName> )",
        /* 48 */"<Read> -> read ( <VarName> )",

        // Add for bonuses part 3
        /* 49 */"<End>  -> end ",
        /* 50 */"<End>  -> end <Program>",
        /* 51 */"<ProgName> -> EPSILON",
        /* 52 */"<ProgName> -> NameProgram",
        /* 53 */"<ifPrim> -> elif <cond> then code <ifPrim>"
    };

    /** Returns the rule corresponding to the given rule number
     * @param s number of the rule used
     */
    public static String getRule(int s) {
        return "Rule "+ s+" : "+rules[s - 1];
    }
}
