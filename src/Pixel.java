public class Pixel {
    private int A;
    private int R;
    private int G;
    private int B;
    public Pixel(int a, int r, int g, int b) {
        A = a;
        R = r;
        G = g;
        B = b;
    }
    public Pixel(int binary) {
        A = (binary >> 24) & 0xff;
        R = (binary >> 16) & 0xff;
        G = (binary >> 8) & 0xff;
        B = binary & 0xff;
    }

    public int getBinaryPixel() {
        int p = A;
        p = (p << 8) | R;
        p = (p << 8) | G;
        p = (p << 8) | B;
        return p;
    }
    public void setPixelValue(int a, int r, int g, int b) {
        A = a;
        R = r;
        G = g;
        B = b;
    }
    public void setPixelValue(int binary) {
        A = (binary >> 24) & 0xff;
        R = (binary >> 16) & 0xff;
        G = (binary >> 8) & 0xff;
        B = binary & 0xff;
    }

    public String toString() {
        return A + "/" + R + "/" + G + "/" + B;
    }
}
