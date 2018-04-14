package visitor;
import syntaxtree.*;
import java.util.Vector;

public class TypeCheckExpVisitor extends TypeDepthFirstVisitor {
   

  // Exp e1,e2;
  public Type visit(And n) {
	Type t1 = n.e1.accept(this);
	if (t1 != null && !(t1 instanceof BooleanType) ) {
       System.out.println("line " + n.e1.line + ",column " + n.e1.column + ": left side of And must be of type boolean, found " + t1.toString());
	   t1 = null;
    }
	Type t2 = n.e2.accept(this);
	if (t2 != null && !(t2 instanceof BooleanType) ) {
       System.out.println("line " + n.e2.line + ",column " + n.e2.column + ": right side of And must be of type boolean, found " + t2.toString());
	   t2 = null;
    }
	if (t1 == null || t2 == null)
		return null;
    return new BooleanType(n.line, n.column);
  }

  // Exp e1,e2;
  public Type visit(LessThan n) {
	Type t1 = n.e1.accept(this);
    if (t1 != null && ! (t1 instanceof IntegerType) && ! (t1 instanceof DoubleType)) {
       System.out.println("line " + n.e1.line + ",column " + n.e1.column + ": left side of LessThan must be of type integer or double, found " + t1.toString());
	   t1 = null;
    }
	Type t2 = n.e2.accept(this);
    if (t2 != null && ! (t2 instanceof IntegerType) && ! (t2 instanceof DoubleType)) {
       System.out.println("line " + n.e2.line + ",column " + n.e2.column + ": right side of LessThan must be of type integer or double, found " + t2.toString());
	   t2 = null;
    }
	if (t1 == null || t2 == null)
		return null;
    return new BooleanType(n.line, n.column);
  }

  // Exp e1,e2;
  public Type visit(Plus n) {
	Type t1 = n.e1.accept(this);
    if (t1 != null && ! (t1 instanceof IntegerType) && ! (t1 instanceof DoubleType)) {
       System.out.println("line " + n.e1.line + ",column " + n.e1.column + ": left side of Plus must be of type integer or double, found " + t1.toString());
	   t1 = null;
    }
	Type t2 = n.e2.accept(this);
    if (t2 != null && ! (t2 instanceof IntegerType) && ! (t2 instanceof DoubleType)) {
       System.out.println("line " + n.e2.line + ",column " + n.e2.column + ": right side of Plus must be of type integer or double, found " + t2.toString());
	   t2 = null;
    }
	if (t1 == null || t2 == null)
		return null;
	if ((t1 instanceof IntegerType) && (t2 instanceof IntegerType))
		return new IntegerType(n.line, n.column);
    return new DoubleType(n.line, n.column);
  }

  // Exp e1,e2;
  public Type visit(Minus n) {
	Type t1 = n.e1.accept(this);
    if (t1 != null && ! (t1 instanceof IntegerType) && ! (t1 instanceof DoubleType)) {
       System.out.println("line " + n.e1.line + ",column " + n.e1.column + ": left side of Minus must be of type integer or double, found " + t1.toString());
	   t1 = null;
    }
	Type t2 = n.e2.accept(this);
    if (t2 != null && ! (t2 instanceof IntegerType) && ! (t2 instanceof DoubleType)) {
       System.out.println("line " + n.e2.line + ",column " + n.e2.column + ": right side of Minus must be of type integer or double, found " + t2.toString());
	   t2 = null;
    }
	if (t1 == null || t2 == null)
		return null;
	if ((t1 instanceof IntegerType) && (t2 instanceof IntegerType))
		return new IntegerType(n.line, n.column);
    return new DoubleType(n.line, n.column);
  }

  // Exp e1,e2;
  public Type visit(Times n) {
	Type t1 = n.e1.accept(this);
    if (t1 != null && ! (t1 instanceof IntegerType) && ! (t1 instanceof DoubleType)) {
       System.out.println("line " + n.e1.line + ",column " + n.e1.column + ": left side of Times must be of type integer or double, found " + t1.toString());
	   t1 = null;
    }
	Type t2 = n.e2.accept(this);
    if (t2 != null && ! (t2 instanceof IntegerType) && ! (t2 instanceof DoubleType)) {
       System.out.println("line " + n.e2.line + ",column " + n.e2.column + ": right side of Times must be of type integer or double, found " + t2.toString());
	   t2 = null;
    }
	if (t1 == null || t2 == null)
		return null;
	if ((t1 instanceof IntegerType) && (t2 instanceof IntegerType))
		return new IntegerType(n.line, n.column);
    return new DoubleType(n.line, n.column);
  }

  // Exp e1,e2;
  public Type visit(ArrayLookup n) {
	Type t1 = n.e1.accept(this);
    if (t1 != null && ! (t1 instanceof IntArrayType)) {
       System.out.println("line " + n.e1.line + ",column " + n.e1.column + ": left side of ArrayLookup must be of type integer array, found " + t1.toString());
	   t1 = null;
    }
	Type t2 = n.e2.accept(this);
    if (t2 != null && ! (t2 instanceof IntegerType)) {
       System.out.println("line " + n.e2.line + ",column " + n.e2.column + ": right side of ArrayLookup must be of type integer, found " + t2.toString());
	   t2 = null;
    }
	if (t1 == null || t2 == null)
		return null;
	return new IntegerType(n.line, n.column);
  }

  // Exp e;
  public Type visit(ArrayLength n) {
	Type t1 = n.e.accept(this);
    if (t1 != null && ! (t1 instanceof IntArrayType)) {
       System.out.println("line " + n.e.line + ",column " + n.e.column + ": left side of ArrayLength must be of type integer array, found " + t1.toString());
	   t1 = null;
    }
	if (t1 == null)
		return null;
    return new IntegerType(n.line, n.column);
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  public Type visit(Call n) {
    String mname = n.i.toString();

	Type t1 = n.e.accept(this);
    if (t1 != null && ! (t1 instanceof InstanceType)) {
       System.out.println("line " + n.e.line + ",column " + n.e.column + ": method " + mname + " called on something that is not an instance of an object, found " + t1.toString());
	   t1 = null;
    } 

	boolean gg = (t1 == null);
	Vector<Type> vt = new Vector<Type>();
    for ( int i = 0; i < n.el.size(); i++ ) {
		Type t2 = n.el.elementAt(i).accept(this);
		if (t2 == null)
			gg = true;
		else
			vt.addElement(t2);
    }
	if (gg)
		return null;
	
    String cname = t1.toString();
	Class tmp = TypeCheckVisitor.symbolTable.getClass(cname);
	// System.out.println("/////////////// TEST calling class " + tmp.id + " method " + mname);
    Method m = tmp.getMethod2(mname, vt, n.i.line, n.i.column);
	if (m == null)
		return null;
	return m.type.instance(n.e.line, n.e.column);
  }

  // int i;
  public Type visit(IntegerLiteral n) {
    return new IntegerType(n.line, n.column);
  }

  public Type visit(True n) {
    return new BooleanType(n.line, n.column);
  }

  public Type visit(False n) {
    return new BooleanType(n.line, n.column);
  }

  // String s;
  public Type visit(IdentifierExp n) {
	return TypeCheckVisitor.symbolTable.getVar(TypeCheckVisitor.currMethod, TypeCheckVisitor.currClass, n.s).type.instance(n.line, n.column);
  }

  public Type visit(This n) {
      return new InstanceType(TypeCheckVisitor.currClass.type().toString(), n.line, n.column);
  }

  // Exp e;
  public Type visit(NewArray n) {
	Type t1 = n.e.accept(this);
    if (t1 != null && ! (t1 instanceof IntegerType)) {
       System.out.println("line " + n.e.line + ",column " + n.e.column + ": argument of NewArray must be of type integer, found " + t1.toString());
	   t1 = null;
    }
	if (t1 == null)
		return null;
    return new IntArrayType(n.line, n.column);
  }

  // IdentifierType i;
  public Type visit(NewObject n) {
    return new InstanceType(n.i.s, n.line, n.column);
  }

  // Exp e;
  public Type visit(Not n) {
	Type t1 = n.e.accept(this);
    if (t1 != null && ! (t1 instanceof BooleanType)) {
       System.out.println("line " + n.e.line + ",column " + n.e.column + ": right side of Not must be of type boolean, found " + t1.toString());
	   t1 = null;
    }
	if (t1 == null)
		return null;
    return new BooleanType(n.line, n.column);
  }

}
 //TypeCheckVisitor.






