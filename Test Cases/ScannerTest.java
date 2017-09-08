package cop5556sp17;

import static cop5556sp17.Scanner.Kind.*;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.Scanner.IllegalCharException;
import cop5556sp17.Scanner.IllegalNumberException;
import cop5556sp17.Scanner.Kind;

public class ScannerTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();



	@Test
	public void testEmpty() throws IllegalCharException, IllegalNumberException {
		String input = "";
		Scanner scanner = new Scanner(input);
		scanner.scan();
	}

	@Test
	public void testSemiConcat() throws IllegalCharException, IllegalNumberException {
		//input string
		String input = ";;;";
		//create and initialize the scanner
		Scanner scanner = new Scanner(input);
		scanner.scan();
		//get the first token and check its kind, position, and contents
		Scanner.Token token = scanner.nextToken();
		assertEquals(SEMI, token.kind);
		assertEquals(0, token.pos);
		String text = SEMI.getText();
		assertEquals(text.length(), token.length);
		assertEquals(text, token.getText());
		//get the next token and check its kind, position, and contents
		Scanner.Token token1 = scanner.nextToken();
		assertEquals(SEMI, token1.kind);
		assertEquals(1, token1.pos);
		assertEquals(text.length(), token1.length);
		assertEquals(text, token1.getText());
		Scanner.Token token2 = scanner.nextToken();
		assertEquals(SEMI, token2.kind);
		assertEquals(2, token2.pos);
		assertEquals(text.length(), token2.length);
		assertEquals(text, token2.getText());
		//check that the scanner has inserted an EOF token at the end
		Scanner.Token token3 = scanner.nextToken();
		assertEquals(Scanner.Kind.EOF,token3.kind);
	}


	/**
	 * This test illustrates how to check that the Scanner detects errors properly. 
	 * In this test, the input contains an int literal with a value that exceeds the range of an int.
	 * The scanner should detect this and throw and IllegalNumberException.
	 * 
	 * @throws IllegalCharException
	 * @throws IllegalNumberException
	 */
	@Test
	public void testIntOverflowError() throws IllegalCharException, IllegalNumberException{
		String input = "99999999999999999";
		Scanner scanner = new Scanner(input);
		thrown.expect(IllegalNumberException.class);
		scanner.scan();		
	}

	@Test
	public void testIdentification() throws IllegalCharException, IllegalNumberException {
		String input = "integer  boolean  image  file  frame  while  if  true  false  blur abc 011 012 013";
		Scanner scanner = new Scanner(input);
		scanner.scan();

		Scanner.Token token = scanner.nextToken();
		assertEquals(KW_INTEGER, token.kind);

		Scanner.Token token1 = scanner.nextToken();
		assertEquals(KW_BOOLEAN, token1.kind);

		Scanner.Token token2 = scanner.nextToken();
		assertEquals(KW_IMAGE, token2.kind);

		Scanner.Token token3 = scanner.nextToken();
		assertEquals(KW_FILE, token3.kind);

		Scanner.Token token4 = scanner.nextToken();
		assertEquals(KW_FRAME, token4.kind);

		Scanner.Token token5 = scanner.nextToken();
		assertEquals(KW_WHILE, token5.kind);

		Scanner.Token token6 = scanner.nextToken();
		assertEquals(KW_IF, token6.kind);

		Scanner.Token token7 = scanner.nextToken();
		assertEquals(KW_TRUE, token7.kind);

		Scanner.Token token8 = scanner.nextToken();
		assertEquals(KW_FALSE, token8.kind);

		Scanner.Token token9 = scanner.nextToken();
		assertEquals(OP_BLUR, token9.kind);

		Scanner.Token token10 = scanner.nextToken();
		assertEquals(IDENT, token10.kind);

		Scanner.Token token11 = scanner.nextToken();
		assertEquals(INT_LIT, token11.kind);
		assertEquals("0", token11.getText());

		Scanner.Token token12 = scanner.nextToken();
		assertEquals(INT_LIT, token12.kind);
		assertEquals("11", token12.getText());

		Scanner.Token token13 = scanner.nextToken();
		assertEquals(INT_LIT, token13.kind);
		assertEquals("0", token13.getText());

		Scanner.Token token14 = scanner.nextToken();
		assertEquals(INT_LIT, token14.kind);
		assertEquals("12", token14.getText());

		Scanner.Token token15 = scanner.nextToken();
		assertEquals(INT_LIT, token15.kind);
		assertEquals("0", token15.getText());

		Scanner.Token token16 = scanner.nextToken();
		assertEquals(INT_LIT, token16.kind);
		assertEquals("13", token16.getText());
	}


	@Test
	public void testComment() throws IllegalCharException, IllegalNumberException {
		String input="/*Hi my name is XYZ*/";

		Scanner scanner = new Scanner(input);
		scanner.scan();

		Scanner.Token token = scanner.nextToken();
		Assert.assertEquals(token.kind, EOF);
	}

	@Test
	public void testOperators() throws IllegalCharException, IllegalNumberException {
		Scanner scanner = new Scanner("< <= ! != |-> ->");
		scanner.scan();



		Scanner.Token token1 = scanner.nextToken();
		assertEquals(LT, token1.kind);

		Scanner.Token token2 = scanner.nextToken();
		assertEquals(LE, token2.kind);

		Scanner.Token token3 = scanner.nextToken();
		assertEquals(NOT, token3.kind);

		Scanner.Token token4 = scanner.nextToken();
		assertEquals(NOTEQUAL, token4.kind);

		Scanner.Token token5 = scanner.nextToken();
		assertEquals(BARARROW, token5.kind);

		Scanner.Token token6 = scanner.nextToken();
		assertEquals(ARROW, token6.kind);

	}


	@Test
	public void testNumericLiterals() throws IllegalCharException, IllegalNumberException {
		String input = "00123456789 99000";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Scanner.Token token1 = scanner.nextToken();
		assertEquals(Kind.INT_LIT, token1.kind);
		assertEquals(0, token1.intVal());
		Scanner.Token token2 = scanner.nextToken();
		assertEquals(Kind.INT_LIT, token2.kind);
		assertEquals(0, token2.intVal());
		Scanner.Token token3 = scanner.nextToken();
		assertEquals(Kind.INT_LIT, token3.kind);
		assertEquals(123456789, token3.intVal());
		Scanner.Token token4 = scanner.nextToken();
		assertEquals(Kind.INT_LIT, token4.kind);
		assertEquals(99000, token4.intVal());
	}

	@Test
	public void testKeyWords() throws IllegalCharException, IllegalNumberException {
		String input = "while if sleep screenheight screenwidth"
				+ "\ngray convolve blur scale"
				+ "\nwidth height"
				+ "\nxloc yloc hide show move"
				+ "\ntrue false";
		Scanner scanner = new Scanner(input);
		scanner.scan();

		Scanner.Token token1 = scanner.nextToken();
		assertEquals(Kind.KW_WHILE, token1.kind);
		Scanner.Token token2 = scanner.nextToken();
		assertEquals(Kind.KW_IF, token2.kind);
		Scanner.Token token3 = scanner.nextToken();
		assertEquals(Kind.OP_SLEEP, token3.kind);
		Scanner.Token token4 = scanner.nextToken();
		assertEquals(Kind.KW_SCREENHEIGHT, token4.kind);
		Scanner.Token token5 = scanner.nextToken();
		assertEquals(Kind.KW_SCREENWIDTH, token5.kind);
		Scanner.Token token6 = scanner.nextToken();
		assertEquals(Kind.OP_GRAY, token6.kind);
		Scanner.Token token7 = scanner.nextToken();
		assertEquals(Kind.OP_CONVOLVE, token7.kind);
		Scanner.Token token8 = scanner.nextToken();
		assertEquals(Kind.OP_BLUR, token8.kind);
		Scanner.Token token9 = scanner.nextToken();
		assertEquals(Kind.KW_SCALE, token9.kind);
		Scanner.Token token10 = scanner.nextToken();
		assertEquals(Kind.OP_WIDTH, token10.kind);
		Scanner.Token token11 = scanner.nextToken();
		assertEquals(Kind.OP_HEIGHT, token11.kind);
		Scanner.Token token12 = scanner.nextToken();
		assertEquals(Kind.KW_XLOC, token12.kind);
		Scanner.Token token13 = scanner.nextToken();
		assertEquals(Kind.KW_YLOC, token13.kind);
		Scanner.Token token14 = scanner.nextToken();
		assertEquals(Kind.KW_HIDE, token14.kind);
		Scanner.Token token15 = scanner.nextToken();
		assertEquals(Kind.KW_SHOW, token15.kind);
		Scanner.Token token16 = scanner.nextToken();
		assertEquals(Kind.KW_MOVE, token16.kind);
		Scanner.Token token17 = scanner.nextToken();
		assertEquals(Kind.KW_TRUE, token17.kind);
		Scanner.Token token18 = scanner.nextToken();
		assertEquals(Kind.KW_FALSE, token18.kind);

	}

	@Test
	public void testSeparators() throws IllegalCharException, IllegalNumberException {
		String input = "!===";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Scanner.Token token1 = scanner.nextToken();
		assertEquals(Kind.NOTEQUAL, token1.kind);
		assertEquals("!=", token1.getText());
		Scanner.Token token2 = scanner.nextToken();
		assertEquals(Kind.EQUAL, token2.kind);
		assertEquals("==", token2.getText());
	}
	
	@Test
	public void testComment2() throws IllegalCharException, IllegalNumberException {
		String input="/*";

		Scanner scanner = new Scanner(input);
		

		//Scanner.Token token = scanner.nextToken();
		thrown.expect(IllegalCharException.class);
		//assertEquals(token.kind, EOF);
		scanner.scan();
	}



}
