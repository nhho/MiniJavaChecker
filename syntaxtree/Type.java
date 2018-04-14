package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

// Data type
public abstract class Type {
  public int line;
  public int column;
  
  public abstract void accept(Visitor v);
  public abstract Type accept(TypeVisitor v);
  public abstract String toString();
  public abstract Type instance(int aline, int acolumn);
}
