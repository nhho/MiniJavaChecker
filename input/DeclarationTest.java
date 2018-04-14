class main {
    public static void main(String[] a) {
        {
            System.out.println(new A().main());
            System.out.println(new nth().main()); // error
        }
    }
}

class AAA extends nth { // error
}

class AA extends AAA {
    boolean b;
    public int b() {
        b = true;
        return 0;
    }
}

class A extends AA {
    int b;
    public int main() {
        b = 1;
        return 0;
    }
}

class A extends nth { // error * 2
    int b; // no error
    public int b() {
        b = 0;
        return 0;
    }
}

class B extends A {
    int b;
    boolean b; // error
    double c;
    int [] d;
    A e;
    public int b(int b, boolean b, double c, int [] d, A e) { // error
        int b1;
        boolean b1; // error
        double c1;
        int [] d1;
        A e1;
        c1 = c;
        x = c;
        d[1] = d[0];
        x[1] = x[0]; // error
        return b; // no error
    }
}

class BB extends A {
    int b;
    boolean b; // error
    double c;
    int [] d;
    A e;
    public int bb(int b, boolean b, double c, int [] d, A e) { // error
        int b1;
        boolean b1; // error
        double c1;
        int [] d1;
        A e1;
        c1 = c;
        x = c;
        d[1] = d[0];
        x[1] = x[0]; // error
        return b; // no error
    }
}
