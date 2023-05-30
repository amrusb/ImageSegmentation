package Utils;

import java.awt.geom.Point2D;

public class Point {
    private int X;
    private int Y;

    public Point(){
        X = 0;
        Y = 0;
    }
    public Point(int x, int y){
        X = x;
        Y = y;
    }

    public Point(double x, double y){
        X = (int) x;
        Y = (int) y;
    }
    public Point(Point2D p){
        X = (int)p.getX();
        Y = (int)p.getY();
    }
    public int getX() {
        return X;
    }

    public void setX(int x) {X = x;}

    public int getY() {
        return Y;
    }

    public void setY(int y) {Y = y;}

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return  true;
        if(obj.getClass() != this.getClass()) return false;
        var temp = (Point) obj;
        return this.X == temp.X && this.Y == temp.getY();
    }

    @Override
    public String toString() {
        return X+"/"+Y;
    }
}
