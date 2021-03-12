import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.Iterator;

import java.util.ArrayList;



public class Main{


    public static void main(String[] args) throws FileNotFoundException, IOException{
        

        CommandLine commandLine = new CommandLine(args);//Handle the case of missed information + parse args


        List<Symbol> symboles = new ArrayList<Symbol>();

        FileReader source = new FileReader(args[args.length-1]); // read given file
        LexicalAnalyzer codeAnalyze = new LexicalAnalyzer(source);
        
        TreeMap<String,Symbol> identifierDict = new TreeMap<String,Symbol>();// TreeMap is a dictionary where values are sorted 

        Symbol symbol;
        /* yylex() will allow us to get the next token
           we will loop on it until we reach the end (by having symbol = null) 
        */
        while ((symbol = codeAnalyze.yylex()).getValue() != null){ 
            symboles.add(symbol);      
            if(symbol.getType().equals(LexicalUnit.VARNAME)){ 
                if (!identifierDict.containsKey(symbol.getValue())){ // We only add information that we did NOT have
                    identifierDict.put(symbol.getValue().toString(),symbol); // add (String : symbol)
                }
            }

        }

        symboles.add(new Symbol(LexicalUnit.END_OF_STREAM)); // add the end of the file
        Iterator<Symbol> iterator = symboles.iterator();
       
		try{
			new Parser(iterator,commandLine).startParser();
		} catch(HandleException e){
            System.out.println(e);
		}
        
       
        // Will allow us to go through the dictionary and print 
        // all the information needed
        /***
        String stringInformation = "\nIdentifiers\n";
        for (Map.Entry<String,Symbol> entry: identifierDict.entrySet()){
            stringInformation += entry.getKey() + "\t" + entry.getValue().getLine() + "\n";
        }
        System.out.println(stringInformation); ***/
    }


}






