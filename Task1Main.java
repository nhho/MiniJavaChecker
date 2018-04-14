import syntaxtree.*;
import visitor.*;
import myparser.*;

public class Task1Main {
    public static void main(String [] args) {
        if (args.length == 0) {
            System.err.println("Missing an argument");
            System.exit(1);
        }
        String Y = args[0];
        try {
            Program root = new MiniJavaParser(System.in).Goal();
            BuildSymbolTableVisitor buildSymTab = new BuildSymbolTableVisitor(Y);
            root.accept(buildSymTab);
        } catch (ParseException e) {
            System.out.println(e.toString());
        }
    }
}
