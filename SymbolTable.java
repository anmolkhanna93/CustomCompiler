package cop5556sp17;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import cop5556sp17.AST.Dec;


public class SymbolTable {
	
	static class Attributes{
		int scopeNumber;
		Dec dec;
	}
	
	Stack<Integer> stack;
	int currentScope, nestScope;
	HashMap<String, ArrayList<Attributes>> map;
	ArrayList<Attributes> values;

	/** 
	 * to be called when block entered
	 */
	public void enterScope(){
		currentScope = nestScope++;
		stack.push(currentScope);
	}
	
	
	/**
	 * leaves scope
	 */
	public void leaveScope(){
		
		stack.pop();
		if (stack.isEmpty()){
			currentScope = 0;
		}else {
			currentScope = stack.peek();
		}
	}
	
	public boolean insert(String ident, Dec dec){
		values = new ArrayList<>();
		if (map.containsKey(ident)){
			values = map.get(ident);
			for (Attributes attrs: values){
				
				int scopeNumber = attrs.scopeNumber;
				if (scopeNumber == currentScope){
					return false;
				}
			
			}
		}
		Attributes attributes = new Attributes();
		attributes.dec = dec;
		attributes.scopeNumber= currentScope;
		values.add(0, attributes);
		map.put(ident, values);
		return true;
	}
	
	
	
	public Dec lookup(String ident){
		if (map.containsKey(ident)){
			values = map.get(ident);
			for (Attributes attrs: values){
				
				int scopeNumber = attrs.scopeNumber;
				if (stack.contains(scopeNumber)){
					return attrs.dec;
				}
			}
			
		}
		return null;
	}
		
	public SymbolTable() {
		stack = new Stack<>();
		currentScope = nestScope = 0;
		map = new HashMap<>();
	}


	@Override
	public String toString() {
		String stackString = stack.toString();
		String hashMapString = map.toString();
		return "Hashmap:" + hashMapString + "\nScope Stack:" + stackString;
	}
	
	

}
