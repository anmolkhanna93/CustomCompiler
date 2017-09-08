package cop5556sp17;

import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.util.TraceClassVisitor;

import cop5556sp17.Scanner.Kind;
import cop5556sp17.Scanner.Token;
import cop5556sp17.AST.ASTVisitor;
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
import cop5556sp17.AST.Tuple;
import cop5556sp17.AST.Type.TypeName;
import cop5556sp17.AST.WhileStatement;

import static cop5556sp17.AST.Type.TypeName.FRAME;
import static cop5556sp17.AST.Type.TypeName.IMAGE;
import static cop5556sp17.AST.Type.TypeName.URL;
import static cop5556sp17.Scanner.Kind.*;

public class CodeGenVisitor implements ASTVisitor, Opcodes {

	/**
	 * @param DEVEL
	 *            used as parameter to genPrint and genPrintTOS
	 * @param GRADE
	 *            used as parameter to genPrint and genPrintTOS
	 * @param sourceFileName
	 *            name of source file, may be null.
	 */
	public CodeGenVisitor(boolean DEVEL, boolean GRADE, String sourceFileName) {
		super();
		this.DEVEL = DEVEL;
		this.GRADE = GRADE;
		this.sourceFileName = sourceFileName;
	}

	ClassWriter cw;
	String className;
	String classDesc;
	String sourceFileName;

	MethodVisitor mv; // visitor of method currently under construction
	
    
    int paramDecArguments = 0;
    int slotNumber = 1;
    ArrayList<Attributes> localVariables = new ArrayList<>();
    
    static class info {
		Kind arrowKind;
		String position;
	}
    static class Attributes{
        Label startLabel;
        Label endLabel;
        int slotNumber;
        Dec dec;
    }


	/** Indicates whether genPrint and genPrintTOS should generate code. */
	final boolean DEVEL;
	final boolean GRADE;

	@Override
	public Object visitProgram(Program program, Object arg) throws Exception {
		 cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
	        className = program.getName();
	        classDesc = "L" + className + ";";
	        String sourceFileName = (String) arg;
	        cw.visit(52, ACC_PUBLIC + ACC_SUPER, className, null, "java/lang/Object",
	                new String[] { "java/lang/Runnable" });
	        cw.visitSource(sourceFileName, null);

	        // generate constructor code
	        // get a MethodVisitor
	        mv = cw.visitMethod(ACC_PUBLIC, "<init>", "([Ljava/lang/String;)V", null,
	                null);
	        mv.visitCode();
	        // Create label at start of code
	        Label constructorStart = new Label();
	        mv.visitLabel(constructorStart);
	        // this is for convenience during development--you can see that the code
	        // is doing something.
	        CodeGenUtils.genPrint(DEVEL, mv, "\nentering <init>");
	        // generate code to call superclass constructor
	        mv.visitVarInsn(ALOAD, 0);
	        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
	        // visit parameter decs to add each as field to the class
	        // pass in mv so decs can add their initialization code to the
	        // constructor.
	        ArrayList<ParamDec> params = program.getParams();
	        for (ParamDec dec : params)
	            dec.visit(this, mv);
	        mv.visitInsn(RETURN);
	        // create label at end of code
	        Label constructorEnd = new Label();
	        mv.visitLabel(constructorEnd);
	        // finish up by visiting local vars of constructor
	        // the fourth and fifth arguments are the region of code where the local
	        // variable is defined as represented by the labels we inserted.
	        mv.visitLocalVariable("this", classDesc, null, constructorStart, constructorEnd, 0);
	        mv.visitLocalVariable("args", "[Ljava/lang/String;", null, constructorStart, constructorEnd, 1);
	        // indicates the max stack size for the method.
	        // because we used the COMPUTE_FRAMES parameter in the classwriter
	        // constructor, asm
	        // will do this for us. The parameters to visitMaxs don't matter, but
	        // the method must
	        // be called.
	        mv.visitMaxs(1, 1);
	        // finish up code generation for this method.
	        mv.visitEnd();
	        // end of constructor

	        // create main method which does the following
	        // 1. instantiate an instance of the class being generated, passing the
	        // String[] with command line arguments
	        // 2. invoke the run method.
	        mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null,
	                null);
	        mv.visitCode();
	        Label mainStart = new Label();
	        mv.visitLabel(mainStart);
	        // this is for convenience during development--you can see that the code
	        // is doing something.
	        CodeGenUtils.genPrint(DEVEL, mv, "\nentering main");
	        mv.visitTypeInsn(NEW, className);
	        mv.visitInsn(DUP);
	        mv.visitVarInsn(ALOAD, 0);
	        mv.visitMethodInsn(INVOKESPECIAL, className, "<init>", "([Ljava/lang/String;)V", false);
	        mv.visitMethodInsn(INVOKEVIRTUAL, className, "run", "()V", false);
	        mv.visitInsn(RETURN);
	        Label mainEnd = new Label();
	        mv.visitLabel(mainEnd);
	        mv.visitLocalVariable("args", "[Ljava/lang/String;", null, mainStart, mainEnd, 0);
	        mv.visitLocalVariable("instance", classDesc, null, mainStart, mainEnd, 1);
	        mv.visitMaxs(0, 0);
	        mv.visitEnd();

	        // create run method
	        mv = cw.visitMethod(ACC_PUBLIC, "run", "()V", null, null);
	        mv.visitCode();
	        Label startRun = new Label();
	        mv.visitLabel(startRun);
	        CodeGenUtils.genPrint(DEVEL, mv, "\nentering run");
	        program.getB().visit(this, null);
	        mv.visitInsn(RETURN);
	        Label endRun = new Label();
	        mv.visitLabel(endRun);
	        mv.visitLocalVariable("this", classDesc, null, startRun, endRun, 0);
	        //add the visitlocalvariable method for all variable in localvariable array
	        for (Attributes attributes: localVariables){
	            mv.visitLocalVariable(attributes.dec.getIdent().getText(), attributes.dec.getTypeName().getJVMTypeDesc(), null, attributes.startLabel, attributes.endLabel, attributes.slotNumber);
	        }
	        mv.visitMaxs(1, 1);
	        mv.visitEnd(); // end of run method
	        
	        
	        cw.visitEnd();//end of class
	        
	        //generate classfile and return it
	        return cw.toByteArray();
	}



	@Override
	public Object visitAssignmentStatement(AssignmentStatement assignStatement, Object arg) throws Exception {
		assignStatement.getE().visit(this, arg);
		CodeGenUtils.genPrint(DEVEL, mv, "\nassignment: " + assignStatement.var.getText() + "=");
		CodeGenUtils.genPrintTOS(GRADE, mv, assignStatement.getE().getType());//check with getTypeName
		assignStatement.getVar().visit(this, arg);
		return null;
		
	}

	@Override
	public Object visitBinaryChain(BinaryChain binaryChain, Object arg) throws Exception {
		
				info bChainInfo = new info();
				bChainInfo.arrowKind = binaryChain.getArrow().kind;
				Chain chain = binaryChain.getE0();
				
				bChainInfo.position = "left";
				
				chain.visit(this, bChainInfo);
				if (chain.typeName == TypeName.URL) {
					mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "readFromURL", PLPRuntimeImageIO.readFromURLSig, false);
				} else if (chain.typeName == TypeName.FILE){
					mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "readFromFile", PLPRuntimeImageIO.readFromFileDesc, false);
				} else {
					
				}
				ChainElem chainElem = binaryChain.getE1();
				
				bChainInfo.position = "right";
				chainElem.visit(this, bChainInfo);
				
				
				
		//assert false : "not yet implemented";
		return null;
	}

	@Override
	public Object visitBinaryExpression(BinaryExpression binaryExpression, Object arg) throws Exception {
		
		
		 if (binaryExpression.getTypeName()==TypeName.INTEGER){
	            
	            binaryExpression.getE0().visit(this, arg);
	            binaryExpression.getE1().visit(this, arg);
	            
	            if(binaryExpression.getOp().kind==PLUS){
	            	mv.visitInsn(IADD);
	            }else if(binaryExpression.getOp().kind==MINUS){
	            	mv.visitInsn(ISUB);
	            }else if(binaryExpression.getOp().kind==TIMES){
	            	mv.visitInsn(IMUL);
	            }else if(binaryExpression.getOp().kind==DIV){
	            	mv.visitInsn(IDIV);
	            }else if(binaryExpression.getOp().kind==MOD){
	            	mv.visitInsn(IREM);
	            }
	            
	        } else if (binaryExpression.getTypeName() == TypeName.BOOLEAN){
	            
	            Label label1 = new Label();
	            Label label2 = new Label();
	            int operation = 0;
	            if(binaryExpression.getOp().kind==AND){
	            	binaryExpression.getE0().visit(this, arg);
                    mv.visitJumpInsn(IFEQ, label1);
                    binaryExpression.getE1().visit(this, arg);
                    mv.visitJumpInsn(IFEQ, label1);
                    mv.visitInsn(ICONST_1);
                    mv.visitJumpInsn(GOTO, label2);
                    mv.visitLabel(label1);
                    mv.visitInsn(ICONST_0);
                    mv.visitLabel(label2);
	            }else if(binaryExpression.getOp().kind==OR){
	            	binaryExpression.getE0().visit(this, arg);
                    mv.visitJumpInsn(IFNE, label1);
                    binaryExpression.getE1().visit(this, arg);
                    mv.visitJumpInsn(IFNE, label1);
                    mv.visitInsn(ICONST_0);
                    mv.visitJumpInsn(GOTO, label2);
                    mv.visitLabel(label1);
                    mv.visitInsn(ICONST_1);
                    mv.visitLabel(label2);
	            }else if(binaryExpression.getOp().kind==Kind.EQUAL || binaryExpression.getOp().kind==Kind.NOTEQUAL|| binaryExpression.getOp().kind==Kind.GT||binaryExpression.getOp().kind==Kind.GE||binaryExpression.getOp().kind==Kind.LT||binaryExpression.getOp().kind==Kind.LE){
	            		
	            		binaryExpression.getE0().visit(this, arg);
	                    binaryExpression.getE1().visit(this, arg);
	                    
	                    
	                    
	                    switch (binaryExpression.getOp().kind){
	                        case EQUAL: operation = IF_ICMPEQ;
	                            break;
	                        case NOTEQUAL: operation = IF_ICMPNE;
	                            break;
	                        case GT: operation = IF_ICMPGT;
	                            break;
	                        case GE: operation = IF_ICMPGE;
	                            break;
	                        case LT : operation = IF_ICMPLT;
	                            break;
	                        case LE : operation = IF_ICMPLE;
	                            break;
	                        default:
	                            break;
	                    }
	                    mv.visitJumpInsn(operation, label1);
	                    mv.visitInsn(ICONST_0);
	                    mv.visitJumpInsn(GOTO, label2);
	                    mv.visitLabel(label1);
	                    mv.visitInsn(ICONST_1);
	                    mv.visitLabel(label2);
	            }
	        }else if(binaryExpression.getTypeName() == TypeName.IMAGE){
	        	String functionName,functionDescription;
	        	binaryExpression.getE0().visit(this, arg);
				binaryExpression.getE1().visit(this, arg);
				functionName = null;
				functionDescription = null;
				Kind type= binaryExpression.getOp().kind;
				if(type==PLUS){
					functionName = "add";
					functionDescription = PLPRuntimeImageOps.addSig;
				}else if(type==MINUS){
					functionName = "sub";
					functionDescription = PLPRuntimeImageOps.subSig;
				}else if(type==TIMES){
					functionName = "mul";
					functionDescription = PLPRuntimeImageOps.mulSig;
				}else if(type==DIV){
					functionName = "div";
					functionDescription = PLPRuntimeImageOps.divSig;
				}else if(type==MOD){
					functionName = "mod";
					functionDescription = PLPRuntimeImageOps.modSig;
				}
				
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, functionName, functionDescription, false);
	        }
	            
	        return null;
		
      //TODO  Implement this
	
	}

	
	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		
			Label label1 = new Label();
	        Label label2 = new Label();
	        Attributes attribute;
	        mv.visitLabel(label1);
	        
	        for (Dec dec : block.getDecs()) {
	            
	        	attribute = new Attributes();
	        	attribute.dec = dec;
	        	attribute.endLabel = label2;
	        	attribute.startLabel = label1;
	        	attribute.slotNumber = slotNumber;
	            dec.visit(this, arg);
	            
	            localVariables.add(attribute);
	        }
	        
	        for (Statement stmt : block.getStatements()) {
	        	stmt.visit(this, arg);
	        	if (stmt instanceof Chain){
					
					mv.visitInsn(POP);
				}
	        }
	        
	        mv.visitLabel(label2);
	        
		
		//TODO  Implement this
		return null;
	}

	@Override
	public Object visitBooleanLitExpression(BooleanLitExpression booleanLitExpression, Object arg) throws Exception {
		//TODO Implement this
		mv.visitLdcInsn(booleanLitExpression.getValue());		
		return null;
	}

	@Override
	public Object visitConstantExpression(ConstantExpression constantExpression, Object arg) {
		Kind type = constantExpression.getFirstToken().kind;
		if (type==KW_SCREENWIDTH) {
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFrame.JVMClassName, "getScreenWidth", PLPRuntimeFrame.getScreenWidthSig, false);
		} else if (type==KW_SCREENHEIGHT) {
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFrame.JVMClassName, "getScreenHeight", PLPRuntimeFrame.getScreenHeightSig, false);
		}
		//assert false : "not yet implemented";
		return null;
	}

	@Override
	public Object visitDec(Dec declaration, Object arg) throws Exception {
		//TODO Implement this
		
		declaration.setSlot(slotNumber++);
		if(declaration.typeName == TypeName.FRAME);{
			mv.visitInsn(ACONST_NULL);
			mv.visitVarInsn(ASTORE, declaration.getSlot());
		}
       
		return null;
	}

	@Override
	public Object visitFilterOpChain(FilterOpChain filterOpChain, Object arg) throws Exception {
		String functionName;
	
		filterOpChain.getArg().visit(this, arg);
		functionName = null;
		Kind type = filterOpChain.getFirstToken().kind;//check
		if(type==OP_BLUR){
			functionName = "blurOp"; 
		}else if(type==OP_CONVOLVE){
			functionName = "convolveOp";
		}else if(type==OP_GRAY){
			functionName = "grayOp";
		}
		
		info direction = (info) arg;
		if (direction.arrowKind == Kind.ARROW) {
			mv.visitInsn(ACONST_NULL);
		} else {
			mv.visitInsn(DUP);
			mv.visitInsn(SWAP);
		}
		mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFilterOps.JVMName, functionName, PLPRuntimeFilterOps.opSig, false);
		
		
		//assert false : "not yet implemented";
		return null;
	}

	@Override
	public Object visitFrameOpChain(FrameOpChain frameOpChain, Object arg) throws Exception {
		String functionName,functionDescription;
		Tuple tuple = frameOpChain.getArg();
		tuple.visit(this, arg);
		functionName = null;
		functionDescription = null;
		Kind type = frameOpChain.getFirstToken().kind;
		if(type==KW_SHOW){
			functionName = "showImage";
			functionDescription = PLPRuntimeFrame.showImageDesc;
		}else if(type==KW_HIDE){
			functionName = "hideImage";
			functionDescription = PLPRuntimeFrame.hideImageDesc;
		}else if(type==KW_XLOC){
			functionName = "getXVal";
			functionDescription = PLPRuntimeFrame.getXValDesc;
		}else if(type==KW_YLOC){
			functionName = "getYVal";
			functionDescription = PLPRuntimeFrame.getYValDesc;
		}else if(type==KW_MOVE){
			functionName = "moveFrame";
			functionDescription = PLPRuntimeFrame.moveFrameDesc;
		}
		
		mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeFrame.JVMClassName, functionName, functionDescription, false);
		
		
		//assert false : "not yet implemented";
		return null;
	}

	@Override
	public Object visitIdentChain(IdentChain identChain, Object arg) throws Exception {
		info direction = (info) arg;
		Dec dec = identChain.getDec();
		if (direction.position == "left"){
			TypeName type = dec.getTypeName();
			if(type==TypeName.INTEGER||type==TypeName.BOOLEAN){
				if (dec instanceof ParamDec) {
					mv.visitFieldInsn(GETSTATIC, className, identChain.getFirstToken().getText(), identChain.getDec().getTypeName().getJVMTypeDesc());
				} else {
					mv.visitVarInsn(ILOAD, dec.getSlot());
				}
			}else if(type==TypeName.IMAGE||type==TypeName.FRAME){
				mv.visitVarInsn(ALOAD, dec.getSlot());
			}else if(type==TypeName.FILE||type==TypeName.URL){
				mv.visitFieldInsn(GETSTATIC, className, identChain.getFirstToken().getText(), dec.getTypeName().getJVMTypeDesc());
			}
			
		} else {
			TypeName type = dec.getTypeName();
			if(type == TypeName.INTEGER){
				mv.visitInsn(DUP);
				if (dec instanceof ParamDec){
					mv.visitFieldInsn(PUTSTATIC, className, identChain.getFirstToken().getText(), dec.getTypeName().getJVMTypeDesc());
				} else {
					mv.visitVarInsn(ISTORE, dec.getSlot());
				}
			}else if(type == TypeName.IMAGE){
				mv.visitInsn(DUP);//check
				mv.visitVarInsn(ASTORE, dec.getSlot());
			}else if(type == TypeName.FILE){
				mv.visitFieldInsn(GETSTATIC, className, identChain.getFirstToken().getText(), dec.getTypeName().getJVMTypeDesc());
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "write", PLPRuntimeImageIO.writeImageDesc, false);
				//mv.visitFieldInsn(GETSTATIC, className, identChain.getFirstToken().getText(), dec.getTypeName().getJVMTypeDesc());
			}else if(type == TypeName.FRAME){
			
				mv.visitVarInsn(ALOAD, dec.getSlot());
				mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeFrame.JVMClassName, "createOrSetFrame", PLPRuntimeFrame.createOrSetFrameSig, false);
				mv.visitInsn(DUP);
				mv.visitVarInsn(ASTORE, dec.getSlot());
			}
			
		}
		
		return null;
	}

	@Override
	public Object visitIdentExpression(IdentExpression identExpression, Object arg) throws Exception {
		//TODO Implement this
		
        if (identExpression.dec instanceof ParamDec) {
			mv.visitFieldInsn(GETSTATIC, className, identExpression.getFirstToken().getText(), identExpression.dec.getTypeName().getJVMTypeDesc());
		} else if (identExpression.typeName == TypeName.INTEGER || identExpression.typeName == TypeName.BOOLEAN) {
			mv.visitVarInsn(ILOAD, identExpression.dec.getSlot());
		} else {
			mv.visitVarInsn(ALOAD, identExpression.dec.getSlot());
		}
		return null;
         
		
		
	}
	
	@Override
	public Object visitIdentLValue(IdentLValue identX, Object arg) throws Exception {
		//TODO Implement this
		Dec dec = identX.getDec();
		 if (dec instanceof ParamDec){
			mv.visitFieldInsn(PUTSTATIC, className, identX.getFirstToken().getText(), dec.getTypeName().getJVMTypeDesc());
		} else if (dec.getTypeName() == TypeName.IMAGE){
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "copyImage", PLPRuntimeImageOps.copyImageSig, false);
			mv.visitVarInsn(ASTORE, dec.getSlot());
		}else if (dec.getTypeName() == TypeName.INTEGER || dec.getTypeName() == TypeName.BOOLEAN){
			mv.visitVarInsn(ISTORE, dec.getSlot());
		}  else {
			mv.visitVarInsn(ASTORE, dec.getSlot());
		}
		return null;
	

	}

	@Override
	public Object visitIfStatement(IfStatement ifStatement, Object arg) throws Exception {
		//TODO Implement this
		Label label1 = new Label();
        
        ifStatement.getE().visit(this, arg);
        mv.visitJumpInsn(IFEQ, label1);
        ifStatement.getB().visit(this, arg);
        mv.visitLabel(label1);
        return null;
	}

	@Override
	public Object visitImageOpChain(ImageOpChain imageOpChain, Object arg) throws Exception {
		//assert false : "not yet implemented";
		Tuple tuple = imageOpChain.getArg();
		tuple.visit(this, arg);
		Kind type = imageOpChain.getFirstToken().kind;
		if(type==KW_SCALE){
			mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageOps.JVMName, "scale", PLPRuntimeImageOps.scaleSig, false);
		}else if(type==OP_HEIGHT){
			mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeImageIO.BufferedImageClassName, "getHeight", PLPRuntimeImageOps.getHeightSig, false);
		}else if(type==OP_WIDTH){
			mv.visitMethodInsn(INVOKEVIRTUAL, PLPRuntimeImageIO.BufferedImageClassName, "getWidth", PLPRuntimeImageOps.getWidthSig, false);
		}
	
		return null;
	}

	@Override
	public Object visitIntLitExpression(IntLitExpression intLitExpression, Object arg) throws Exception {
		//TODO Implement this
		 mv.visitLdcInsn(intLitExpression.value);
		 
		 
	     return null;
	}


	@Override
	public Object visitParamDec(ParamDec paramDec, Object arg) throws Exception {
		//TODO Implement this
		
		FieldVisitor fv = cw.visitField(ACC_STATIC, paramDec.getIdent().getText(), paramDec.getTypeName().getJVMTypeDesc(), null, null);
        fv.visitEnd();
        
        TypeName type = paramDec.getTypeName();
        
        if(type==TypeName.INTEGER){
        	mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            
            mv.visitIntInsn(BIPUSH, paramDecArguments++);
            
            mv.visitInsn(AALOAD);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I", false);
            
        }else if(type==TypeName.BOOLEAN){
        	mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            
            mv.visitIntInsn(BIPUSH, paramDecArguments++);
            
            mv.visitInsn(AALOAD);
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "parseBoolean", "(Ljava/lang/String;)Z", false);
            
        }else if(type == TypeName.FILE){
        	 mv.visitTypeInsn(NEW, "java/io/File");
             mv.visitInsn(DUP);
             
             mv.visitVarInsn(ALOAD, 1);
             
             mv.visitIntInsn(BIPUSH, paramDecArguments++);
             
             mv.visitInsn(AALOAD);
             mv.visitMethodInsn(INVOKESPECIAL, "java/io/File", "<init>", "(Ljava/lang/String;)V", false);
             
        }else if(type == TypeName.URL){
        	 mv.visitVarInsn(ALOAD, 1);
             mv.visitIntInsn(BIPUSH, paramDecArguments++);
             mv.visitMethodInsn(INVOKESTATIC, PLPRuntimeImageIO.className, "getURL", PLPRuntimeImageIO.getURLSig, false);
        }
        
       
       
        mv.visitFieldInsn(PUTSTATIC, className, paramDec.getIdent().getText(), paramDec.getTypeName().getJVMTypeDesc());
        
        return null;

	}

	@Override
	public Object visitSleepStatement(SleepStatement sleepStatement, Object arg) throws Exception {
		sleepStatement.getE().visit(this, arg);
        if (sleepStatement.getE().getTypeName() == TypeName.INTEGER) {
            mv.visitInsn(I2L);
        }
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "sleep", "(J)V", false);
		 
		
		//assert false : "not yet implemented";
        //check this once again
		return null;
	}

	@Override
	public Object visitTuple(Tuple tuple, Object arg) throws Exception {
		List<Expression> expression = tuple.getExprList();
		for(Expression expressions:expression){
			expressions.visit(this, arg);
		}
       
		return null;
	}

	@Override
	public Object visitWhileStatement(WhileStatement whileStatement, Object arg) throws Exception {
		Label label1 = new Label();
		Label label2 = new Label();
		mv.visitLabel(label1);
		whileStatement.getE().visit(this, arg);
		mv.visitJumpInsn(IFEQ, label2);
		whileStatement.getB().visit(this, arg);
		mv.visitJumpInsn(GOTO, label1);
		mv.visitLabel(label2);
		//TODO Implement this
		return null;
		
	}

}
