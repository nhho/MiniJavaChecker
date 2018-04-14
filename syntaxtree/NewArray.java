package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class NewArray extends Exp {
  public Exp e;
  
  public NewArray(Exp ae, int aline, int acolumn) { 
    e=ae; line=aline; column=acolumn;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
