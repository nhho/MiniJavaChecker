class main {
    public static void main(String[] a) {
        System.out.println(new B().main());
    }
}

class A {
    double b;
    double bb;

    public int c() {
        b = 1;
        return 0;
    }
}

class B extends A {
    int b;
    
    public int d() {
        b = 2;
        return 0;
    }
    
    public int main() {
        int tmp;
        tmp = this.d();
        tmp = this.c();
        bb = b;
        System.out.println(b); // 2
        return 0;
    }
}
