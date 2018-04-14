class A {
    public static void main(String[] a) {{}}
}

class B {
    public int x() { return 0; }
    public child y() { return new child(); }
}

class C extends B {
    public double x() { return 0; } // error
    public par y() { return new par(); } // error
}

class par {}
class child extends par {}
