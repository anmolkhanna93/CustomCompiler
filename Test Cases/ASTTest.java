package cop5556sp17;

import static cop5556sp17.Scanner.Kind.PLUS;
import static cop5556sp17.Scanner.Kind.MINUS;
import static cop5556sp17.Scanner.Kind.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.Parser.SyntaxException;
import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;
import cop5556sp17.Scanner.Kind;
import cop5556sp17.AST.ASTNode;
import cop5556sp17.AST.AssignmentStatement;
import cop5556sp17.AST.BinaryChain;
import cop5556sp17.AST.BinaryExpression;
import cop5556sp17.AST.Block;
import cop5556sp17.AST.BooleanLitExpression;
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
import cop5556sp17.AST.Tuple;
import cop5556sp17.AST.WhileStatement;

public class ASTTest {

	static final boolean doPrint = true;
	static void show(Object s){
		if(doPrint){System.out.println(s);}
	}
	

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void testFactor0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(IdentExpression.class, ast.getClass());
	}

	@Test
	public void testFactor1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "123";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(IntLitExpression.class, ast.getClass());
	}

	@Test
	public void testFactor2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "true";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BooleanLitExpression.class, ast.getClass());
	}

	@Test
	public void testFactor3() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "false";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BooleanLitExpression.class, ast.getClass());
	}
	@Test
	public void testFactor4() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "screenwidth";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(ConstantExpression.class, ast.getClass());
	}

	
	@Test
	public void testBinaryExpr0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "1+abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(IntLitExpression.class, be.getE0().getClass());
		assertEquals(IdentExpression.class, be.getE1().getClass());
		assertEquals(PLUS, be.getOp().kind);
	}
	
	@Test
	public void testBinaryExpr1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "1+2";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(IntLitExpression.class, be.getE0().getClass());
		assertEquals(IntLitExpression.class, be.getE1().getClass());
		assertEquals(PLUS, be.getOp().kind);
	}
	@Test
	public void testBinaryExpr2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "1+2+3";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(BinaryExpression.class, be.getE0().getClass());
		
		assertEquals(IntLitExpression.class, be.getE1().getClass());
		
		
		assertEquals(PLUS, be.getOp().kind);
		
	}
	@Test
	public void testBinaryExpr3() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "1+2+3+4";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(BinaryExpression.class, be.getE0().getClass());
		
		assertEquals(IntLitExpression.class, be.getE1().getClass());
		
		
		assertEquals(PLUS, be.getOp().kind);
		
	}
	@Test
	public void testBinaryExpr4() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "1+2+3+4-5";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(BinaryExpression.class, be.getE0().getClass());
		
		assertEquals(IntLitExpression.class, be.getE1().getClass());
		
		
		assertEquals(MINUS, be.getOp().kind);
		
	}
	@Test
	public void testBinaryExpr5() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "1-2+3-4-5";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(BinaryExpression.class, be.getE0().getClass());
		
		assertEquals(IntLitExpression.class, be.getE1().getClass());
		
		
		assertEquals(MINUS, be.getOp().kind);
		
	}
	@Test
	public void testBinaryExpr6() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "1-2+3-4+5";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(BinaryExpression.class, be.getE0().getClass());
		
		assertEquals(IntLitExpression.class, be.getE1().getClass());
		
		
		assertEquals(PLUS, be.getOp().kind);
		
	}
	@Test
	public void testBinaryExpr7() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "1%2";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(IntLitExpression.class, be.getE0().getClass());
		assertEquals(IntLitExpression.class, be.getE1().getClass());
		assertEquals(MOD, be.getOp().kind);
	}
//	@Test
//	public void testBinaryExpr8() throws IllegalCharException, IllegalNumberException, SyntaxException {
//		String input = "gray(true)";
//		Scanner scanner = new Scanner(input);
//		scanner.scan();
//		Parser parser = new Parser(scanner);
//		ASTNode ast = parser.expression();
//		//assertEquals(Tuple.class, ast.getClass());
//		//Tuple tu = (Tuple) ast;
//		
//		//assertEquals(IntLitExpression.class, be.getE0().getClass());
//		//assertEquals(IntLitExpression.class, be.getE1().getClass());
//		//assertEquals(OP_GRAY, tu.getFirstToken().kind);
//	}
	@Test
	public void testBinaryExpr9() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "1-2+3-4%5";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(BinaryExpression.class, be.getE0().getClass());
		
		assertEquals(BinaryExpression.class, be.getE1().getClass());
		
		
		assertEquals(MINUS, be.getOp().kind);
	}
	@Test
	public void testBinaryExpr10() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "1+2%3-4-5";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(BinaryExpression.class, be.getE0().getClass());
		
		assertEquals(IntLitExpression.class, be.getE1().getClass());
		
		
		assertEquals(MINUS, be.getOp().kind);
	}
	@Test
	public void testBinaryExpr11() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "1+2%3-4%5";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(BinaryExpression.class, be.getE0().getClass());
		
		assertEquals(BinaryExpression.class, be.getE1().getClass());
		
		
		assertEquals(MINUS, be.getOp().kind);
	}
	@Test
	public void testBinaryExpr12() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "anmol+kashish";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(IdentExpression.class, be.getE0().getClass());
		assertEquals(IdentExpression.class, be.getE1().getClass());
		assertEquals(PLUS, be.getOp().kind);
	}
	@Test
	public void testBinaryExpr13() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "anmol+kashish+123+hello-anmol";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(BinaryExpression.class, be.getE0().getClass());
		assertEquals(IdentExpression.class, be.getE1().getClass());
		assertEquals(MINUS, be.getOp().kind);
	}
	@Test
    public void testBinaryExpr14() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "1 + 2 - 3";
        Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
        ASTNode ast = parser.expression();
        assertEquals(BinaryExpression.class, ast.getClass());
        BinaryExpression be = (BinaryExpression) ast;
        assertEquals(BinaryExpression.class, be.getE0().getClass());
        assertEquals(IntLitExpression.class, be.getE1().getClass());
        assertEquals(MINUS, be.getOp().kind);
        be = (BinaryExpression) be.getE0();
        assertEquals(IntLitExpression.class, be.getE0().getClass());
        assertEquals(1, be.getE0().getFirstToken().intVal());
        assertEquals(IntLitExpression.class, be.getE1().getClass());
        assertEquals(2, be.getE1().getFirstToken().intVal());
        assertEquals(PLUS, be.getOp().kind);
    }
	@Test
    public void testBinaryExpr15() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "1 * 2 - 3";
        Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
        ASTNode ast = parser.expression();
        assertEquals(BinaryExpression.class, ast.getClass());
        BinaryExpression be = (BinaryExpression) ast;
        assertEquals(BinaryExpression.class, be.getE0().getClass());
        assertEquals(IntLitExpression.class, be.getE1().getClass());
        assertEquals(MINUS, be.getOp().kind);
        be = (BinaryExpression) be.getE0();
        assertEquals(IntLitExpression.class, be.getE0().getClass());
        assertEquals(1, be.getE0().getFirstToken().intVal());
        assertEquals(IntLitExpression.class, be.getE1().getClass());
        assertEquals(2, be.getE1().getFirstToken().intVal());
        assertEquals(TIMES, be.getOp().kind);
    }
	@Test
	public void testBinaryExpr16() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "1+2+3+4";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode ast = parser.expression();
		assertEquals(BinaryExpression.class, ast.getClass());
		BinaryExpression be = (BinaryExpression) ast;
		assertEquals(BinaryExpression.class, be.getE0().getClass());		
		assertEquals(IntLitExpression.class, be.getE1().getClass());
		assertEquals(PLUS, be.getOp().kind);
		be = (BinaryExpression) be.getE0();
        assertEquals(BinaryExpression.class, be.getE0().getClass());
        assertEquals(IntLitExpression.class, be.getE1().getClass());
        assertEquals(PLUS, be.getOp().kind);
		
	}
	
	@Test
    public void testBlock0() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "{frame a integer b}";
        Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
        ASTNode ast = parser.block();
        assertEquals(Block.class, ast.getClass());
        Block b = (Block) ast;
        ArrayList<Dec> d = b.getDecs();
        assertEquals("frame", d.get(0).getType().getText());
        assertEquals("a", d.get(0).getIdent().getText());
        assertEquals("b", d.get(1).getIdent().getText());
        assertEquals("integer", d.get(1).getType().getText());
    }
	 
	@Test
    public void testBlock1() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "{integer a boolean b}";
        Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
        ASTNode ast = parser.block();
        assertEquals(Block.class, ast.getClass());
        Block b = (Block) ast;
        ArrayList<Dec> d = b.getDecs();
        assertEquals("integer", d.get(0).getType().getText());
        assertEquals("a", d.get(0).getIdent().getText());
        assertEquals("b", d.get(1).getIdent().getText());
        assertEquals("boolean", d.get(1).getType().getText());
    }
	@Test
    public void testDec0() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "boolean a";
        Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
        ASTNode ast = parser.dec();
        assertEquals(Dec.class, ast.getClass());
        Dec d = (Dec) ast;
        assertEquals("boolean", d.getFirstToken().getText());
        assertEquals("a", d.getIdent().getText());
        
    }
	@Test
    public void testDec1() throws IllegalCharException, IllegalNumberException, SyntaxException {
        String input = "boolean a";
        Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
        ASTNode ast = parser.dec();
        assertEquals(Dec.class, ast.getClass());
        Dec d = (Dec) ast;
        assertEquals("a", d.getIdent().getText());
        assertEquals(KW_BOOLEAN, d.getType().kind);
    }
	@Test
	public void testChainElem0() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "blur(a,b,c,)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		parser.chainElem();
	}
	@Test
	public void testChainElem1() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "blur(a,b,c,";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		parser.chainElem();
	}
	@Test
	public void testProgram0() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "prog0 {}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.parse();
	}
	@Test
	public void testProgram1() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "prog0 {integer a frame}";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.parse();
	}
	
	@Test
	public void testProgram2() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "prog1 {boolean b integer a";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.parse();
	}
	@Test
	public void testProgram3() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "x <- 33 ;";
		Parser parser = new Parser(new Scanner(input).scan());
		ASTNode ast = parser.statement();
        assertEquals(AssignmentStatement.class, ast.getClass());
	}
	
	
	
}
