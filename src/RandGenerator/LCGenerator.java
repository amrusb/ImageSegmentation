package RandGenerator;

import java.time.LocalDateTime;

/*
* Klasa reprezentuje Linear Congruential Generator
* generator liczb losowych o rozkładzie jednorodnym*/
public class LCGenerator {
    private static int a = 1664525;
    private static double m = Math.pow(2.0, 32.0);
    private static int c = 1013904223;
    private double value;

    public LCGenerator(long seed){
        //LocalDateTime.now().getNano() + (LocalDateTime.now().getSecond() * LocalDateTime.now().getMinute())
        if(seed % 2 == 0) seed+=1;
        this.value = seed;
    }

    public double nextDouble(){
        double next = (a * value + c) % m;
        value = next;
        return value / m;
    }

}
