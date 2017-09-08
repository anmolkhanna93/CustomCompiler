package cop5556sp17.AST;

import cop5556sp17.Scanner.Token;


public abstract class Chain extends Statement {
	public Type.TypeName typeName =null;
	
	public Chain(Token firstToken) {
		super(firstToken);
	}
	public Type.TypeName getTypeName() {
		return typeName;
	}

	public void setTypeName(Type.TypeName typeName) {
		this.typeName = typeName;
	}
	
	public Type.TypeName getType() {
		return typeName;
	}

	public void setType(Type.TypeName typeName) {
		this.typeName = typeName;
	}
	

}
