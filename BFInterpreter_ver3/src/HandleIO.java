/**
 * 
 * @author Rishabh Dalal
 * Description: Class to handle all the I/O of the input program
 */

import java.util.Scanner;

class HandleIO{

	private Scanner cin;
	private String input;
	private boolean flag;
	
	public HandleIO(){
		cin = new Scanner(System.in);
		input = "";
	}
	
	public void printCharOutput(char cout){ 
		//For handling the "." of bf
		
		System.out.println(cout);
	}
	
	public void printStrOutput(String a) { 
		//To print out any message to the console
		
		System.out.println(a);
	}
	
	public int takeIntInput(){  
		//To handle "," of the bf
		
		flag = false;
		int newInput = 0;
		String userIn = "";
		System.out.print("Enter an integer: ");
		while (!flag) {
			try {
				userIn = cin.nextLine();
				userIn = preprocess(userIn);
				newInput = Integer.parseInt(userIn);
				flag = true;
			}
			catch (NumberFormatException e){
				System.out.print("Enter an integer: ");
			}
		}
		return newInput;
	}

	private String preprocess(String str) {
		//To remove comments from the input program
		
		str = str.replace("\n", "");
		return str;
	}

	public String takeStrInput(){
		// To take the input program from the user
		
		String userIn = "";
		System.out.print("Enter the code: ");
		userIn = cin.nextLine();
		return preprocess(userIn);
	}
}