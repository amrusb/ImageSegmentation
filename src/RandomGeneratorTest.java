import RandomGenerator.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class RandomGeneratorTest {

    public static void main(String[] args) {
        int n = 100_000;
        int rand = 10;
        var lcgR = new LCGenerator(2314302);
        var ldgHas = new TreeMap<Double, Integer>();

        var random = new Random();
        var lrHas = new TreeMap<Double, Integer>();

        for (int i = 0; i < n; i++) {
            double val = lcgR.nextDouble();
            val =  Math.floor(val * rand) / rand;
            ldgHas.put(val, ldgHas.getOrDefault(val, 0) + 1);

            val = random.nextGaussian();
            val =  Math.floor(val * rand) / rand;

            val = random.nextDouble();;
            val = Math.floor(val * rand) / rand;
            lrHas.put(val, ldgHas.getOrDefault(val, 0) + 1);
        }

        DefaultCategoryDataset ldg_chart_dataset = new DefaultCategoryDataset();
        DefaultCategoryDataset lr_chart_dataset = new DefaultCategoryDataset();
        for (Map.Entry<Double, Integer> entry : ldgHas.entrySet()) {
            ldg_chart_dataset.addValue(  entry.getValue() , "data" , entry.getKey() );
        }

        for (Map.Entry<Double, Integer> entry : lrHas.entrySet()) {
            lr_chart_dataset.addValue(  entry.getValue() , "data" , entry.getKey() );
        }

        JFreeChart ldgChartObject = ChartFactory.createLineChart(
                "Linear Congruential Generator","",
                "Ilosc wygenerowanych liczb",
                ldg_chart_dataset,PlotOrientation.VERTICAL,
                true,true,false);

        JFreeChart lrChartObject = ChartFactory.createLineChart(
                "java.Random.nextDouble()","",
                "Ilosc wygenerowanych liczb",
                lr_chart_dataset,PlotOrientation.VERTICAL,
                true,true,false);

        int width = 800;
        int height = 800;

        String date = LocalDate.now().toString();
        File lcg = new File( "./rand_charts/lcg-" + date + ".jpeg" );
        File lrand = new File("./rand_charts/random-linear-" + date + ".jpeg");
        try{
            ChartUtils.saveChartAsJPEG(lcg ,ldgChartObject, width ,height);
            ChartUtils.saveChartAsJPEG(lrand ,lrChartObject, width ,height);
            System.out.println("\nDiagramy wygenerowano.");
        }
        catch (IOException e){

        }
    }


}


