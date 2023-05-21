package RandGenerator;

import java.time.LocalDateTime;

/*
* Klasa reprezentuje Linear Congruential Generator
* generator liczb losowych o rozkładzie jednorodnym*/
public class LCGenerator {
    private static int a = LocalDateTime.now().getNano() + (LocalDateTime.now().getSecond() * LocalDateTime.now().getMinute());
    private static double m = Math.pow(2.0, 32.0);
    private static int c = LocalDateTime.now().getNano();;
    private double value;

    public LCGenerator(long seed){
        //c = LocalDateTime.now().getNano();
        //a = LocalDateTime.now().getNano() + (LocalDateTime.now().getSecond() * LocalDateTime.now().getMinute());
        this.value = seed;
    }

    public double nextDouble(){
        double next = (a * value + c) % m;
        value = next;
        return value / m;
    }

}
