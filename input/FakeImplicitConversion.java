class A {
    public static void main(String[] a) {{}}
}

class x {
    int i;
    boolean b;
    y y;
    z z;
    public int main() {
        b = i;
        i = b;
        y = z;
        z = y;
        return i;
    }
}

class y extends z {}
class z{}
