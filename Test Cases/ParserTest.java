

package cop5556sp17;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.Parser.SyntaxException;
import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;


public class ParserTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void testFactor0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "abc";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.factor();
	}
	
	@Test
	public void testFactor1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(a*b(b+c)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		parser.factor();
	}

	@Test
	public void testFactor2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(a*b";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
		parser.factor();
	}
	@Test
	public void testFactor3() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "(a*B*c*d*e*h*5)";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.factor();
	}
	
	@Test
	public void testArg0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,5) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		System.out.println(scanner);
		Parser parser = new Parser(scanner);
        parser.arg();
	}

	@Test
	public void testArg1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,5,8,9,0) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		System.out.println(scanner);
		Parser parser = new Parser(scanner);
        parser.arg();
	}
	
	@Test
	public void testArg2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,5 ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
        parser.arg();
	}
	@Test
	public void testArg3() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,5,,8,) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(SyntaxException.class);
        parser.arg();
	}
	
	@Test
	public void testArgerror() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "  (3,) ";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.arg();
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
		parser.program();
	}
	
	@Test
	public void testProgram2() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "prog1 {boolean b integer a";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.program();
	}
	
	@Test
	public void testChain0() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "a->a->a->a->b<-b->c->d<-c<-a";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.chain();
	}
	
	@Test
	public void testChain1() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "gray(a*b)|->a->a->b->c";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.chain();
	}
	
	@Test
	public void testChain2() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "a->a->b->";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.chain();
	}
	
	@Test
	public void testChain3() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "gray(a+b)|->";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.chain();
	}
	
	
	
	@Test
	public void testChainElem() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "width";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.chainElem();
	}
	
	@Test
	public void testChainElem1() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "blur(a,b,c,)";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.chainElem();
	}
	
	@Test
	public void testChainElem2() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "blur(a,b,c,";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.chainElem();
	}
	@Test
	public void testElem0() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "(a*b)/(b*c)*(c*d)/(d/e)";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.elem();
	}
	@Test
	public void testElem1() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "(a*b)/(b*c)*(c*d)/(d/e";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.elem();
	}
	@Test
	public void testElem2() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "(a*b)/(b*c)*(c*d)/(d/e}";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.elem();
	}
	@Test
	public void testElem3() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "(a*b)/(b*c}*{c*d)/(d/e)";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.elem();
	}
	
	@Test
	public void testDec0() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "integer a boolean b integer a";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.dec();
	}
	
	@Test
	public void testDec1() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "image a image b integer a boolean b integer c";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.dec();
	}
	
	@Test
	public void testDec2() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "integer";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.dec();
	}
	
	@Test
	public void testDec3() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "integer boolean image frame";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.dec();
	}
	
	@Test
	public void testParamDec0() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "url abcdefghijklmnopq";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.paramDec();
	}
	@Test
	public void testParamDec1() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "url abcdefghijklmnopq integer abc boolean d file a";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.paramDec();
	}
	
	@Test
	public void testParamDec2() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "url file integer boolean";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.paramDec();
	}
	@Test
	public void testStatement0() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "sleep 1;";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.statement();
	}
	
	@Test
	public void testStatement1() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "if(i==false){boolean a}";
		Parser parser = new Parser(new Scanner(input).scan());
		parser.statement();
	}
	
	@Test
	public void testStatement2() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "while(i<10){";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.statement();
	}
	
	@Test
	public void testStatement3() throws IllegalCharException, IllegalNumberException, SyntaxException{
		String input = "sleep 1+2+3+4";
		Parser parser = new Parser(new Scanner(input).scan());
		thrown.expect(SyntaxException.class);
		parser.statement();
	}
	
	
	@Test
	public void testTerm0() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "2 + 3 - 4";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.term();
	}
	
	@Test
	public void testTerm1() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "2 + 3 -";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.term();
	}
	@Test
	public void testTerm2() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "+ 2 + - 3 | 4";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		thrown.expect(Parser.SyntaxException.class);
		parser.term();
	}
	@Test
	public void testTerm3() throws IllegalCharException, IllegalNumberException, SyntaxException {
		String input = "2 + 3 | 4";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		parser.term();
	}
	 
	
}
