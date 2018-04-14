package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class IdentifierExp extends Exp {
  public String s;

  public IdentifierExp(String as, int aline, int acolumn) { 
    s=as; line=aline; column=acolumn;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
