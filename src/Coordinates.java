class Coordinates{
    private int X;
    private int Y;
    public Coordinates(int X, int Y){
        this.X = X;
        this.Y = Y;
    }
    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    @Override
    public String toString() {
        return "(" + X +", " + Y + ")";
    }
}
