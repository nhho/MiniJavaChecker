package visitor;
import syntaxtree.*;
import java.util.Hashtable;
import java.util.Enumeration;

public class BuildSymbolTableVisitor extends TypeDepthFirstVisitor {

  SymbolTable symbolTable;
  private int state;
  private String Y;
  private int need, done;
  // 0: check class name conflicts
  // 1: check class extension
  // other: dfs

  public BuildSymbolTableVisitor(String aY) {
    symbolTable = new SymbolTable();
	Y = aY;
	need = 0;
	done = 0;
  }
  
  private String dummyConvert(String a) {
	if (Character.isDigit(a.charAt(0))) {
      return "(dummy)";
	}
	return a;
  }

  public SymbolTable getSymTab() {
    return symbolTable;
  }

  // In global scope => both currClass and currMethod are null
  //   Contains class declaration
  // Inside a class (but not in a method) => currMethod is null
  //   Contains field and method declarations
  // Inside a method
  //   Contains declaration of local variables 
  // These two variables help keep track of the current scope.
  private Class currClass;
  private Method currMethod;
 
  // Note: Because in MiniJava there is no nested scopes and all local 
  // variables can only be declared at the beginning of a method. This "hack"
  // uses two variables instead of a stack to track nested level.


  // MainClass m;
  // ClassDeclList cl;
  public Type visit(Program n) {
	for ( state = 0; state < 3 || done < need; state++ ) {
		// System.out.println("/// TEST need: " + need + " done: " + done);
		n.m.accept(this);  // Main class declaration

		// Declaration of remaining classes
		for ( int i = 0; i < n.cl.size(); i++ ) {
			n.cl.elementAt(i).accept(this);
		}
	}
		// System.out.println("/// TEST need: " + need + " done: " + done);
    return null;
  }
  
  // IdentifierType i1 (name of class)
  // Identifier i2 (name of argument in main();
  // Statement s;
  public Type visit(MainClass n) {
	if (state == 0) {
     symbolTable.addClass( n.i1.s, null, n.line1, n.column1); 
    } else if (state == 1) {
		need++;
	} else {
     currClass = symbolTable.getClass(n.i1.s);
	 if (!currClass.vis){
	 currClass.vis = true;
	  if (n.i1.s.equals(Y)) {
	    System.out.println(currClass.idRef + ", Class");
	  }
    //this is an ugly hack.. but its not worth having a Void and
    //String[] type just for one occourance
    currMethod = new Method ("main", new IdentifierType("void", n.line4, n.column4), n.line2, n.column2);
	// assume not to be called, overloaded nor tested
    currMethod.addVar(n.i2.toString(),
		      new IdentifierType("String[]", n.line3, n.column3));
	// assume not to be tested
    n.s.accept(this);

    currMethod = null;
	  done++;
    }}
    return null;
  }
  
  // IdentifierType i;  (Class name)
  // VarDeclList vl;  (Field declaration)
  // MethodDeclList ml; (Method declaration)
  public Type visit(ClassDeclSimple n) {
	if (state == 0) {
    if (!symbolTable.addClass( n.i.s, null, n.line, n.column)){
	System.out.println(n.i.s + ": Redeclaration (line " + symbolTable.failLine + ",column " + symbolTable.failColumn + " and line " + n.line + ",column " + n.column + ")");
	n.i.s = Integer.toString(symbolTable.dummyCnt);
    }
    } else if (state == 1) {
		need++;
	} else {
    // Entering a new class scope (no need to explicitly leave a class scope)
    currClass =  symbolTable.getClass(n.i.s);
	 if (!currClass.vis){
	 currClass.vis = true;
	  if (n.i.s.equals(Y)) {
	    System.out.println(currClass.idRef + ", Class");
	  }

    // Process field declaration
    for ( int i = 0; i < n.vl.size(); i++ ) {
      n.vl.elementAt(i).accept(this);
    }

    // Process method declaration
    for ( int i = 0; i < n.ml.size(); i++ ) {
      n.ml.elementAt(i).accept(this);
    }
	  done++;
	}}
    return null;
  }
 
  // IdentifierType i; (Class name)
  // IdentifierType j; (Superclass's name)
  // VarDeclList vl;  (Field declaration)
  // MethodDeclList ml; (Method declaration)
  public Type visit(ClassDeclExtends n) {
	if (state == 0) {
    if (!symbolTable.addClass( n.i.s,  n.j.s, n.line, n.column)) {
      System.out.println(n.i.s + ": Redeclaration (line " + symbolTable.failLine + ",column " + symbolTable.failColumn + " and line " + n.line + ",column " + n.column + ")");
	  n.i.s = Integer.toString(symbolTable.dummyCnt);
    }
    } else if (state == 1) {
	  Class tmp = symbolTable.getClass(n.i.s);
	  if (!symbolTable.containsClass(tmp.parent)) {
		System.out.println(tmp.parent + ": Unknown identifier (line " + n.j.line + ",column " + n.j.column + ")");
		tmp.parent = null; // don't use n.j anymore
	  }
	  need++;
	} else {
    // Entering a new class scope (no need to explicitly leave a class scope)
    currClass = symbolTable.getClass(n.i.s);
		if (currClass.parent != null) {
			Class tmp = symbolTable.getClass(currClass.parent);
			if (!tmp.vis)
					return null;
		}
		
	 if (!currClass.vis){
	 currClass.vis = true;
	  if (n.i.s.equals(Y)) {
	    System.out.print(currClass.idRef + ", Class");
		Class tmp = currClass;
		while (tmp.parent != null) {
			System.out.print(", " + tmp.parent);
			tmp = symbolTable.getClass(tmp.parent);
		}
		System.out.println();
	  }
	  if (currClass.parent != null) {
		n.j.accept(this);
	  }
  
    for ( int i = 0; i < n.vl.size(); i++ ) {
      n.vl.elementAt(i).accept(this);
    }
    for ( int i = 0; i < n.ml.size(); i++ ) {
      n.ml.elementAt(i).accept(this);
    }
	  done++;
	}}
    return null;
  }

  // Type t;
  // Identifier i;
  //
  // Field delcaration or local variable declaration
  public Type visit(VarDecl n) {
    
    Type t =  n.t.accept(this);
	if (t == null) {
	  return null;
	}
    String id =  n.i.toString();

    // Not inside a method => a field declaration
    if (currMethod == null) {

      // Add a field
      if (!currClass.addVar(id,t)) {
      System.out.println(id + ": Redeclaration (line " + currClass.failLine + ",column " + currClass.failColumn + " and line " + t.line + ",column " + t.column + ")");
	  n.i.s = Integer.toString(currClass.dummyCnt);
      }
	  if (n.i.s.equals(Y)) {
	    Variable tmp = currClass.getVar(n.i.s);
	    System.out.println(tmp.idRef + ", Data member, " + t.toString() + ", " + dummyConvert(currClass.id));
	  }
    } else {
      // Add a local variable
      if (!currMethod.addVar(id,t)){
      System.out.println(id + ": Redeclaration (line " + currMethod.failLine + ",column " + currMethod.failColumn + " and line " + t.line + ",column " + t.column + ")");
	  n.i.s = Integer.toString(currMethod.dummyCnt);
      }
	  if (n.i.s.equals(Y)) {
	    Variable tmp = currMethod.getVar(n.i.s);
	    System.out.println(tmp.idRef + ", Local, " + t.toString() + ", " + dummyConvert(currClass.id) + "::" + dummyConvert(currMethod.id) + "()");
	  }
    }
    return t;
  }

  // Type t;  (Return type)
  // Identifier i; (Method name)
  // FormalList fl; (Formal parameters)
  // VarDeclList vl; (Local variables)
  // StatementList sl; 
  // Exp e; (The expression that evaluates to the return value)
  //
  // Method delcaration
  public Type visit(MethodDecl n) {
    Type t = n.t.accept(this);
	if (t == null) return null;

    String id = n.i.toString();
    
    // Entering a method scope 
    currMethod = new Method(id,t,n.line,n.column);

	boolean gg = false;
    for ( int i = 0; i < n.fl.size(); i++ ) {
      if (n.fl.elementAt(i).accept(this) == null) {
		  gg = true;
	  }
    }
	if (gg) return null;
    if (!currClass.addMethod(id,t,currMethod)){
      System.out.println(id + ": Redeclaration (line " + currClass.failLine + ",column " + currClass.failColumn + " and line " + n.line + ",column " + n.column + ")");
	  n.i.s = Integer.toString(currClass.dummyCnt);
    }
	n.idRef = currMethod.idRef;
    if (n.i.s.equals(Y)) {
	  System.out.print(currMethod.idRef + ", " + dummyConvert(currClass.id) + ", " + t.toString() + " (");
		for ( int i = 0; i < n.fl.size(); i++ ) {
			if (i > 0)
				 System.out.print(", ");
		  System.out.print(n.fl.elementAt(i).t.toString() + " " + dummyConvert(n.fl.elementAt(i).i.s));
		}
	  System.out.println(")");
    }
	
    for ( int i = 0; i < n.vl.size(); i++ ) {
      n.vl.elementAt(i).accept(this);
    }
    for ( int i = 0; i < n.sl.size(); i++ ) {
      n.sl.elementAt(i).accept(this);
    }

    n.e.accept(this);

    // Leaving a method scope (return to class scope)
    currMethod = null;
    return null;
  }

  // Type t;
  // Identifier i;
  // 
  // Register a formal parameter
  public Type visit(Formal n) {
      
    Type t = n.t.accept(this);
	if (t == null) {
	  return null;
	}
    String id = n.i.toString();
    
    if (!currMethod.addParam(id,t)){
      System.out.println(id + ": Redeclaration (line " + currMethod.failLine + ",column " + currMethod.failColumn + " and line " + t.line + ",column " + t.column + ")");
	  n.i.s = Integer.toString(currMethod.dummyCnt);
    }
	if (n.i.s.equals(Y)) {
	  Variable tmp = currMethod.getVar(n.i.s);
	  System.out.println(tmp.idRef + ", Param, " + t.toString() + ", " + dummyConvert(currClass.id) + "::" + dummyConvert(currMethod.id) + "()");
	}
    return t;
  }

  public Type visit(IntArrayType n) {
    return n; 
  }

  public Type visit(BooleanType n) {
    return n;
  }

  public Type visit(IntegerType n) {
    return n;
  }

  public Type visit(DoubleType n) {
    return n;
  }

  // String s;
  // assume no declaration here
  public Type visit(IdentifierType n) {
	if (!symbolTable.containsClass(n.s)) {
	  System.out.println(n.s + ": Unknown identifier (line " + n.line + ",column " + n.column + ")");
	  return null;
	}
	if (n.s.equals(Y)) {
	  Class tmp = symbolTable.getClass(n.s);
	  System.out.println("line " + n.line + ",column " + n.column + ": " + tmp.idRef);
	}
    return n;
  }

  // StatementList sl;
  // Optional for MiniJava (unless variable declaration is allowed inside
  // a block
  public Type visit(Block n) {
    for ( int i = 0; i < n.sl.size(); i++ ) {
        n.sl.elementAt(i).accept(this);
    }
    return null;
  }

  // Exp e;
  // Statement s1,s2;
  public Type visit(If n) {
    n.e.accept(this);
    n.s1.accept(this);
    n.s2.accept(this);
    return null;
  }

  // Exp e;
  // Statement s;
  public Type visit(While n) {
    n.e.accept(this);
    n.s.accept(this);
    return null;
  }

  // Exp e;
  public Type visit(Print n) {
    n.e.accept(this);
    return null;
  }
  
  // Identifier i;
  // Exp e;
  public Type visit(Assign n) {
	Variable tmp = symbolTable.getVar(currMethod, currClass, n.i.s);
	if (tmp == null) {
      System.out.println(n.i.s + ": Unknown identifier (line " + n.i.line + ",column " + n.i.column + ")");
	  n.i.s = null;
	} else {
	  if (n.i.s.equals(Y)) {
	    System.out.println("line " + n.i.line + ",column " + n.i.column + ": " + tmp.idRef);
  	  }
	}
    n.e.accept(this);
    return null;
  }

  // Identifier i;
  // Exp e1,e2;
  public Type visit(ArrayAssign n) {
	Variable tmp = symbolTable.getVar(currMethod, currClass, n.i.s);
	if (tmp == null) {
      System.out.println(n.i.s + ": Unknown identifier (line " + n.i.line + ",column " + n.i.column + ")");
	  n.i.s = null;
	} else {
	  if (n.i.s.equals(Y)) {
	    System.out.println("line " + n.i.line + ",column " + n.i.column + ": " + tmp.idRef);
  	  }
	}
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e1,e2;
  public Type visit(And n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e1,e2;
  public Type visit(LessThan n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e1,e2;
  public Type visit(Plus n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e1,e2;
  public Type visit(Minus n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e1,e2;
  public Type visit(Times n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e1,e2;
  public Type visit(ArrayLookup n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e;
  public Type visit(ArrayLength n) {
    n.e.accept(this);
    return null;
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public Type visit(Call n) {
    n.e.accept(this);
	// NOT IN TASK 1
    // n.i.accept(this);
    for ( int i = 0; i < n.el.size(); i++ ) {
        n.el.elementAt(i).accept(this);
    }
    return null;
  }

  // int i;
  public Type visit(IntegerLiteral n) {
    return null;
  }

  public Type visit(True n) {
    return null;
  }

  public Type visit(False n) {
    return null;
  }

  // String s;
  public Type visit(IdentifierExp n) {
	Variable tmp = symbolTable.getVar(currMethod, currClass, n.s);
	if (tmp == null) {
      System.out.println(n.s + ": Unknown identifier (line " + n.line + ",column " + n.column + ")");
	  n.s = null;
	  return null;
	}
	if (n.s.equals(Y)) {
	  System.out.println("line " + n.line + ",column " + n.column + ": " + tmp.idRef);
  	}
    return tmp.type();
  }

  public Type visit(This n) {
    return null;
  }

  // Exp e;
  public Type visit(NewArray n) {
    n.e.accept(this);
    return null;
  }

  // IdentifierType i;
  public Type visit(NewObject n) {
    n.i.accept(this);
    return null;
  }

  // Exp e;
  public Type visit(Not n) {
    n.e.accept(this);
    return null;
  }

  // String s;
  // no access
  public Type visit(Identifier n) {
    return null;
  }
}
