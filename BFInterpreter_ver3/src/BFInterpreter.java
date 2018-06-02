/**
 * 
 * @author      Rishabh Dalal
 * Description: Main class that interprets the bf program
 * 
 * Modified:    Rishabh Dalal, 1 June 2018
 * Description: Added functions to hash the positions of braces
 *              for faster interpretation of bf programs that 
 *              have highly nested loops
 *              
 * Modified:    Rishabh Dalal, 2 June 2018
 * Description: Added functions to trim the code by merging the
 * 				same consequent characters and handling all of
 * 				them at once in their respective functions for 
 *              faster interpretation of bf programs.
 */

import java.util.ArrayList;

class BFInterpreter{

	private static String VALID_LETTERS = "[]<>+-.,";
	private HandleIO stream;
	private ArrayList<Integer> array;
	private String code;
	private int currentIndexArray;
	private int currentIndexCode;
	private Stack stack;
	private ArrayList<Integer> hash;

	public BFInterpreter() throws Exception {
		
		stack = new Stack();
		hash = new ArrayList();
		stream = new HandleIO();
		code = stream.takeStrInput();
		array = new ArrayList();
		array.add(0);
		currentIndexArray = 0;
		currentIndexCode = 0;
		code = preprocess(code);
		code = trim(code);
		System.out.println("trimmed code: " + code);
		
		efficient();
		
		//printArray(hash);
		interpreter();
	
	}

	private void interpreter() throws Exception{
		//Main method to interpret the BF program
		
		int current = 0;

		while (currentIndexCode < code.length()){
			if (Character.isDigit(code.charAt(currentIndexCode))){
				current = Character.getNumericValue(code.charAt(currentIndexCode));
				currentIndexCode++;
			}
			else { 
				if (!(Character.isDigit(code.charAt(currentIndexCode - 1)))) {
					current = 1;
				}
				if (code.charAt(currentIndexCode) == '>'){
					handleGreaterThan(current);
				}
				else if (code.charAt(currentIndexCode) == '<'){
					handleLessThan(current);
				}
				else if (code.charAt(currentIndexCode) == '['){
					handleOpenBracket();
				}
				else if (code.charAt(currentIndexCode) == ']'){
					handleClosingBracket();
				}
				else if (code.charAt(currentIndexCode) == '.'){ //Outputs the char, not int, at the given index in the array
					handleDot(current);
				}
				else if (code.charAt(currentIndexCode) == ','){
					handleComma(current);
				}
				else if (code.charAt(currentIndexCode) == '+'){
					handlePlus(current);
				}
				else {
					handleMinus(current);
				}
			
			}
		}
		stream.printStrOutput("DONE!");
	}	
	

	private void efficient() throws Exception {
		//Procedure to hash the positions of corresponding braces
		//eg. [[]] -> {3, 2, 1, 0} 
		//First brace at index 0 matches to brace at index 3.
		

		char current = 0;
		stack.empty();   //Keeping a stack for nested braces
		
		int openingIndex = 0;
		int j = 0;
		int closingIndex = 0;
		for (int i = 0; i < code.length(); i++) hash.add(0);  //Constructing the array
		
		for (int i = 0; i < code.length(); i++) {
			current = code.charAt(i);
			if (current == '[') {
				stack.push('[');
				openingIndex = i;
				j = i; //To not interfere with loop control variable
				while (true){
					if (stack.isEmpty()) {
						closingIndex = j;
						break;      //Found the corresponding brace
					}
					j++;
					if (j >= code.length()) {
						throw new Exception("Wrong program.");
					}
					current = code.charAt(j);
					if (current == '[') { //Another level of nested brace
						stack.push(']');
					}
					else if (current == ']') { //One nested level finished
						stack.pop();
					}
				}
				hash.set(openingIndex, closingIndex);
				hash.set(closingIndex, openingIndex);
				j = openingIndex + 1;
			}
			
		}
	}
		
	private String trim(String cin) {
		// trimming the code by merging similar consequent expressions.
		// [+++,,...] ->  [3+2,3.] (and handling + efficiently)
		
		String trimmedCin = "";
		int j = 0;
		int total = 1;
		char initialChar = 0;
		
		int i = 0;
		while (i < cin.length()) {
			
			initialChar = cin.charAt(i);
			j = i;
			total = 0;
			if ((initialChar != '[') && (initialChar != ']')){
				while (true) {
					if (j >= cin.length()) break;
					if (cin.charAt(j) != initialChar) {
						break;
					}
					else {
						total++;
						j++;
					}
				}
			}
			if (total == 0) i++;
			else  i += total; 
			if (total > 1) trimmedCin += Integer.toString(total) + Character.toString(initialChar);
			else trimmedCin += Character.toString(initialChar);
		}
		return trimmedCin;
	}
	
	private void printArray(ArrayList<Integer> arr) {
		//printing any ArrayList
		
		for (int i = 0; i < arr.size(); i++) {
			System.out.print(arr.get(i));
			System.out.print(" ");
		}
		System.out.println();
	}
	
	private String preprocess(String cin){
		// Removing unnecessary comments from the program
		
		String newCode = "";
		char letter;
		for (int i=0; i < cin.length(); i++){
			letter =  cin.charAt(i);
			if (VALID_LETTERS.contains(Character.toString(letter))){
				newCode += letter;
			}
		}
		return newCode;
	}

	private void handleGreaterThan(int times){
	
		if (array.size() >= (currentIndexArray + times + 1)){   // + 1 to offset index starting at 0
			currentIndexArray = currentIndexArray + times;
		}
		else{
			for (int i=0; i < times; i++) {
				array.add(0);
				currentIndexArray++;
			}
		}
		currentIndexCode++;
	}
	
	private void handleLessThan(int times) throws Exception{
		if (currentIndexArray - times < 0){
			throw new Exception("Trying to go to -ve indices.");
		}
		else currentIndexArray = currentIndexArray - times;
		currentIndexCode++;
	}
		
	private void handleDot(int times){
		int curr = array.get(currentIndexArray);
		
		for (int i = 0; i < times; i++) {
			stream.printCharOutput((char) curr);
		}
		currentIndexCode++;		
	}
	
	private void handleComma(int times){
		for (int i=0; i < times; i++) {
			int tempInput = stream.takeIntInput();
			array.set(currentIndexArray, tempInput);
		}
		currentIndexCode++;
	}
	
	private void handlePlus(int times){
		array.set(currentIndexArray, array.get(currentIndexArray) + times + 1);
		currentIndexCode++;
	}
	
	private void handleMinus(int times){
		array.set(currentIndexArray, array.get(currentIndexArray) - times - 1);
		currentIndexCode++;
	}
	
	private void handleClosingBracket(){
		//Handling the opening bracket
		
		char current = 0;
		if (array.get(currentIndexArray) == 0) currentIndexCode++;
		else {
			currentIndexCode = hash.get(currentIndexArray) + 1;
		}
	}

	private void handleOpenBracket(){
		//Handling the opening bracket
		
		char current = 0;
		if (array.get(currentIndexArray) == 0){
			currentIndexCode = hash.get(currentIndexArray);
		}	
		else currentIndexCode++;
	}
}