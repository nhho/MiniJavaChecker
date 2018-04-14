package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class ClassDeclSimple extends ClassDecl {
  public IdentifierType i;
  public VarDeclList vl;  
  public MethodDeclList ml;
  public int line;
  public int column;
 
  public ClassDeclSimple(IdentifierType ai, VarDeclList avl, MethodDeclList aml, int aline, int acolumn) {
    i=ai; vl=avl; ml=aml; line=aline; column=acolumn;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
