package cop5556sp17;

import cop5556sp17.Scanner.Kind;
import static cop5556sp17.Scanner.Kind.*;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import cop5556sp17.AST.*;
import cop5556sp17.Scanner.Token;

public class Parser {

	/**
	 * Exception to be thrown if a syntax error is detected in the input.
	 * You will want to provide a useful error message.
	 *
	 */


	@SuppressWarnings("serial")
	public static class SyntaxException extends Exception {
		public SyntaxException(String message) {
			super(message);
		}
	}

	/**
	 * Useful during development to ensure unimplemented routines are
	 * not accidentally called during development.  Delete it when 
	 * the Parser is finished.
	 *
	 */
	@SuppressWarnings("serial")	
	public static class UnimplementedFeatureException extends RuntimeException {
		public UnimplementedFeatureException() {
			super();
		}
	}

	Scanner scanner;
	Token t;

	Parser(Scanner scanner) {
		this.scanner = scanner;
		t = scanner.nextToken();
	}

	/**
	 * parse the input using tokens from the scanner.
	 * Check for EOF (i.e. no trailing junk) when finished
	 * 
	 * @throws SyntaxException
	 */
	Program parse() throws SyntaxException {
		Program temp = program();
		matchEOF();
		return temp;
	}

	Expression expression() throws SyntaxException {
		//TODO
		Expression obj1,obj2 = null;
		obj1 = term();
		while(t.kind == Kind.LE || t.kind == Kind.GT ||
				t.kind == Kind.GE || t.kind == Kind.EQUAL || t.kind == Kind.NOTEQUAL ||t.kind ==  Kind.LT){

			Token token1 = t;
			consume();

			obj2=term();
			obj1 = new BinaryExpression(obj1.getFirstToken(), obj1, token1, obj2);
		}
		return obj1;
		//throw new UnimplementedFeatureException();
	}

	Expression term() throws SyntaxException {
		//TODO
		Expression obj1,obj2 = null;

		obj1 = elem();
		while(t.kind == Kind.PLUS || t.kind == Kind.MINUS || t.kind == Kind.OR){
			Token token1 = t;
			consume();
			obj2 = elem();
			obj1 = new BinaryExpression(obj1.getFirstToken(), obj1, token1, obj2);
		}
		return obj1;
		//throw new UnimplementedFeatureException();
	}

	Expression elem() throws SyntaxException {
		//TODO
		Expression obj1,obj2 = null;

		obj1 = factor();
		while(t.kind == Kind.TIMES || t.kind == Kind.DIV || t.kind == Kind.AND 
				|| t.kind == Kind.MOD){
			Token token1 = t;
			consume();
			obj2 = factor();
			obj1 = new BinaryExpression(obj1.getFirstToken(), obj1, token1, obj2);
		}
		return obj1;
		//throw new UnimplementedFeatureException();
	}

	Expression factor() throws SyntaxException {
		Kind kind = t.kind;
		Expression obj1 = null;

		switch (kind) {
		case IDENT: {
			obj1 = new IdentExpression(t);
			consume();
		}
		break;
		case INT_LIT: {
			obj1 = new IntLitExpression(t);
			consume();
		}
		break;
		case KW_TRUE:
		case KW_FALSE: {
			obj1 = new BooleanLitExpression(t);
			consume();
		}
		break;
		case KW_SCREENWIDTH:
		case KW_SCREENHEIGHT: {
			obj1 = new ConstantExpression(t);
			consume();
		}
		break;
		case LPAREN: {
			consume();
			obj1 = expression();
			match(RPAREN);
		}
		break;
		default:
			//you will want to provide a more useful error message
			throw new SyntaxException("illegal factor");
		}

		return obj1;
	}

	Block block() throws SyntaxException {

		Token token1 = t;
		match(LBRACE);
		Block btemp1 = null;
		ArrayList<Dec> decList = new ArrayList<Dec>();
		ArrayList<Statement> statementList = new ArrayList<Statement>();
		while ( t.kind  == Kind.KW_BOOLEAN || t.kind == Kind.KW_INTEGER ||
				t.kind  == Kind.KW_IMAGE || t.kind == Kind.KW_FRAME ||
				t.kind  == Kind.OP_SLEEP || t.kind == Kind.KW_WHILE ||
				t.kind  == Kind.KW_IF || t.kind == Kind.IDENT ||
				t.kind  == Kind.OP_BLUR || t.kind == Kind.OP_GRAY ||
				t.kind  == Kind.OP_CONVOLVE || t.kind == Kind.KW_SHOW ||
				t.kind  == Kind.KW_HIDE || t.kind == Kind.KW_MOVE ||
				t.kind  == Kind.KW_XLOC || t.kind == Kind.KW_YLOC ||
				t.kind  == Kind.OP_WIDTH || t.kind == Kind.OP_HEIGHT ||
				t.kind  == Kind.KW_SCALE ){


			if(t.kind  == Kind.KW_BOOLEAN){
				Dec d = dec();
				decList.add(d);
			}else if(t.kind == Kind.KW_INTEGER){
				Dec d = dec();
				decList.add(d);
			}else if(t.kind  == Kind.KW_IMAGE){
				Dec d = dec();
				decList.add(d);
			}else if(t.kind == Kind.KW_FRAME){
				Dec d = dec();
				decList.add(d);
			}else {
				Statement stemp = statement();
				statementList.add(stemp);
			}
		}	
		match(Kind.RBRACE);
		btemp1= new Block(token1, decList, statementList);
		return btemp1;
	}

	Program program() throws SyntaxException {
		Token token1 = t;
		match(IDENT);
		Program ptemp1 = null;
		Block btemp1 = null;
		ArrayList<ParamDec> params = new ArrayList<ParamDec>();
		if (t.kind==(LBRACE)){
			btemp1 = block();
		} else {
			params.add(paramDec());
			while (t.kind==(COMMA)){
				consume();
				params.add(paramDec());
			}
			btemp1 = block();
		}
		matchEOF();
		ptemp1 = new Program(token1, params, btemp1);
		return ptemp1;
	}



	ParamDec paramDec() throws SyntaxException {
		//TODO

		if(t.kind==KW_URL){
			Token token1,token2=null;
			token1 = t;
			consume();
			token2 = t;
			match(IDENT);//check
			return new ParamDec(token1, token2);

		}else if(t.kind==KW_FILE){
			Token token1,token2=null;
			token1 = t;
			consume();
			token2 = t;
			match(IDENT);//check
			return new ParamDec(token1, token2);

		}else if(t.kind==KW_INTEGER){
			Token token1,token2=null;
			token1 = t;
			consume();
			token2 = t;
			match(IDENT);//check
			return new ParamDec(token1, token2);

		}else if(t.kind==KW_BOOLEAN){
			Token token1,token2=null;
			token1 = t;
			consume();
			token2 = t;
			match(IDENT);//check
			return new ParamDec(token1, token2);


		}else{
			throw new SyntaxException("Illegal");
		}

		//throw new UnimplementedFeatureException();
	}

	Dec dec() throws SyntaxException {
		//TODO
		

		if(t.kind==KW_INTEGER){
			Token token1,token2=null;
			token1 = t;
			consume();
			token2 = t;
			match(IDENT);
			return new Dec(token1, token2);

		}else if(t.kind==KW_BOOLEAN){
			Token token1,token2=null;
			token1 = t;
			consume();
			token2 = t;
			match(IDENT);
			return new Dec(token1, token2);

		}else if(t.kind==KW_IMAGE){
			Token token1,token2=null;
			token1 = t;
			consume();
			token2 = t;
			match(IDENT);
			return new Dec(token1, token2);

		}else if(t.kind==KW_FRAME){
			Token token1,token2=null;
			token1 = t;
			consume();
			token2 = t;
			match(IDENT);
			return new Dec(token1, token2);

		}else{
			throw new SyntaxException("illegal symbol");
		}

		//throw new UnimplementedFeatureException();
	}

	Statement statement() throws SyntaxException {
		//TODO

		if(t.kind == Kind.OP_SLEEP){
			Token token1=null;
			token1 = t;
			consume();
			Expression e = expression();
			match(SEMI);
			return new SleepStatement(token1, e);

		}
		else if (t.kind == Kind.KW_WHILE || t.kind == Kind.KW_IF){
			Kind kind = t.kind;
			Token token1=null;
			token1 = t;
			consume();
			match(LPAREN);
			Expression etemp = expression();
			match(RPAREN);
			Block btemp = block();//
			if(kind == Kind.KW_WHILE){
				return new WhileStatement(token1, etemp,btemp);
			}else{
				return new IfStatement(token1, etemp,btemp);
			}
			

		}
		else if (t.kind == Kind.IDENT){
			Token token1=null;
			if (scanner.peek().kind == Kind.ASSIGN){
				IdentLValue val = new IdentLValue(t);
				token1 = t;
				consume();
				//token1 = t;
				match(ASSIGN);
				Expression e = expression();
				match(SEMI);
				return new AssignmentStatement(token1, val, e);

			} else {
				Chain cobj = chain();
				match(SEMI);
				return cobj;

			}
		}
		else if (t.kind == Kind.OP_BLUR || t.kind == Kind.OP_GRAY ||
				t.kind == Kind.OP_CONVOLVE || t.kind == Kind.KW_SHOW ||
				t.kind == Kind.KW_HIDE || t.kind == Kind.KW_MOVE ||
				t.kind == Kind.KW_XLOC || t.kind == Kind.KW_YLOC ||
				t.kind == Kind.OP_WIDTH || t.kind == Kind.OP_HEIGHT ||
				t.kind == Kind.KW_SCALE){

			//	consume(); check for the error
			Chain cobj = chain();
			match(Kind.SEMI);
			return cobj;

		}
		else{

			throw new SyntaxException("Illegal Statement");
		} 



		//throw new UnimplementedFeatureException();
	}

	Chain chain() throws SyntaxException {
		//TODO

		Token token1,token2=null;
		token1=t;
		Chain cObj = null;
		ChainElem ce = null;
		cObj= chainElem();

		if(t.kind == Kind.ARROW || t.kind == Kind.BARARROW){
			token2 = t;
			consume();
			ce = chainElem();
			cObj = new BinaryChain(token1, cObj, token2, ce);

			while(t.kind == Kind.ARROW || t.kind == Kind.BARARROW){
				token2 = t;
				consume();
				ce = chainElem();
				cObj = new BinaryChain(token1, cObj, token2, ce);
			}
		}else{
			throw new SyntaxException("illegal chain");
		}
		return cObj;
		//throw new UnimplementedFeatureException();
	}

	ChainElem chainElem() throws SyntaxException {
		//TODO
		ChainElem ceObj=null;

		if(t.kind==Kind.IDENT){
			ceObj = new IdentChain(t);
			consume();
			arg();			//check
		}else if(t.kind==Kind.OP_BLUR){
			Token token1 = t;
			consume();
			Tuple a = arg();
			ceObj = new FilterOpChain(token1, a);
		}else if(t.kind==Kind.OP_GRAY){
			Token token1 = t;
			consume();
			Tuple a = arg();
			ceObj = new FilterOpChain(token1, a);
		}else if(t.kind==Kind.OP_CONVOLVE){
			Token token1 = t;
			consume();
			Tuple a = arg();
			ceObj = new FilterOpChain(token1, a);
		}else if(t.kind==Kind.KW_SHOW){
			Token token1 = t;
			consume();
			Tuple a = arg();
			ceObj = new FrameOpChain(token1, a);
		}else if(t.kind==Kind.KW_HIDE){
			Token token1 = t;
			consume();
			Tuple a = arg();
			ceObj = new FrameOpChain(token1, a);
		}else if(t.kind==Kind.KW_MOVE){
			Token token1 = t;
			consume();
			Tuple a = arg();
			ceObj = new FrameOpChain(token1, a);
		}else if(t.kind==Kind.KW_XLOC){
			Token token1 = t;
			consume();
			Tuple a = arg();
			ceObj = new FrameOpChain(token1, a);
		}else if(t.kind==Kind.KW_YLOC){
			Token token1 = t;
			consume();
			Tuple a = arg();
			ceObj = new FrameOpChain(token1, a);
		}else if(t.kind==Kind.OP_HEIGHT){
			Token token1 = t;
			Tuple a =null;//check
			consume();
			a = arg();
			ceObj = new ImageOpChain(token1, a);
		}else if(t.kind==Kind.OP_WIDTH){
			Token token1 = t;
			Tuple a =null;//check
			consume();
			a = arg();
			ceObj = new ImageOpChain(token1, a);
		}else if(t.kind==Kind.KW_SCALE){
			Token token1 = t;
			Tuple a =null;//check
			consume();
			a = arg();
			ceObj = new ImageOpChain(token1, a);

		}
		else{
			throw new SyntaxException("illegal chain element");
		}
		return ceObj;
		//throw new UnimplementedFeatureException();
	}

	Tuple arg() throws SyntaxException {
		//TODO
		Token token1 =t;
		Tuple tuObj = null;
		List<Expression> exprList = new ArrayList<Expression>();
		if(t.kind==Kind.LPAREN){
			consume();
			exprList.add(expression());//check
			while(t.kind==Kind.COMMA){
				consume();
				Expression e = expression();
				exprList.add(e);
			}
			match(RPAREN);
		}
		tuObj = new Tuple(token1, exprList);
		return tuObj;
		//throw new UnimplementedFeatureException();
	}

	/**
	 * Checks whether the current token is the EOF token. If not, a
	 * SyntaxException is thrown.
	 * 
	 * @return
	 * @throws SyntaxException
	 */
	private Token matchEOF() throws SyntaxException {
		if (t.kind==EOF) {

			return t;
		}
		throw new SyntaxException("expected EOF");
	}

	/**
	 * Checks if the current token has the given kind. If so, the current token
	 * is consumed and returned. If not, a SyntaxException is thrown.
	 * 
	 * Precondition: kind != EOF
	 * 
	 * @param kind
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind kind) throws SyntaxException {
		if (t.kind==kind) {

			//t.isKind(kind)
			return consume();
		}
		throw new SyntaxException("saw " + t.kind + "expected " + kind);
	}

	/**
	 * Checks if the current token has one of the given kinds. If so, the
	 * current token is consumed and returned. If not, a SyntaxException is
	 * thrown.
	 * 
	 * * Precondition: for all given kinds, kind != EOF
	 * 
	 * @param kinds
	 *            list of kinds, matches any one
	 * @return
	 * @throws SyntaxException
	 */
	private Token match(Kind... kinds) throws SyntaxException {
		// TODO. Optional but handy
		return null; //replace this statement
	}

	/**
	 * Gets the next token and returns the consumed token.
	 * 
	 * Precondition: t.kind != EOF
	 * 
	 * @return
	 * 
	 */
	private Token consume() throws SyntaxException {
		Token tmp = t;
		t = scanner.nextToken();
		return tmp;
	}

}
