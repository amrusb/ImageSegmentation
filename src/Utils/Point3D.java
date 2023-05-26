package Utils;

public class Point3D {
    private int X;
    private int Y;
    private int Z;

    public Point3D(){
        X = 0;
        Y = 0;
        Z = 0;
    }
    public Point3D(int x, int y, int z){
        X = x;
        Y = y;
        Z = z;
    }
    public int getX() {
        return X;
    }

    public void setX(int x) {X = x;}

    public int getY() {
        return Y;
    }

    public void setY(int y) {Y = y;}

    public int getZ() {
        return Z;
    }

    public void setZ(int z) {Z = z;}

    @Override
    public String toString() {
        return X+"/"+Y+"/"+Z;
    }
}
