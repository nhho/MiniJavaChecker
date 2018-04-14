package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

/*
   class i extends j {
     (Variable declarations)*
     (Method declaration)*
   }
*/
public class ClassDeclExtends extends ClassDecl {
  public IdentifierType i;
  public IdentifierType j;
  public VarDeclList vl;      // Sequence of variable declarations
  public MethodDeclList ml;   // Sequence of method declarations
  public int line;
  public int column;
 
  public ClassDeclExtends(IdentifierType ai, IdentifierType aj, 
                  VarDeclList avl, MethodDeclList aml, int aline, int acolumn) {
    i=ai; j=aj; vl=avl; ml=aml; line=aline; column=acolumn;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
