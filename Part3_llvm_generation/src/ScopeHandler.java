import java.util.ArrayList;
import java.util.List;







/**
 * Class that handle the 
 * variable created and the different LLVM code to add
 */
public class ScopeHandler {
    private List<String> listInstructions = new ArrayList<String>();
    private int  variableCounter = 0;
    private int counterAlloc = 0;


    private ArrayList<String> variableList = new ArrayList<String>();




    public ScopeHandler(List<String> listInstructions ){
        this.listInstructions = listInstructions;
    }



    /**
     * Check if the variable already exist 
     * if not we declare it wit the right LLVM syntax
     * @param variable is a VARNAME 
     */
    public void declareVariable (String variable){ 
    	if (!variableList.contains(variable)){
    		
    		listInstructions.add( rightFormatVarname(variable)+ " = alloca i32");
    		variableList.add(variable);
    		variableCounter++;

    	}
    }

    /**
     * Assign a variable under the right LLVM syntax
     * @param adress where to store it
     * @param value the value to store
     */
    public void assignVariable(String adress,String value){
    	listInstructions.add("store i32 "+value+", i32* "+rightFormatVarname(adress));
    }



    /**
    * increment our counter
    */
    public void increment(){
    	counterAlloc++;
    }


    /**
    * get our couter under a String type allowing us to 
    * directly add it on an instruction
    * @return  counter
    */
    public String getCount(){
    	return Integer.toString(counterAlloc);
    }

    /**
    * @return our counter with the right format for LLVM
    */
    public String  getLastVar(){
        return "%"+Integer.toString(counterAlloc);
    }

    /**
     * Load the variable 
     * @param variable a given variable
     */
    public void loadVariable(String variable){
    	if (isVariableDeclared(variable)){
    		listInstructions.add("%"+counterAlloc+" = load i32, i32* "+ rightFormatVarname(variable));
    		
    	} else{
    		throw new RuntimeException ("Variable " + variable + " has not been declared");
    	}
    }



    /**
     * add the print instruction
     */
    public void addPrintInstruction(){
    	listInstructions.add("call void @println(i32 " +"%"+counterAlloc + ")");
    }


    /**
    * We check if the variable has already been declare, 
    * if its the case, we only need to modify his value
    * otherwise, we need to declare it and add it to the list
    */
    public void readInstruction(String variable){
    	String modVariable = rightFormatVarname(variable);
    	increment();
    	listInstructions.add("%"+counterAlloc+" = call i32 @readInt()");

    	if (!variableList.contains(variable)){
    		listInstructions.add( modVariable+ " = alloca i32");
    		variableList.add(variable); 
    	}
    	listInstructions.add("store i32 "+getLastVar()+", i32* "+modVariable);
    }
    
    /**
     * 
     * @param  variable 
     * @return  gives us the right format for a given variable
     */
    private String rightFormatVarname(String variable){
    	return  "%"+ variable + "_0";
    }


    /**
     * Check if the variable has already been declared
     * @param  variable  a given varname
     * @return  true if its the case , else false
     */
    public boolean isVariableDeclared(String variable){
		if (variableList.contains(variable)){
			return true;
		}
		return false;
    }


    /**
    * function that add a certain value to the counterAlloc
    * Help us in the case where we have 
    * x = 3+6+y
    * we will have load "y" so the counterAlloc need to 
    * be incremented
    * @param  val value 
    */
    public void addCountValue(int val){
    	counterAlloc+=val;
    }



    /**
     * Allow us to load when we have a simple assignation
     * like b:=a , we check if the variable "a" is declared or not 
     * @param variable [description]
     */
    public void simpleAssignationVarname(String variable,String var){
    	increment();
    	if (variableList.contains(variable)){
        	listInstructions.add(getLastVar()+" = load i32, i32* %"+variable+"_0");
        	listInstructions.add("store i32 "+getLastVar()+", i32* %"+var+"_0");
    	}else{
    		throw new RuntimeException (variable + " has not been declared at this time");
    	}

    }

}
