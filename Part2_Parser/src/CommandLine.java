
import java.io.File;

/**
 * Parse the commande line argument
 * 
 */
public class CommandLine {

    private boolean isVerbose = false;
    private boolean isLatex = false;
    private String latexFile;
    private String testFile;
    private String errorMessage = "Error, in the input. You have to execute your program like that : \njava -jar yourJarFile.jar  [-v] [-wt latex.tex ] sourceFile.alg \nYour test file must be an .alg !";

    public CommandLine(String[] args){

        String testFile = args[args.length-1];
        File file = new File(testFile);
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".")+1);
        if(!extension.equals("alg") || args.length > 4){  // case where we test it with something other than a .ALG file
            System.out.println(errorMessage);
            System.exit(0);
        }
        int pos = 0;
        try {
            if (args[pos].equals("-v")){
                isVerbose = true;
                pos++;
                }
            if (args[pos].equals("-wt")){
                isLatex = true;
                latexFile = args[++pos];
                }
            testFile = args[args.length-1];
            }
        catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(errorMessage);
            } 
        
    }

    /**
     * @return true if the user asked more information by putting -v
     */
    public boolean isVerboseOption(){
        return isVerbose;}

    /**
     * @return true if the user asked the derivation tree by putting -wt
     */
    public boolean isLatex(){
        return isLatex;}

    /**
     * @return the latex file entered by the user 
     */
    public String getLatexFile(){
        return latexFile;}

    /**
     * @return the test file entered by the user
     */
    public String getTestFile(){
        return testFile;}
}





