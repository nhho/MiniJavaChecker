class main {
    public static void main(String[] a) {
        {}
    }
}

class A {
    public int mai() {
        int mai;
        mai = 1;
        mai = mai + mai;
        return mai;
    }
}

class foo {
    public int foo() {
        foo foo;
        foo bar;
        int tmp;
        tmp = this.foo();
        foo = new foo();
        bar = foo;
        foo = bar;
        return 0;
    }
}

class fooA {
    int tmp;
    public int foo() {
        int tmp;
        return 0;
    }
}

class fooB {
    int tmp;
    public int foo(int tmp) {
        return 0;
    }
}
