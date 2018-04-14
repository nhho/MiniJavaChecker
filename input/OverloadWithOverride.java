class A {
    public static void main(String[] a) {{}}
}

class X extends Y {
  public int foo(){
    x = this.y();  // The x and y should be resolved to the x in class Y and y() in class X.
    return x;
  }

  public int y(){ // Overloading and Overriding
    return 0;
  }

  public int y(int x, int y){
    return 0;
  }
}

class Y {
  int x;
  public int y() {
    return 0;
  }
}
