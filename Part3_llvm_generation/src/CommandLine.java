
import java.io.File;

/**
 * Parse the commande line argument
 * 
 */
public class CommandLine {

    private boolean isLLfile = false;
    private String llFile;
    private String testFile;
    private String errorMessage = "Error, in the input. You have to execute your program like that : \njava -jar  yourJarFile.jar sourceFile.alg [-o outputFile.ll] \nYour test file must be an .alg !";

    public CommandLine(String[] args){
        int pos = 0;
        try {
        	testFile = args[pos];
        	pos++;
            if (args.length > 1 && args[pos].equals("-o")){
            	isLLfile = true;
                llFile = args[pos+1];
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(errorMessage);
            } 
        
    }


    /**
     * @return true if the user asked to write the LLVM syntax in a file
     */
    public boolean isLLfile(){
        return isLLfile;}

    /**
     * @return the ll file entered by the user 
     */
    public String getLLFile(){
        return llFile;}

    /**
     * @return the test file entered by the user
     */
    public String getTestFile(){
        return testFile;}
}





