class OverloadTest {
    public static void main(String[] a) {{}}
}

class C {
  public int f(int x, int y) { return 0; } // #5
}
class B extends C {
  public int g(int x) { return 0; } // #4
  public int h(int x) { return 0;} // #7
}

class A extends B {
  public int f(int x) { return 0; }  // #1
  public int f(double x) { return 0; } // #2
  public int g(double x) { return 0; } // #3
  public int h(int x) { return 0; } // #6  (Error: Have different return type against the overridden method in B)

  public int bar() {
    double b;
    b=this.f(1); // Resolve to method #1
    b=this.f(1+b); // Resolve to method #2
    b=this.g(1); // Resolve to method #4
    b=this.g(1+b); // Resolve to method #3
    b=this.f(1,1); // Resolve to method #5
    return 0;
  }
}