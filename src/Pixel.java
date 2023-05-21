public class Pixel extends Point{
    private int A;
    private int assignment = -1;
    public Pixel(int a, int r, int g, int b) {
        super(r,g,b);
        A = a;

    }
    public Pixel(int binary) {
        super();
        A = (binary >> 24) & 0xff;
        int r = (binary >> 16) & 0xff;
        int g = (binary >> 8) & 0xff;
        int b = binary & 0xff;
        super.setX(r);
        super.setY(g);
        super.setZ(b);
    }

    public int getBinaryPixel() {
        int p = A;
        p = (p << 8) | super.getX();
        p = (p << 8) | super.getY();
        p = (p << 8) | super.getZ();
        return p;
    }
    public void setPixelValue(int r, int g, int b) {
        super.setX(r);
        super.setY(g);
        super.setZ(b);
    }

    public void setAssignment(int clusterNumber) {this.assignment = clusterNumber;}
    public int getAssignment() {return assignment;}

    public int getR() { return super.getX(); }

    public int getG() { return super.getY(); }

    public int getB() { return super.getZ(); }


    public String toString() {
        return A + "/" + super.toString();
    }
}
