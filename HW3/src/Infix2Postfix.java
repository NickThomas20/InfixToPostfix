import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

/**
 * @NickThomas
 *  This project is made to take in a file named input.txt and will convert it from infix notation to postfix
 *  notation. It will be then written to a file called output.txt and that file will have the equation as
 *  postfix notation
 */
public class Infix2Postfix {
    public static void main(String args[]) {
        //String will be used for all of the File content
        String nextLine = "";
        try {
            String output = "";
            File file = new File("input.txt");
            //scanner line for the while
            Scanner scan = new Scanner(file);
            //while loop to just get the next line
            while(scan.hasNextLine()) {
                String infix = scan.nextLine();
                //output is shown to have each line individually
                output += infixToPostfix(infix) + "\n";
            }//end while scan nextLineLine

            //write to the output file now
            try{
                FileWriter writer = new FileWriter("output.txt");
                //write to the new file for each line
                writer.write(output);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //close scanner
            scan.close();
            //catch for the file input
        } catch (FileNotFoundException e) {
            //catch file not found exception and show error
            e.printStackTrace();
        }



    }//end main

    /**
     * This method is made to actually convert from infix to postfix notation and give it out as a string to output
     * back to the file
     *
     * @param infix
     */
   public static String infixToPostfix(String infix){
       //error checking usage
       int countSub = 0;
       int countParth = 0;
       String subPostfix = "";

       int rank = 0;
       //intialize everything
        Stack<String> stack = new Stack<>();
        Scanner scan2 = new Scanner(infix);
        String postfix = "";
        while(scan2.hasNext()){
            //checks to see if it has an int
            if(scan2.hasNextInt()){
                //append to string
                int num = scan2.nextInt();
                 postfix += num + " ";
                 //error checking
                 subPostfix += num + " ";
                 countSub++;
                 rank++;

                 //extra operand check error
                 if(rank > 1){
                     postfix = "Error: too many operands (" + num + ")";
                     stack.clear();
                     break;
                 }

            }//end if
            else {
                String operator = scan2.next();
                //checks if open parth, if it is then add to stack
                if(operator.equals("(")) {
                    //error checking
                    countParth++;
                    countSub = 0;
                    rank = 0;
                    subPostfix = "";
                    //put on the stack
                    stack.push(operator);
                }
                //closing and pop all parth
                else if (operator.equals(")")) {
                    //error checking
                    countParth--;
                    if(countParth < 0){
                      postfix = "Error: no opening parenthesis detected";
                      stack.clear();
                      break;
                    }
                    //check everything inside the stack
                 while (!stack.isEmpty() && !stack.peek().equals("(")) {
                        //pop all inside the parth and add to the stack
                        postfix += stack.pop() + " ";
                    }//end while
                    //pop the (
                    stack.pop();
                 //for sub expressions checking
                 if(countSub == 0){
                     postfix = "Error: no subexpression detected ()";
                     stack.clear();
                     break;
                 }
                 //This is to check in between parth and see if they are ( 4 ) (4 * 4)
                 else if(countSub == 1){
                     postfix = "Error: too many operands (" + subPostfix.trim() + ")";
                     stack.clear();
                     break;
                 }
                } else {
                    //error checking
                    //countSub++;
                    rank--;
                    //checks to see if the pres is <= and will pop unless ^^
                    while (!stack.isEmpty() && Prescendent(operator, true) <= Prescendent(stack.peek(), false)) {
                        postfix += stack.pop() + " ";
                    }
                    //otherwise push it to stack
                    stack.push(operator);
                    //checks to see if it has too many operators to throw the error
                    if(rank != 0 || rank == 0 && !(scan2.hasNext())){
                        postfix = "Error: too many operators (" + operator + ")";
                        countParth = 0;
                        stack.clear();
                        break;
                    }
                }
            }//long else

        }//end big while
       //error check close parth
       if(countParth >= 1){
           postfix = "Error: no closing parenthesis detected";
           stack.clear();

       }
       //start popping it all and adding it to the string
       while(!stack.isEmpty()){
           //pops it all
           postfix += stack.pop() + " ";
       }
       //gets rid of spacing
       postfix = postfix.trim();
       //close scanner
       scan2.close();
        return postfix;
   }// end method

    /**
     * This is a method made to set the Precedent of each of the following symbols: + - * / % ^ ( )
     *
     * @param symbol
     */
    public static int Prescendent(String symbol, boolean fromInput) {
        //this conditional is made to return the input presecdent of each of these symbols used for later on postfix notation
        if(fromInput) {
            if (symbol.equals("+") || symbol.equals("-")) {
                return 1;
            } else if (symbol.equals("*") || symbol.equals("/") || symbol.equals("%")) {
                return 2;
            } else if (symbol.equals("^")) {
                return 4;
            }
        }else {
            if (symbol.equals("+") || symbol.equals("-")) {
                return 1;
            } else if (symbol.equals("*") || symbol.equals("/") || symbol.equals("%")) {
                return 2;
            } else if (symbol.equals("^")) {
                return 3;
            }
        }
        //return -1 if none are used so the program won't run
        return -1;
    }//end method
}//end class
