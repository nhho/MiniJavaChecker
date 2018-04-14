class main {
    public static void main(String[] a) {
        // expected: 101 202 301 302 401 501 502 601 602 603 604 605 606 702 704 706 801 802 802 803 806 809 811 901 0
        System.out.println(new RealMain().main());
    }
}

class RealMain {
    public int main() {
        int tmp;
        tmp = new A2().main();
        tmp = new B2().main();
        tmp = new C().main();
        tmp = new D2().main();
        tmp = new E().main();
        tmp = new F().main();
        tmp = new G().main();
        tmp = new H4().main();
        tmp = new I4().main();
        return 0;
    }
}

// standard extends

class A1 {
    public int aa() {
        System.out.println(101);
        return 0;
    }
}

class A2 extends A1 {
    public int main() {
        int tmp;
        tmp = this.aa(); // 101
        return 0;
    }
}

// standard override

class B1 {
    public int bb() {
        System.out.println(201);
        return 0;
    }
}

class B2 extends A1 {
    public int bb() {
        System.out.println(202);
        return 0;
    }

    public int main() {
        int tmp;
        tmp = this.bb(); // 202
        return 0;
    }
}

// standard overload

class C {
    public int cc(int a) {
        System.out.println(301);
        return 0;
    }

    public int cc(boolean a) {
        System.out.println(302);
        return 0;
    }

    public int main() {
        int tmp;
        tmp = this.cc(0); // 301
        tmp = this.cc(true); // 302
        return 0;
    }
}

// overload with extends

class D1 {
    public int dd(int a) {
        System.out.println(401);
        return 0;
    }
}

class D2 extends D1 {
    public int dd(double a) {
        System.out.println(402);
        return 0;
    }

    public int main() {
        int tmp;
        tmp = this.dd(0); // 401
        return 0;
    }
}

// overload with custom class

class E1 {
}

class E2 {
}

class E {
    public int ee(E1 a) {
        System.out.println(501);
        return 0;
    }

    public int ee(E2 a) {
        System.out.println(502);
        return 0;
    }

    public int main() {
        int tmp;
        tmp = this.ee(new E1()); // 501
        tmp = this.ee(new E2()); // 502
        return 0;
    }
}

// overload with more than one arguments

class F {
    public int ff(int a) {
        System.out.println(601);
        return 0;
    }

    public int ff(boolean a) {
        System.out.println(602);
        return 0;
    }

    public int ff(int a, int b) {
        System.out.println(603);
        return 0;
    }

    public int ff(int a, boolean b) {
        System.out.println(604);
        return 0;
    }

    public int ff(boolean a, int b) {
        System.out.println(605);
        return 0;
    }

    public int ff(boolean a, boolean b) {
        System.out.println(606);
        return 0;
    }

    public int main() {
        int tmp;
        tmp = this.ff(0); // 601
        tmp = this.ff(true); // 602
        tmp = this.ff(0, 0); // 603
        tmp = this.ff(0, true); // 604
        tmp = this.ff(true, 0); // 605
        tmp = this.ff(true, true); // 606
        return 0;
    }
}

// overload with implicit conversion

class G {
    public int gg(boolean a) {
        System.out.println(701);
        return 0;
    }

    public int gg(double a) {
        System.out.println(702);
        return 0;
    }

    public int gg(int a, double b) {
        System.out.println(703);
        return 0;
    }

    public int gg(int a, int b) {
        System.out.println(704);
        return 0;
    }

    public int gg(double a, int b) {
        System.out.println(705);
        return 0;
    }

    public int gg(int a, double b, int c) {
        System.out.println(706);
        return 0;
    }

    public int gg(int a, double b, double c) {
        System.out.println(707);
        return 0;
    }

    public int main() {
        int tmp;
        tmp = this.gg(0); // 702
        tmp = this.gg(0, 0); // 704
        tmp = this.gg(0, 0, 0); // 706
        return 0;
    }
}

// overload with implicit conversion on class

class H1 {
}

class H2 extends H1 {
}

class H3 extends H2 {
}

class H4 {
    public int hh(H1 a) {
        System.out.println(801);
        return 0;
    }

    public int hh(H2 a) {
        System.out.println(802);
        return 0;
    }

    public int hh(int a, H1 b) {
        System.out.println(803);
        return 0;
    }

    public int hh(double a, H1 b) {
        System.out.println(804);
        return 0;
    }

    public int hh(H1 a, H1 b) {
        System.out.println(805);
        return 0;
    }

    public int hh(H2 a, H2 b) {
        System.out.println(806);
        return 0;
    }

    public int hh(H1 a, H2 b) {
        System.out.println(807);
        return 0;
    }

    public int hh(int a, H1 b, H1 c) {
        System.out.println(808);
        return 0;
    }

    public int hh(int a, H1 b, H2 c) {
        System.out.println(809);
        return 0;
    }

    public int hh(int a, double b, H1 d) {
        System.out.println(810);
        return 0;
    }

    public int hh(int a, double c, H2 d) {
        System.out.println(811);
        return 0;
    }

    public int main() {
        int tmp;
        tmp = this.hh(new H1()); // 801
        tmp = this.hh(new H2()); // 802
        tmp = this.hh(new H3()); // 802
        tmp = this.hh(0, new H2()); // 803
        tmp = this.hh(new H3(), new H3()); // 806
        tmp = this.hh(0, new H3(), new H3()); // 809
        tmp = this.hh(0, 0, new H3()); // 811
        return 0;
    }
}

// override with implicit conversion on class in return type

class I1 {
}

class I2 extends I1 {
}

class I3 {
    public I1 ii(int a) {
        return new I1();
    }
}

class I4 extends I3 {
    public I2 ii(int a) {
        System.out.println(901);
        return new I2();
    }
    
    public int main() {
        I1 tmp;
        tmp = this.ii(0); // 901
        return 0;
    }
}
