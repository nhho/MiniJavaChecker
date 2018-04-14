package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class BooleanType extends Type {
  public BooleanType(int aline, int acolumn) { 
    line=aline; column=acolumn;
  }
  
  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }

  public String toString() {
    return "boolean";
  }

  public BooleanType instance(int aline, int acolumn) {
    return new BooleanType(aline, acolumn);
  }
}
