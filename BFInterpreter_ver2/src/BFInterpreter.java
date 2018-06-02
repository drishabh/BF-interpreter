/**
 * 
 * @author Rishabh Dalal
 * Description: Main class that interprets the bf program
 * 
 * Modified: Rishabh Dalal, 1 June 2018
 * Description: Added functions to hash the positions of braces
 *              for faster interpretation of bf programs that 
 *              have highly nested loops
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
		//
		
		stack = new Stack();
		hash = new ArrayList();
		stream = new HandleIO();
		code = stream.takeStrInput();
		array = new ArrayList();
		array.add(0);
		currentIndexArray = 0;
		currentIndexCode = 0;
		code = preprocess(code);
		efficient();
		
		printArray(hash);
		interpreter();
	}

	private void interpreter() throws Exception{
		//Main method to interpret the BF program

		while (currentIndexCode < code.length()){
			if (code.charAt(currentIndexCode) == '>'){
				handleGreaterThan();
			}
			else if (code.charAt(currentIndexCode) == '<'){
				handleLessThan();
			}
			else if (code.charAt(currentIndexCode) == '['){
				handleOpenBracket();
			}
			else if (code.charAt(currentIndexCode) == ']'){
				handleClosingBracket();
			}
			else if (code.charAt(currentIndexCode) == '.'){ //Outputs the char, not int, at the given index in the array
				stream.printCharOutput((char) handleDot());
			}
			else if (code.charAt(currentIndexCode) == ','){
				handleComma();
			}
			else if (code.charAt(currentIndexCode) == '+'){
				handlePlus();
			}
			else {
				handleMinus();
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

	private void handleGreaterThan(){
	
		if (array.size() >= (currentIndexArray + 1 + 1)){   // + 1 to offset index starting at 0
			currentIndexArray++;
		}
		else{
			array.add(0);
			currentIndexArray++;
		}
		currentIndexCode++;
	}
	
	private void handleLessThan() throws Exception{
		if (currentIndexArray <= 0){
			throw new Exception("Trying to go to -ve indices.");
		}
		else currentIndexArray--;
		currentIndexCode++;
	}
		
	private int handleDot(){
		currentIndexCode++;
		return array.get(currentIndexArray);
		
	}
	
	private void handleComma(){
		int tempInput = stream.takeIntInput();
		array.set(currentIndexArray, tempInput);
		currentIndexCode++;
	}
	
	private void handlePlus(){
		array.set(currentIndexArray, array.get(currentIndexArray) + 1);
		currentIndexCode++;
	}
	
	private void handleMinus(){
		array.set(currentIndexArray, array.get(currentIndexArray) - 1);
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