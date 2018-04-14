package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class IntArrayType extends Type {
  public IntArrayType(int aline, int acolumn) { 
    line=aline; column=acolumn;
  }
  
  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }

  public String toString() {
    return "int []";
  }

  public IntArrayType instance(int aline, int acolumn) {
    return new IntArrayType(aline, acolumn);
  }
}
