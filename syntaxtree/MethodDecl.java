package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class MethodDecl {
  public Type t;
  public Identifier i;
  public FormalList fl;
  public VarDeclList vl;
  public StatementList sl;
  public Exp e;
  public int line;
  public int column;
  public int idRef;

  public MethodDecl(Type at, Identifier ai, FormalList afl, VarDeclList avl, 
                    StatementList asl, Exp ae, int aline, int acolumn) {
    t=at; i=ai; fl=afl; vl=avl; sl=asl; e=ae; line=aline; column=acolumn;
  }
 
  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}
