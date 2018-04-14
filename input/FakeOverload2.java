class A {
    public static void main(String[] a) {{}}
}

class B {
    public int z(int a, double b) { return 0; }
    public int z(double a, int b) { return 0; }
    public int z(int a, grandpar b) { return 0; }
    public int z(double a, child b) { return 0; }
    public int z(double a, par b) { return 0; }
    public int z(grandpar a, child b) { return 0; }
    public int z(child a, par b) { return 0; }
    public int z(grandpar a, child b, child c) { return 0; }
    public int z(child a, par b, par c) { return 0; }
    public int w() { return this.z(0, 0); } // error
    public int w() { return this.z(0, new child()); } // error
    public int w() { return this.z(new child(), new child()); } // error
    public int w() { return this.z(new child(), new child(), new child()); } // error
}

class grandpar {}
class par extends grandpar {}
class child extends par {}
