/**  Important to test the error cases in case the
 * AST is not being completely traversed.
 * 
 * Only need to test syntactically correct programs, or
 * program fragments.
 */

package cop5556sp17;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.AST.ASTNode;
import cop5556sp17.AST.BinaryChain;
import cop5556sp17.AST.ChainElem;
import cop5556sp17.AST.Dec;
import cop5556sp17.AST.IdentExpression;
import cop5556sp17.AST.Program;
import cop5556sp17.AST.Statement;
import cop5556sp17.AST.Type.TypeName;
import cop5556sp17.Parser.SyntaxException;
import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;
import cop5556sp17.TypeCheckVisitor.TypeCheckException;

public class TypeCheckVisitorTest {
	

	@Rule
    public ExpectedException thrown = ExpectedException.none();
/*
	@Test
    public void testchain() throws Exception{
        String input = "p {\nimage xyz\nxyz|->gray;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        Program program = parser.program();
        TypeCheckVisitor v = new TypeCheckVisitor();
        program.visit(v, null);
        BinaryChain bc = (BinaryChain) program.getB().getStatements().get(0);
        assertEquals(TypeName.IMAGE, bc.typeName);
    }
    */
	   @Test
       public void testAssignment() throws Exception{
        String input = "p {\nboolean y \ny <- true;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        program.visit(v, null);
       }
      
        
       @Test
       public void testVisit0() throws Exception {
        String input = "method file y { }";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        //System.out.println(scanner);
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        program.visit(v, null);
       }
        
       @Test
       public void testVisit1() throws Exception {
        String input = "chelsea{if(a>c <= b){}}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        thrown.expect(TypeCheckVisitor.TypeCheckException.class);
        program.visit(v, null);
       }
       
        
       @Test
       public void testVisit2() throws Exception {
        String input = "tos integer u,\n integer x\n{integer y image i u -> i; i -> height; frame f i -> scale (x) -> f;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        thrown.expect(TypeCheckVisitor.TypeCheckException.class);
        program.visit(v, null);
       }
        
       @Test
       public void testVisit5() throws Exception {
        String input = "chelsea{if(false){integer b}\n b <- 5;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
       
        thrown.expect(TypeCheckVisitor.TypeCheckException.class);
        program.visit(v, null);
       }
        
       @Test
       public void testExpression0() throws Exception {
        String input = "chelsea{integer p\n integer q\n integer r\n r <- (p + q);}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        program.visit(v, null);
       }
        
       @Test
       public void testExpression1() throws Exception {
        String input = "chelsea{integer a\n integer b\n integer c\n c <- (a * b);}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        program.visit(v, null);
       
       }
        
       @Test
       public void testExpression2() throws Exception {
        String input = "chelsea{image a\n image b\n image c\n c <- (a - b);}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        program.visit(v, null);
        
       }
        
       @Test
       public void testExpression3() throws Exception {
        String input = "chelsea{image a\n image b\n image c\n c <- (a - b);}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        program.visit(v, null);
       }
        
       @Test
       public void testExpression4() throws Exception {
        String input = "chelsea{integer a\n integer b\n integer c\n c <- (a / b);}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        program.visit(v, null);
       }
        
       @Test
       public void testExpression5() throws Exception {
        String input = "chelsea{integer p\n image q\n image r\n r <- (p * q);}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        program.visit(v, null);
       }
      
 
        
       @Test
       public void testChain1() throws Exception {
       //TODO check
       
        String input = "chelsea{frame p \n p -> yloc;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
              ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        program.visit(v, null);
       }
        
       @Test
       public void testChain2() throws Exception {
                
        
        String input = "chelsea{frame q \n q -> xloc;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        program.visit(v, null);
       }
        
       @Test
       public void testChain3() throws Exception {
                    //TODO check
        String input = "chelsea{frame f integer one integer two \n f -> move (1, 2);}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        program.visit(v, null);
       }
        
       @Test
       public void testChain4() throws Exception {
        String input = "chelsea{image i\n i -> scale(5) ->gray;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        program.visit(v, null);
       
       }
      
        
       @Test
       public void testChain5() throws Exception {
        String input = "chelsea{image i\n i -> blur -> scale(5);}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        program.visit(v, null);
       }
        
       @Test
       public void test1() throws Exception {
        String input = "chelsea url u{integer a\n if(false){integer a\n a <- 59;}\n a <- 20;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        program.visit(v, null);
        
       }
        
       @Test
       public void test2() throws Exception {
        String input = "chelsea{if(true){integer p}p <- 9;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        thrown.expect(TypeCheckVisitor.TypeCheckException.class);
        program.visit(v, null);
       }
        
       @Test
       public void test3() throws Exception {
        String input = "chelsea{integer a\n while(true){integer i2} i <- i2;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        thrown.expect(TypeCheckVisitor.TypeCheckException.class);
        program.visit(v, null);
       }
        
       @Test
       public void Expression6() throws Exception {
        String input = "chelsea{integer i \n i <- screenheight;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        program.visit(v, null);
       }
        
       @Test
       public void testAssignment0() throws Exception {
        String input = "chelsea{integer a \n integer b \n a <- b;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        program.visit(v, null);
       }
        
       @Test
       public void testBadAssignment1() throws Exception {
        String input = "chelsea{integer a \n boolean b \n a <- b;}";
        Scanner scanner = new Scanner(input);
        scanner.scan();
        Parser parser = new Parser(scanner);
        ASTNode program = parser.parse();
        TypeCheckVisitor v = new TypeCheckVisitor();
        thrown.expect(TypeCheckVisitor.TypeCheckException.class);
        program.visit(v, null);
       }
      
}
