/**
 * 
 * @author Rishabh Dalal
 * Description: Stack ADT
 */

import java.util.ArrayList;

public class Stack {
	private ArrayList<Character> stk;
	
	public Stack() {
		stk = new ArrayList();
	}
	
	public void push(char a) {
		stk.add(a);
	}
	
	public boolean isEmpty() {
		return (stk.size() == 0);
	}
	
	public void pop() throws Exception {
		if (stk.size() == 0) {
			throw new Exception("Stack is empty.");
		}
		else {
			stk.remove(stk.size() -1);
		}
	}
	
	public void empty() {
		stk.clear();
	}
	
	public int length() {
		return stk.size();
	}
}
