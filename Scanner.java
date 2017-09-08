package cop5556sp17;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;



public class Scanner {
	/**
	 * Kind enum
	 */

	public static enum Kind {
		IDENT(""), INT_LIT(""), KW_INTEGER("integer"), KW_BOOLEAN("boolean"), 
		KW_IMAGE("image"), KW_URL("url"), KW_FILE("file"), KW_FRAME("frame"), 
		KW_WHILE("while"), KW_IF("if"), KW_TRUE("true"), KW_FALSE("false"), 
		SEMI(";"), COMMA(","), LPAREN("("), RPAREN(")"), LBRACE("{"), 
		RBRACE("}"), ARROW("->"), BARARROW("|->"), OR("|"), AND("&"), 
		EQUAL("=="), NOTEQUAL("!="), LT("<"), GT(">"), LE("<="), GE(">="), 
		PLUS("+"), MINUS("-"), TIMES("*"), DIV("/"), MOD("%"), NOT("!"), 
		ASSIGN("<-"), OP_BLUR("blur"), OP_GRAY("gray"), OP_CONVOLVE("convolve"), 
		KW_SCREENHEIGHT("screenheight"), KW_SCREENWIDTH("screenwidth"), 
		OP_WIDTH("width"), OP_HEIGHT("height"), KW_XLOC("xloc"), KW_YLOC("yloc"), 
		KW_HIDE("hide"), KW_SHOW("show"), KW_MOVE("move"), OP_SLEEP("sleep"), 
		KW_SCALE("scale"), EOF("eof");

		Kind(String text) {
			this.text = text;
		}

		final String text;

		String getText() {
			return text;
		}
	}
	HashMap<String, Kind> keyWordMap = new HashMap<>();
	public void setMap() {

		keyWordMap.put("integer", Kind.KW_INTEGER);
		keyWordMap.put("boolean", Kind.KW_BOOLEAN);
		keyWordMap.put("image", Kind.KW_IMAGE);
		keyWordMap.put("url", Kind.KW_URL);
		keyWordMap.put("file", Kind.KW_FILE);
		keyWordMap.put("frame", Kind.KW_FRAME);
		keyWordMap.put("while", Kind.KW_WHILE);
		keyWordMap.put("if", Kind.KW_IF);
		keyWordMap.put("true", Kind.KW_TRUE);
		keyWordMap.put("false", Kind.KW_FALSE);
		keyWordMap.put("blur", Kind.OP_BLUR);
		keyWordMap.put("gray", Kind.OP_GRAY);
		keyWordMap.put("convolve", Kind.OP_CONVOLVE);
		keyWordMap.put("screenheight", Kind.KW_SCREENHEIGHT);
		keyWordMap.put("height", Kind.OP_HEIGHT);
		keyWordMap.put("width", Kind.OP_WIDTH);
		keyWordMap.put("screenwidth", Kind.KW_SCREENWIDTH);
		keyWordMap.put("xloc", Kind.KW_XLOC);
		keyWordMap.put("yloc", Kind.KW_YLOC);
		keyWordMap.put("hide", Kind.KW_HIDE);
		keyWordMap.put("show", Kind.KW_SHOW);
		keyWordMap.put("move", Kind.KW_MOVE);
		keyWordMap.put("sleep", Kind.OP_SLEEP);
		keyWordMap.put("scale", Kind.KW_SCALE);
	}

	/**
	 * Thrown by Scanner when an illegal character is encountered
	 */
	@SuppressWarnings("serial")
	public static class IllegalCharException extends Exception {
		public IllegalCharException(String message) {
			super(message);
		}
	}

	/**
	 * Thrown by Scanner when an int literal is not a value that can be represented by an int.
	 */
	@SuppressWarnings("serial")
	public static class IllegalNumberException extends Exception {
		public IllegalNumberException(String message){
			super(message);
		}
	}


	/**
	 * Holds the line and position in the line of a token.
	 */
	static class LinePos {
		public final int line;
		public final int posInLine;

		public LinePos(int line, int posInLine) {
			super();
			this.line = line;
			this.posInLine = posInLine;
		}

		@Override
		public String toString() {
			return "LinePos [line=" + line + ", posInLine=" + posInLine + "]";
		}
	}




	public class Token {
		public final Kind kind;
		public final int pos;  
		public final int length;  

		//returns the text of this Token
		public String getText() {


			if ((this.kind == Kind.IDENT) || (this.kind == Kind.INT_LIT)) {
				return chars.substring(pos, pos + length);
			} else {
				return this.kind.getText();
			}
			//TODO IMPLEMENT THIS

		}

		//returns a LinePos object representing the line and column of this Token
		LinePos getLinePos(){


			int lNumber = java.util.Collections.binarySearch(lineNumbering, pos);
			if (lNumber < 0){
				lNumber = (lNumber * -1) - 2;
			}
			int posInLine = pos - lineNumbering.get(lNumber);
			return new LinePos(lNumber, posInLine);

			//TODO IMPLEMENT THIS
		}

		Token(Kind kind, int pos, int length) {
			this.kind = kind;
			this.pos = pos;
			this.length = length;
		}

		/** 
		 * Precondition:  kind = Kind.INT_LIT,  the text can be represented with a Java int.
		 * Note that the validity of the input should have been checked when the Token was created.
		 * So the exception should never be thrown.
		 * 
		 * @return  int value of this token, which should represent an INT_LIT
		 * @throws NumberFormatException
		 */
		public int intVal() throws NumberFormatException{

			return Integer.parseInt(this.getText());

			//TODO IMPLEMENT THIS
		}
		//Assignment three
		  @Override
		  public int hashCode() {
		   final int prime = 31;
		   int result = 1;
		   result = prime * result + getOuterType().hashCode();
		   result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		   result = prime * result + length;
		   result = prime * result + pos;
		   return result;
		  }

		  @Override
		  public boolean equals(Object obj) {
		   if (this == obj) {
		    return true;
		   }
		   if (obj == null) {
		    return false;
		   }
		   if (!(obj instanceof Token)) {
		    return false;
		   }
		   Token other = (Token) obj;
		   if (!getOuterType().equals(other.getOuterType())) {
		    return false;
		   }
		   if (kind != other.kind) {
		    return false;
		   }
		   if (length != other.length) {
		    return false;
		   }
		   if (pos != other.pos) {
		    return false;
		   }
		   return true;
		  }

		 

		  private Scanner getOuterType() {
		   return Scanner.this;
		  }

	}




	Scanner(String chars) {
		this.chars = chars;
		tokens = new ArrayList<Token>();
		setMap();

	}

	public int handleWhiteSpace(int pos, int length){
		
		
		Boolean commentClosed=false;
		while(pos < length){

			if ((pos<(length-1))&& chars.charAt(pos) == '/' && chars.charAt(pos+1) == '*') {
				commentClosed = false;
				pos = pos + 2;
				while((commentClosed == false) && (pos < (length - 1))){
					if (chars.charAt(pos) == '*' && chars.charAt(pos+1) == '/') {
						pos++;
						commentClosed = true;
					}else if(chars.charAt(pos)=='\n'){
						lineNumbering.add(pos+1);
					}
					pos++;
				}
				if (commentClosed == false) {
					pos = -1;
					break;
				}else {

				}
			} else if (chars.charAt(pos) == '\n' ){
				if (chars.charAt(pos) == '\\' && chars.charAt(pos+1) == 'n'){
					pos++;
				}
				pos++;
				lineNumbering.add(pos);
			} else if (Character.isWhitespace(chars.charAt(pos))){
				pos++;
			} else{
				break;
			}
		}
		return pos;
	}



	/**
	 * Initializes Scanner object by traversing chars and adding tokens to tokens list.
	 * 
	 * @return this scanner
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 */
	public Scanner scan() throws IllegalCharException, IllegalNumberException {
		int pos = 0; 
		int length = chars.length();
		String state = "START";
		int startPos = 0;
		String sym = "";
		Character ch;
		lineNumbering.add(0);
		if(length == 0){
			length = -1;
		}
		while(pos <= length){
			ch = pos< length ? chars.charAt(pos): '"';
			switch(state){
			case "START":{
				pos = handleWhiteSpace(pos, length);
				if (pos == -1) {
					throw new IllegalCharException("The comment is not closed properly");
				}
				ch = pos < length ? chars.charAt(pos): '"';
				startPos = pos;
				switch(ch){
				case '"': {

					pos++;
				}  break;
				case ';': {
					tokens.add(new Token(Kind.SEMI, startPos, 1));
					pos++;
				} break;
				case ',': {
					tokens.add(new Token(Kind.COMMA, startPos, 1));
					pos++;
				} break;
				case '(': {
					tokens.add(new Token(Kind.LPAREN, startPos, 1));
					pos++;
				} break;
				case ')': {
					tokens.add(new Token(Kind.RPAREN, startPos, 1));
					pos++;
				} break;
				case '{': {
					tokens.add(new Token(Kind.LBRACE, startPos, 1));
					pos++;
				} break;
				case '}': {
					tokens.add(new Token(Kind.RBRACE, startPos, 1));
					pos++;
				} break;
				case '&': {
					tokens.add(new Token(Kind.AND, startPos, 1));
					pos++;
				} break;
				case '+': {
					tokens.add(new Token(Kind.PLUS, startPos, 1));
					pos++;
				} break;
				case '*': {
					tokens.add(new Token(Kind.TIMES, startPos, 1));
					pos++;
				} break;
				case '/': {
					tokens.add(new Token(Kind.DIV, startPos, 1));
					pos++;
				} break;
				case '%': {
					tokens.add(new Token(Kind.MOD, startPos, 1));
					pos++;
				} break;
				case '-': {
					pos++;
					state = "AFTER_MINUS";
				} break;
				case '|': {
					pos++;
					state = "AFTER_OR";
				} break;
				case '=': {
					pos++;

					state = "AFTER_EQUAL";
				} break;
				case '!': {
					pos++;
					state = "AFTER_NOT";
				} break;
				case '<': {
					pos++;
					state = "AFTER_LT";
				} break;
				case '>': {
					pos++;
					state = "AFTER_GT";
				} break;
				default:
					if (Character.isJavaIdentifierStart(ch)) {

						pos++;
						state = "IN_IDENT";
					} else if (Character.isDigit(ch)) {

						if (ch == '0'){
							tokens.add(new Token(Kind.INT_LIT, startPos, 1));
						}else{
							state = "IN_DIGIT";
						}
						pos++;
					} else {

						throw new IllegalCharException("The symbol found is not legal");
					}
				}
			}break;
			case "IN_DIGIT":{
				if((pos<length) && (Character.isDigit(chars.charAt(pos)))){

					pos++;
				}else{
					try{
						sym = chars.substring(startPos, pos);
						if(keyWordMap.containsKey(sym)){
							tokens.add(new Token(keyWordMap.get(sym), startPos, pos - startPos));
						}else{
							Integer.parseInt(sym);
							tokens.add(new Token(Kind.INT_LIT, startPos, pos-startPos));
						}

					} catch (NumberFormatException e) {
						// TODO: handle exception
						throw new IllegalNumberException("Provided number is out of range.");
					}
					state = "START";
				}
			}break;
			case "IN_IDENT":{
				if ((pos<length) && (Character.isJavaIdentifierPart(chars.charAt(pos)))){
					//sym = sym + chars.charAt(pos);
					pos++;
				}else{
					//Todo':- check for keywords
					sym = chars.substring(startPos, pos);
					if(keyWordMap.containsKey(sym)){
						tokens.add(new Token(keyWordMap.get(sym), startPos, pos - startPos));
					}else{
						tokens.add(new Token(Kind.IDENT, startPos, pos-startPos));
					}

					state = "START";
				}
			}break;
			case "AFTER_EQUAL":{
				if((pos<length) && (chars.charAt(pos) == '=')){
					pos++;
					tokens.add(new Token(Kind.EQUAL, startPos, pos-startPos));

					state = "START";
				}else{

					throw new IllegalCharException("Expecting double '==' but found single '='.");
				}
			}break;
			case "AFTER_MINUS":{
				if((pos<length) && (chars.charAt(pos) == '>')){
					pos++;
					tokens.add(new Token(Kind.ARROW, startPos, pos-startPos));
					state = "START";
				}else{

					tokens.add(new Token(Kind.MINUS, startPos, 1));
					state = "START";
				}
			} break;
			case "AFTER_OR":{
				if((pos<length) && (chars.charAt(pos) == '-')){
					pos++;
					state = "AFTER_OR_MINUS";
				}else{
					tokens.add(new Token(Kind.OR, startPos, pos - startPos));

					state = "START";
				}
			} break;
			case "AFTER_OR_MINUS": {
				if ((pos<length) && (chars.charAt(pos) == '>')) {
					pos++;
					tokens.add(new Token(Kind.BARARROW, startPos, pos-startPos));
					state = "START";
				}else {
					tokens.add(new Token(Kind.MINUS, startPos, pos-startPos));
					state = "START";
				}

			} break;
			case "AFTER_NOT": {
				if ((pos<length) && (chars.charAt(pos) == '=')) {
					pos++;
					tokens.add(new Token(Kind.NOTEQUAL, startPos, pos-startPos));
					state = "START";
				}else {
					tokens.add(new Token(Kind.NOT, startPos, 1));

					state = "START";
				}
			} break;
			case "AFTER_LT": {
				if ((pos<length) && (chars.charAt(pos) == '=')) {
					pos++;
					tokens.add(new Token(Kind.LE, startPos, pos-startPos));
					state = "START";
				}else if ((pos<length) && (chars.charAt(pos) == '-')) {
					pos++;
					tokens.add(new Token(Kind.ASSIGN, startPos, pos-startPos));
					state = "START";
				}else{
					tokens.add(new Token(Kind.LT, startPos, 1));
					state = "START";
				}
			} break;
			case "AFTER_GT": {
				if ((pos<length) && (chars.charAt(pos) == '=')) {
					pos++;
					tokens.add(new Token(Kind.GE, startPos, pos-startPos));
					state = "START";
				}else {
					tokens.add(new Token(Kind.GT, startPos, pos-startPos));
					state = "START";
				}
			} break;
			default: assert false;
			}

		}

		if (pos > length) {
			pos = length;
		}
		tokens.add(new Token(Kind.EOF,pos,0));
		return this; 
		//TODO IMPLEMENT THIS!!!!
	}



	final ArrayList<Token> tokens;
	final String chars;
	int tokenNum;
	ArrayList<Integer> lineNumbering = new ArrayList<Integer>();

	/*
	 * Return the next token in the token list and update the state so that
	 * the next call will return the Token..  
	 */
	public Token nextToken() {
		if (tokenNum >= tokens.size())
			return null;
		return tokens.get(tokenNum++);
	}

	/*
	 * Return the next token in the token list without updating the state.
	 * (So the following call to next will return the same token.)
	 */
	public Token peek(){
		if (tokenNum >= tokens.size())
			return null;
		return tokens.get(tokenNum);		
	}



	/**
	 * Returns a LinePos object containing the line and position in line of the 
	 * given token.  
	 * 
	 * Line numbers start counting at 0
	 * 
	 * @param t
	 * @return
	 */
	public LinePos getLinePos(Token t) {
		//TODO IMPLEMENT THIS

		return t.getLinePos();
	}


}
