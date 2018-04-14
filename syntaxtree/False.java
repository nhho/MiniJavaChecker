package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class False extends Exp {
  public False(int aline, int acolumn) { 
    line=aline; column=acolumn;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
