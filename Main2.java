import syntaxtree.*;
import visitor.*;
import myparser.*;

// This one use PrettyPrint to print the input
public class Main2 {
   public static void main(String [] args) {
      try {
         Program root = new MiniJavaParser(System.in).Goal();
          root.accept(new PrettyPrintVisitor());
      }
      catch (ParseException e) {
         System.out.println(e.toString());
      }
   }
}
