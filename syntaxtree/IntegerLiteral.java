package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class IntegerLiteral extends Exp {
  public int i;

  public IntegerLiteral(int ai, int aline, int acolumn) { 
    i=ai; line=aline; column=acolumn;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
