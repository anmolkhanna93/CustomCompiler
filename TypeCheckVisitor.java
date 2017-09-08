package cop5556sp17;

import cop5556sp17.AST.ASTNode;
import cop5556sp17.AST.ASTVisitor;
import cop5556sp17.AST.Tuple;
import cop5556sp17.AST.Type;
import cop5556sp17.AST.AssignmentStatement;
import cop5556sp17.AST.BinaryChain;
import cop5556sp17.AST.BinaryExpression;
import cop5556sp17.AST.Block;
import cop5556sp17.AST.BooleanLitExpression;
import cop5556sp17.AST.Chain;
import cop5556sp17.AST.ChainElem;
import cop5556sp17.AST.ConstantExpression;
import cop5556sp17.AST.Dec;
import cop5556sp17.AST.Expression;
import cop5556sp17.AST.FilterOpChain;
import cop5556sp17.AST.FrameOpChain;
import cop5556sp17.AST.IdentChain;
import cop5556sp17.AST.IdentExpression;
import cop5556sp17.AST.IdentLValue;
import cop5556sp17.AST.IfStatement;
import cop5556sp17.AST.ImageOpChain;
import cop5556sp17.AST.IntLitExpression;
import cop5556sp17.AST.ParamDec;
import cop5556sp17.AST.Program;
import cop5556sp17.AST.SleepStatement;
import cop5556sp17.AST.Statement;
import cop5556sp17.AST.Type.TypeName;
import cop5556sp17.AST.WhileStatement;

import java.text.BreakIterator;
import java.util.ArrayList;



import cop5556sp17.Scanner.Kind;
import cop5556sp17.Scanner.LinePos;
import cop5556sp17.Scanner.Token;
import static cop5556sp17.AST.Type.TypeName.*;
import static cop5556sp17.Scanner.Kind.ARROW;
import static cop5556sp17.Scanner.Kind.KW_HIDE;
import static cop5556sp17.Scanner.Kind.KW_MOVE;
import static cop5556sp17.Scanner.Kind.KW_SHOW;
import static cop5556sp17.Scanner.Kind.KW_XLOC;
import static cop5556sp17.Scanner.Kind.KW_YLOC;
import static cop5556sp17.Scanner.Kind.OP_BLUR;
import static cop5556sp17.Scanner.Kind.OP_CONVOLVE;
import static cop5556sp17.Scanner.Kind.OP_GRAY;
import static cop5556sp17.Scanner.Kind.OP_HEIGHT;
import static cop5556sp17.Scanner.Kind.OP_WIDTH;
import static cop5556sp17.Scanner.Kind.*;

public class TypeCheckVisitor implements ASTVisitor {

	@SuppressWarnings("serial")
	public static class TypeCheckException extends Exception {
		TypeCheckException(String message) {
			super(message);
		}
	}

	SymbolTable symtab = new SymbolTable();


	
	@Override
	public Object visitBinaryChain(BinaryChain binaryChain, Object arg) throws Exception {
		binaryChain.getE0().visit(this, arg);
		TypeName ct = binaryChain.getE0().typeName;

		binaryChain.getE1().visit(this, arg);
		ChainElem chainElem = binaryChain.getE1();
		TypeName cet = binaryChain.getE1().typeName;

		Token op = binaryChain.getArrow();


		if(ct == URL){ 
			if(op.kind == ARROW && cet == IMAGE)
				binaryChain.typeName = IMAGE;
		}
		else if(ct == FILE){ 
			if(op.kind == ARROW && cet == IMAGE)
				binaryChain.typeName = IMAGE;
		}
		else if(ct == FRAME){ 
			if(op.kind == ARROW && chainElem instanceof FrameOpChain){
				Kind kind = chainElem.firstToken.kind;
				if(kind == KW_XLOC){
					binaryChain.typeName = INTEGER;
				}else if(kind == KW_YLOC){
					binaryChain.typeName = INTEGER;
				}else if (kind == KW_SHOW){
					binaryChain.typeName = FRAME;
				}else if(kind == KW_HIDE){
					binaryChain.typeName = FRAME;
				}else if(kind == KW_MOVE){
					binaryChain.typeName = FRAME;
				}
			}
		}
		else if(ct == IMAGE){ 
			if((op.kind == BARARROW || op.kind == ARROW) && chainElem instanceof FilterOpChain){
				Kind kind = chainElem.firstToken.kind;

				if(kind == OP_GRAY){
					binaryChain.typeName = IMAGE;
				}else if(kind == OP_BLUR){
					binaryChain.typeName = IMAGE;
				}else if(kind == OP_CONVOLVE){
					binaryChain.typeName = IMAGE;
				}
			}
			else if(op.kind == ARROW ){
				Kind kind = chainElem.firstToken.kind;

				if (cet == FRAME){
					binaryChain.typeName = FRAME;
				}
				else if(cet == FILE){
					binaryChain.typeName = NONE;
				}

				else if(chainElem instanceof ImageOpChain){

					if ( kind == OP_WIDTH){
						binaryChain.typeName = INTEGER;
					}else if(kind == OP_HEIGHT){
						binaryChain.typeName = INTEGER;
					}else if(kind == KW_SCALE){
						binaryChain.typeName = IMAGE;
					}

				}else if (chainElem instanceof IdentChain && chainElem.typeName==IMAGE){
					binaryChain.typeName = IMAGE;
				}
			}
		}
			else if (ct == INTEGER) {
				if (op.kind == ARROW) {
					if ((chainElem instanceof IdentChain)&&
							(chainElem.typeName == INTEGER)) {
						binaryChain.typeName = INTEGER;
					}
				}
			}
		//System.out.println(ct + " " + binaryChain.typeName + " " + binaryChain.typeName);
		if (binaryChain.typeName != null) {
			return binaryChain;
		}

		throw new TypeCheckException("error occured");
	}



	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression e0 = binaryExpression.getE0();
		e0.visit(this, arg);
		TypeName e0T = e0.typeName;
		Expression e1 = binaryExpression.getE1();
		e1.visit(this, arg);
		TypeName e1T = e1.typeName;
		Token op = binaryExpression.getOp();
		if(op.kind==PLUS){
			if(e0T==INTEGER && e1T==INTEGER){
				binaryExpression.typeName = INTEGER;
			}else if(e0T==IMAGE && e1T==IMAGE){
				binaryExpression.typeName = IMAGE;
			}
		}else if(op.kind==MINUS){
			if(e0T==INTEGER && e1T==INTEGER){
				binaryExpression.typeName = INTEGER;
			}else if(e0T==IMAGE && e1T==IMAGE){
				binaryExpression.typeName = IMAGE;
			}
		}else if(op.kind == TIMES||op.kind==DIV||op.kind==MOD){
			if(e0T==INTEGER && e1T==INTEGER){
				binaryExpression.typeName = INTEGER;
			}else if(e0T==IMAGE && e1T==INTEGER){
				binaryExpression.typeName = IMAGE;
			}else if(e0T==INTEGER && e1T==IMAGE){
				binaryExpression.typeName = IMAGE;
			}
		}else if(op.kind == LT|| op.kind==GT||op.kind==LE||op.kind==GE){
			if(e0T==INTEGER && e1T==INTEGER){
				binaryExpression.typeName = BOOLEAN;
			}else if(e0T==BOOLEAN && e1T==BOOLEAN){
				binaryExpression.typeName = BOOLEAN;
			}
		}else if(op.kind==EQUAL ||op.kind==NOTEQUAL||op.kind==AND||op.kind==OR){
			if(e0T==e1T){
				binaryExpression.typeName = BOOLEAN;
			}
		}
		if(binaryExpression.typeName !=null){
			return binaryExpression;
		}
		throw new TypeCheckException("error occured");

	}
	

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		// TODO Auto-generated method stub
		symtab.enterScope();
		for(Dec dec: block.getDecs()){
			dec.visit(this, arg);
		}

		for(Statement statements:block.getStatements()){
			statements.visit(this, arg);
		}
		symtab.leaveScope();
		return null;
	}

	@Override
	public Object visitBooleanLitExpression(BooleanLitExpression booleanLitExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		booleanLitExpression.typeName =BOOLEAN;
		return booleanLitExpression;

	}

	@Override
	public Object visitFilterOpChain(FilterOpChain filterOpChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Tuple tuple = filterOpChain.getArg();
		tuple.visit(this, arg);
		if(tuple.getExprList().size()==0){
			filterOpChain.typeName = IMAGE;
			return filterOpChain;
		}
		throw new TypeCheckException("error occured");
	}

	@Override
	public Object visitFrameOpChain(FrameOpChain frameOpChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		frameOpChain.kind = frameOpChain.firstToken.kind;
		Kind opKind = frameOpChain.firstToken.kind;
		Tuple tuple = frameOpChain.getArg();
		tuple.visit(this, arg);
		
		if(opKind==KW_SHOW||opKind==KW_HIDE){
			if(tuple.getExprList().size()==0){
				frameOpChain.typeName = NONE;
				return frameOpChain;
			}
		}else if(opKind==KW_XLOC||opKind==KW_YLOC){
			if(tuple.getExprList().size()==0){
				frameOpChain.typeName = INTEGER;
				
				return frameOpChain;
			}
		}else if(opKind==Kind.KW_MOVE){
			if(tuple.getExprList().size()==2){
				frameOpChain.typeName = NONE;
				return frameOpChain;
			}
		}else{
			throw new TypeCheckException("kind not matching");
		}

		throw new TypeCheckException("error");//make better statements;


	}

	@Override
	public Object visitIdentChain(IdentChain identChain, Object arg) throws Exception {
		
		if(symtab.lookup(identChain.getFirstToken().getText())!=null){
			identChain.typeName = symtab.lookup(identChain.getFirstToken().getText()).typeName;
			identChain.setDec(symtab.lookup(identChain.getFirstToken().getText()));
			return identChain;
			
		}
		throw new TypeCheckException("error occures in ident chain");
		
	}

	@Override//check this function
	public Object visitIdentExpression(IdentExpression identExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		if(symtab.lookup(identExpression.firstToken.getText())!=null){
			identExpression.typeName = symtab.lookup(identExpression.firstToken.getText()).typeName;
			identExpression.dec = symtab.lookup(identExpression.firstToken.getText());
			return identExpression;
		}
		throw new TypeCheckException("error");
	}

	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression expression = ifStatement.getE();
		expression.visit(this, arg);
		Block block = ifStatement.getB();
		
		if(expression.typeName==BOOLEAN){
			block.visit(this, arg);
			return ifStatement;
		}
		throw new TypeCheckException("error: not boolean");
	}

	@Override
	public Object visitIntLitExpression(IntLitExpression intLitExpression, Object arg) throws Exception {
		// TODO Auto-generated method stub
		intLitExpression.typeName = TypeName.INTEGER;//CHECK
		return intLitExpression;
	}

	@Override
	public Object visitSleepStatement(SleepStatement sleepStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression expression = sleepStatement.getE();
		expression.visit(this, arg);
		if(expression.typeName == TypeName.INTEGER){//
			return sleepStatement;
		}

		throw new TypeCheckException("eroro");//check

	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		Expression expression = whileStatement.getE();
		expression.visit(this, arg);
		Block block = whileStatement.getB();
		if(whileStatement.getE().typeName == BOOLEAN){
			block.visit(this, arg);
			return whileStatement;
		}
		throw new TypeCheckException("type is not a boolean");

	}

	
	@Override
	public Object visitDec(Dec declaration, Object arg) throws Exception {
		// TODO Auto-generated method stub
		declaration.typeName = Type.getTypeName(declaration.getType());//
		boolean flag = symtab.insert(declaration.getIdent().getText(), declaration);
		if(!flag){
			throw new TypeCheckException("error");
		}
		return declaration;
	}

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		// TODO Auto-generated method stub
		symtab.enterScope();
		/*boolean flag = symtab.insert(program.getName(),null);
		if(!flag){
			throw new TypeCheckException("error");
		}*/
		for(ParamDec paramdec: program.getParams()){
			paramdec.visit(this, arg);
		}
		program.getB().visit(this, arg);
		return program;
	}

	@Override
	public Object visitAssignmentStatement(AssignmentStatement assignStatement, Object arg) throws Exception {
		// TODO Auto-generated method stub
		IdentLValue value = assignStatement.getVar();
		value.visit(this, arg);
		Expression expression = assignStatement.getE();
		expression.visit(this, arg);
		if(Type.getTypeName(value.dec.getType())==expression.typeName){
			return assignStatement;
		}else{
			throw new TypeCheckException("error");
		}

	}

	@Override
	public Object visitIdentLValue(IdentLValue identX, Object arg) throws Exception {
		// TODO Auto-generated method stub
		if(symtab.lookup(identX.firstToken.getText())!=null){
			identX.dec = symtab.lookup(identX.firstToken.getText());
			return identX;
		}
		throw new TypeCheckException("error");
	}

	@Override
	public Object visitParamDec(ParamDec paramDec, Object arg) throws Exception {
		// TODO Auto-generated method stub
		paramDec.typeName = Type.getTypeName(paramDec.getType());
		boolean flag = symtab.insert(paramDec.getIdent().getText(), paramDec);
		if(!flag){
			throw new TypeCheckException("error");
		}
		return paramDec;

	}

	@Override
	public Object visitConstantExpression(ConstantExpression constantExpression, Object arg) {
		// TODO Auto-generated method stub
		constantExpression.typeName = INTEGER;
		return constantExpression;

	}

	@Override
	public Object visitImageOpChain(ImageOpChain imageOpChain, Object arg) throws Exception {
		// TODO Auto-generated method stub
		imageOpChain.kind = imageOpChain.firstToken.kind;
		Kind op = imageOpChain.firstToken.kind;
		Tuple tuple = imageOpChain.getArg();
		tuple.visit(this, arg);//
		if(op==Kind.OP_WIDTH||op==OP_HEIGHT){
			if(tuple.getExprList().size()==0){
				imageOpChain.typeName = INTEGER;
				return imageOpChain;
			}
		}else if(op == Kind.KW_SCALE){
			if(tuple.getExprList().size()==1){
				imageOpChain.typeName = IMAGE;
				return imageOpChain;
			}
		}
		throw new TypeCheckException("grammar error");

		
	}

	@Override
	public Object visitTuple(Tuple tuple, Object arg) throws Exception {
		// TODO Auto-generated method stub
		for(Expression expression: tuple.getExprList()){
			expression.visit(this, arg);
			if(expression.typeName!= INTEGER){
				throw new TypeCheckException("errir");
			}
		}
		return tuple;
	}


}
