package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class NewObject extends Exp {
  public IdentifierType i;
  
  public NewObject(IdentifierType ai, int aline, int acolumn) { 
    i=ai; line=aline; column=acolumn;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
