class A {
    public static void main(String[] a) {{}}
}

class Foo {
    public int Bar() {
        Foo = 1; // unknwon identifier instead of type mismatch
        return 0;
    }
}
