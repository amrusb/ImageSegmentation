package Utils;

public class Cluster extends Point3D {
    private int size = 0;

    public Cluster(Pixel pixel){
        super(pixel.getR(),pixel.getG(),pixel.getB());
    }
    public Cluster(int x, int y, int z){
        super(x,y,z);
    }
    public void increaseSize(){
        size++;
    }
    public void decreaseSize(){
        size--;
    }
    public int getSize(){
        return size;
    }

    public int getX() {return super.getX();}
    public void setX(int x) {super.setX(x);}
    public int getY() {return super.getY();}
    public void setY(int y) {super.setY(y);}
    public int getZ() {return super.getZ();}
    public void setZ(int z) {super.setZ(z);}

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(obj == null) return false;
        if(getClass() != obj.getClass()) return false;
        Cluster c = (Cluster)obj;

        return super.getX() == c.getX() &&
                super.getY() == c.getY() &&
                super.getZ() == c.getZ();

    }

    @Override
    public String toString() {
        return super.toString();
    }
}
