class main {
    public static void main(String[] a) {
        {}
    }
}

class A {
    public int a(int a) {
        int a; // error
        return this.a();
    }
}
