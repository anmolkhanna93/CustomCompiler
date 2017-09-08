
package cop5556sp17;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cop5556sp17.AST.ASTNode;
import cop5556sp17.AST.Program;

public class CodeGenVisitorTest {

	static final boolean doPrint = true;

	static void show(Object s) {
		if (doPrint) {
			System.out.println(s);
		}
	}

	boolean devel = false;
	boolean grade = true;

	
	@Test
	public void frametest() throws Exception {
	String progname = "frametest";
	String input = progname
			+ " url u, file f {frame fr image i u -> i |-> gray -> fr; i -> f;integer x integer y fr->xloc->x;fr->yloc->y;fr->move(x + 50, y + 50) -> show;}";
	Scanner scanner = new Scanner(input);
	scanner.scan();
	Parser parser = new Parser(scanner);
	ASTNode program = parser.parse();
	TypeCheckVisitor v = new TypeCheckVisitor();
	program.visit(v, null);
	show(program);

	
	CodeGenVisitor cv = new CodeGenVisitor(devel, grade, null);
	byte[] bytecode = (byte[]) program.visit(cv, null);

	
	CodeGenUtils.dumpBytecode(bytecode);

	
	String name = ((Program) program).getName();
	String classFileName = "bin/" + name + ".class";
	OutputStream output = new FileOutputStream(classFileName);
	output.write(bytecode);
	output.close();
	System.out.println("wrote classfile to " + classFileName);

	
	String[] args = new String[2]; 
	args[0] = "http://e2.365dm.com/football/badges/192/210.png";
	args[1] = "/Users/anmolkhanna/Desktop/ads/pic1.jpg";

	Runnable instance = CodeGenUtils.getInstance(name, bytecode, args);
	instance.run();
	}
	@Test
	public void emptyProg() throws Exception {
		
		String progname = "emptyProg";
		String input = progname + "  {}";
		Scanner scanner = new Scanner(input);
		scanner.scan();
		Parser parser = new Parser(scanner);
		ASTNode program = parser.parse();
		TypeCheckVisitor v = new TypeCheckVisitor();
		program.visit(v, null);
		show(program);

		CodeGenVisitor cv = new CodeGenVisitor(devel, grade, null);
		byte[] bytecode = (byte[]) program.visit(cv, null);

		
		CodeGenUtils.dumpBytecode(bytecode);

		
		String name = ((Program) program).getName();
		String classFileName = "bin/" + name + ".class";
		OutputStream output = new FileOutputStream(classFileName);
		output.write(bytecode);
		output.close();
		System.out.println("wrote classfile to " + classFileName);

		
		String[] args = new String[0]; 
		Runnable instance = CodeGenUtils.getInstance(name, bytecode, args);
		instance.run();
	}
	
	
	
}