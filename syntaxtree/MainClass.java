package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class MainClass {
  public IdentifierType i1;
  public Identifier i2;
  public Statement s;
  public int line1;
  public int column1;
  public int line2;
  public int column2;
  public int line3;
  public int column3;
  public int line4;
  public int column4;

  public MainClass(IdentifierType ai1, Identifier ai2, Statement as, int aline1, int acolumn1, int aline2, int acolumn2, int aline3, int acolumn3, int aline4, int acolumn4) {
    i1=ai1; i2=ai2; s=as; line1=aline1; column1=acolumn1; line2=aline2; column2=acolumn2; line3=aline3; column3=acolumn3; line4=aline4; column4=acolumn4;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}

