/**
 * 
 * @author Rishabh Dalal
 * Description: Main class that interprets the bf program
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
	
	public BFInterpreter() throws Exception {
		stack = new Stack();
		stream = new HandleIO();
		code = stream.takeStrInput();
		array = new ArrayList();
		array.add(0);
		currentIndexArray = 0;
		currentIndexCode = 0;
		code = preprocess(code);
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
	
	private void handleClosingBracket() throws Exception{
		//Handling the opening bracket
		
		stack.empty();   //Keeping a stack for nested braces 
		stack.push(']'); //For the currently encountered opening brace
		char current = 0;
		if (array.get(currentIndexArray) == 0) currentIndexCode++;
		else {
			while (true){
				if (stack.isEmpty()) break;      //Found the corresponding brace
				currentIndexCode--;
				if (currentIndexCode < 0) {
					throw new Exception("Wrong program.");
				}
				current = code.charAt(currentIndexCode);
				if (current == ']') { //Another level of nested brace
					stack.push(']');
				}
				else if (current == '[') { //One nested level finished
					stack.pop();
				}
			}
		}
	}
	
	private void handleOpenBracket() throws Exception{
		//Handling the opening bracket
		
		stack.empty();   //Keeping a stack for nested braces
		stack.push('['); //For the currently encountered closing brace
		char current = 0;
		if (array.get(currentIndexArray) == 0){
			while (true){
				if (stack.isEmpty()) break;
				currentIndexCode++;
				if (currentIndexCode >= code.length()) {
					throw new Exception("Wrong program.");
				}
				current = code.charAt(currentIndexCode);
				if (current == '[') stack.push('[');
				
				else if (current == ']') stack.pop();
			}
		//	currentIndexCode++;
		}	
		else currentIndexCode++;
	}
}
		
	