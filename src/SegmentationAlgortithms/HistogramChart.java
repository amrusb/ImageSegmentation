package SegmentationAlgortithms;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;

import static org.jfree.chart.ChartUtils.saveChartAsPNG;

public class HistogramChart {

    public static void createHistogram(int[] histogram) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (int i = 0; i < histogram.length; i++) {
            dataset.addValue(histogram[i], "Frequency", String.valueOf(i));
        }

        JFreeChart chart = ChartFactory.createBarChart(
                "Histogram",
                "Value",
                "Frequency",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        int width = 800; // szerokość obrazu diagramu
        int height = 600; // wysokość obrazu diagramu
        File chartFile = new File("histogram.png");

        try {
            saveChartAsPNG(chartFile, chart, width, height);
            System.out.println("Diagram został zapisany jako histogram.png");
        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas zapisu diagramu: " + e.getMessage());
        }
    }
}
