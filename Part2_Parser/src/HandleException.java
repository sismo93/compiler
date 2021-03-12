
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handle possible error 
 */
public class HandleException extends Exception {
    String errorMessage;

    public HandleException(Symbol receivedToken,List<LexicalUnit> expectedToken){
        String expectedTokenString  = expectedToken.get(0).toString();

        if (expectedToken.size()>1){
            expectedTokenString = getAllToken(expectedToken);
        }
        errorMessage = "Error at line : " + receivedToken.getLine() + " column : " + 
                        receivedToken.getColumn() + " Received token  : " + receivedToken.getType()
                        + " but expected " + expectedTokenString;
    }

    /**
     * get the tokens that were expected
     * @param expectedToken list of expected token
     * @return a string message containing the right information 
     */
   private String getAllToken(List<LexicalUnit> expectedToken){
       String message = " one of these : ";
       for (LexicalUnit unit: expectedToken) {
           message += unit.toString() + "/";
        }
       return message;
  }

    /**
     * @return the error message with the right information
     */
    public String toString() {
        return this.errorMessage;
    }


}
