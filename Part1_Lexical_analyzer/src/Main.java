import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

import java.util.Map;
import java.util.TreeMap;



public class Main{


    public static void main(String[] args) throws FileNotFoundException, IOException{
        //Handle the case of missed information
        String testFile = args[args.length-1];
        File file = new File(testFile);
        String fileName = file.getName();
        String extension = fileName.substring(fileName.lastIndexOf(".")+1);
        if(!extension.equals("alg")){  // case where we test it with something other than a .ALG file
            System.out.println("Error, in the input. You have to execute your program like that : \njava -jar yourJarFile.jar sourceFile.alg \nYour test file must be an .alg !");
            System.exit(0);
        }

        FileReader source = new FileReader(testFile); // read given file
        LexicalAnalyzer codeAnalyze = new LexicalAnalyzer(source);
        
        TreeMap<String,Symbol> identifierDict = new TreeMap<String,Symbol>();// TreeMap is a dictionary where values are sorted 

        Symbol symbol;
        /* yylex() will allow us to get the next token
           we will loop on it until we reach the end (by having symbol = null) 
        */
        while ((symbol = codeAnalyze.yylex()).getValue() != null){
            System.out.println(symbol.toString());          
            if(symbol.getType().equals(LexicalUnit.VARNAME)){ 
                if (!identifierDict.containsKey(symbol.getValue())){ // We only add information that we did NOT have
                    identifierDict.put(symbol.getValue().toString(),symbol); // add (String : symbol)
                }
            }

        }
        // Will allow us to go through the dictionary and print 
        // all the information needed
        String stringInformation = "\nIdentifiers\n";
        for (Map.Entry<String,Symbol> entry: identifierDict.entrySet()){
            stringInformation += entry.getKey() + "\t" + entry.getValue().getLine() + "\n";
        }
        System.out.println(stringInformation);
    }


}






