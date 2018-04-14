package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class DoubleType extends Type {
  public DoubleType(int aline, int acolumn) { 
    line=aline; column=acolumn;
  }
  
  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }

  public String toString() {
    return "double";
  }

  public DoubleType instance(int aline, int acolumn) {
    return new DoubleType(aline, acolumn);
  }
}
