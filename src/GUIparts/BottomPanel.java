package GUIparts;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class BottomPanel extends JPanel {
    private static JLabel fileName = new JLabel();
    private static JLabel durationInfo = new JLabel("Czas trwania algorytmu:");
    private static JLabel durationTime = new JLabel();
    private static JProgressBar progressBar = new JProgressBar();
    private Font BOTTOM_FONT = new Font("SansSerif", Font.PLAIN, 12);
    private Font BOTTOM_FONT2 = new Font("Monospaced",  Font.PLAIN, 13);

    public BottomPanel(){
        setLayout(new GridBagLayout());
        //Border border = BorderFactory.createLineBorder(Color.GRAY, 1);
        Border border = BorderFactory.createEtchedBorder();
        setBorder(border);
        var constraints = new GridBagConstraints();
        constraints.weightx = 100;
        constraints.weighty = 100;

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.insets.set(2, 5,2, 2);
        var fileNameInfo = new JLabel("Nazwa pliku:");
        fileNameInfo.setFont(BOTTOM_FONT);

        add(fileNameInfo, constraints);
        constraints.gridx = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets.set(2, 5,2, 10);
        add(fileName, constraints);
        fileName.setFont(BOTTOM_FONT2);

        constraints.gridx = 2;
        constraints.insets.set(2, 10,2, 5);
        constraints.anchor = GridBagConstraints.EAST;
        add(durationInfo, constraints);
        durationInfo.setFont(BOTTOM_FONT);
        constraints.gridx = 3;
        constraints.insets.set(2, 5,2, 10);
        constraints.anchor = GridBagConstraints.WEST;
        add(durationTime, constraints);

        durationTime.setFont(BOTTOM_FONT2);

        constraints.gridx = 5;
        constraints.gridwidth = 6;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        //constraints.anchor = GridBagConstraints.EAST;
        constraints.insets.set(2, 10,2, 5);
        add(progressBar, constraints);
        progressBar.setMinimum(0);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
    }

    public static void setFileName(String name){
        fileName.setText(name);
    }
    public static void setDurationTime(float time){
        durationTime.setText(time + " sec");
    }
    public static void setProgress(int value) {
        progressBar.setValue(value);
        progressBar.repaint();
    }
    public static void setProgressMaximum(int maxValue){
        progressBar.setMaximum(maxValue);
    }
    public static void incrementProgress(){
        progressBar.setValue(progressBar.getValue()+1);
    }
    public static void setProgressBarVisible(boolean flag){
        progressBar.setVisible(flag);
    }
    public static void setDurationInfoVisible(boolean flag){
        durationInfo.setVisible(flag);
    }
    public static void clear(){
        fileName.setText("");
        durationTime.setText("");
        setProgress(0);
    }
}
