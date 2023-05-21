package RandGenerator;

import java.time.LocalDateTime;
import java.util.Random;

/*
* Klasa reprezentuje transormacje Box-Muller,
* przekształcajaca rozkład jednorodny, w rozkład normalny*/
public class GaussianRandomGenerator {
    private static final LCGenerator random = new LCGenerator(LocalDateTime.now().getNano() - (LocalDateTime.now().getSecond() * LocalDateTime.now().getMinute()));
    public static double nextDouble(){
        double u1 =  random.nextDouble();
        double u2 = random.nextDouble();
        double r = (-2) * Math.log(u1);
        r = Math.sqrt(r);
        double theta = 2 * Math.PI * u2;

        double X = r * Math.cos(theta);
        double Y = r * Math.sin(theta);

        return X;
    }
}
