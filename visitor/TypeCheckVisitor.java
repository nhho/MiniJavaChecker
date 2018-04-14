package visitor;
import syntaxtree.*;

public class TypeCheckVisitor extends DepthFirstVisitor {

  static Class currClass;
  static Method currMethod;
  static SymbolTable symbolTable;
   
  public TypeCheckVisitor(SymbolTable s){
    symbolTable = s;
  }

  // MainClass m;
  // ClassDeclList cl;
  public void visit(Program n) {
    n.m.accept(this);
    for ( int i = 0; i < n.cl.size(); i++ ) {
        n.cl.elementAt(i).accept(this);
    }
  }
  
  // IdentifierType i1;
  // Identifier i2;
  // Statement s;
  public void visit(MainClass n) {
      String i1 = n.i1.toString();
      currClass = symbolTable.getClass(i1);
      
      //n.i2.accept(this);
      n.s.accept(this);
  }
  
  // IdentifierType i;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclSimple n) {
    String id = n.i.toString();
    currClass = symbolTable.getClass(id);
	/*
    for ( int i = 0; i < n.vl.size(); i++ ) {
        n.vl.elementAt(i).accept(this);
    }
	*/
    for ( int i = 0; i < n.ml.size(); i++ ) {
        n.ml.elementAt(i).accept(this);
    }
  }
 
  // IdentifierType i;
  // IdentifierType j;
  // VarDeclList vl;
  // MethodDeclList ml;
  public void visit(ClassDeclExtends n) {
    String id = n.i.toString();
    currClass = symbolTable.getClass(id);
    // n.j.accept(this);
	/*
    for ( int i = 0; i < n.vl.size(); i++ ) {
        n.vl.elementAt(i).accept(this);
    }
	*/
    for ( int i = 0; i < n.ml.size(); i++ ) {
        n.ml.elementAt(i).accept(this);
    }
  }

  /*
  // Type t;
  // Identifier i;
  public void visit(VarDecl n) {
    n.t.accept(this);
    n.i.accept(this);
  }
  */

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  public void visit(MethodDecl n) {
    // n.t.accept(this);
    String id = n.i.toString();
    currMethod = currClass.getMethodById(n.idRef);
	// System.out.println("/////////////// TEST declare class " + currClass.id +  " method " + currMethod.id);
    Type retType = currMethod.type();
	/*
    for ( int i = 0; i < n.fl.size(); i++ ) {
        n.fl.elementAt(i).accept(this);
    }
    for ( int i = 0; i < n.vl.size(); i++ ) {
        n.vl.elementAt(i).accept(this);
    }
	*/
    for ( int i = 0; i < n.sl.size(); i++ ) {
        n.sl.elementAt(i).accept(this);
    }
	Type t1 = n.e.accept(new TypeCheckExpVisitor());
    if (t1 != null && !symbolTable.compareTypes2(t1, retType)) {
		System.out.println("line " + n.e.line + ",column " + n.e.column + ": return type for method " + id + " must be " + retType.toString() + ", found " + t1.toString());
    }
  }

  /*
  // Type t;
  // Identifier i;
  public void visit(Formal n) {
      n.t.accept(this);
      n.i.accept(this);
  }
  */

  // Exp e;
  // Statement s1,s2;
  public void visit(If n) {
	Type t1 = n.e.accept(new TypeCheckExpVisitor());
	if (t1 != null && !(t1 instanceof BooleanType) ) {
       System.out.println("line " + n.e.line + ",column " + n.e.column + ": condition of If must be of type boolean, found " + t1.toString());
	   t1 = null;
    }
    n.s1.accept(this);
    n.s2.accept(this);
  }

  // Exp e;
  // Statement s;
  public void visit(While n) {
	Type t1 = n.e.accept(new TypeCheckExpVisitor());
	if (t1 != null && !(t1 instanceof BooleanType) ) {
       System.out.println("line " + n.e.line + ",column " + n.e.column + ": condition of While must be of type boolean, found " + t1.toString());
	   t1 = null;
    }
    n.s.accept(this);
  }

  // Exp e;
  public void visit(Print n) {
	Type t1 = n.e.accept(new TypeCheckExpVisitor());
	if (t1 != null && !(t1 instanceof IntegerType) ) {
       System.out.println("line " + n.e.line + ",column " + n.e.column + ": argument of Print must be of type integer, found " + t1.toString());
    }
  }
  
  // Identifier i;
  // Exp e;
  public void visit(Assign n) {
    Type t1 = symbolTable.getVar(currMethod,currClass,n.i.toString()).type;
    Type t2 = n.e.accept(new TypeCheckExpVisitor());
	if (t2 != null && !symbolTable.compareTypes2(t2,t1)){
		System.out.println("line " + n.i.line + ",column " + n.i.column + ": assigning " + t2.toString() + " to " + t1.toString());
    }
  }

  // Identifier i;
  // Exp e1,e2;
  public void visit(ArrayAssign n) {
    Type t0 = symbolTable.getVar(currMethod,currClass,n.i.toString()).type;      
      if (! (t0 instanceof IntArrayType) ) {
		System.out.println("line " + n.i.line + ",column " + n.i.column + ": element must be query from integer array, found " + t0.toString());
      }
	Type t1 = n.e1.accept(new TypeCheckExpVisitor());
    if (t1 != null && ! (t1 instanceof IntegerType)) {
       System.out.println("line " + n.e1.line + ",column " + n.e1.column + ": index of array must be of type integer, found " + t1.toString());
    }
	Type t2 = n.e2.accept(new TypeCheckExpVisitor());
    if (t2 != null && ! (t2 instanceof IntegerType)) {
       System.out.println("line " + n.e2.line + ",column " + n.e2.column + ": right side of ArrayAssign must be of type integer, found " + t2.toString());
    }
  }
}



