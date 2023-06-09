package UserInterface;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

public class ClusterInputDialog extends JDialog {
    private final JFormattedTextField clusterCountField;
    private final JSlider slider = new JSlider(2, 30, 6);
    private final JCheckBox imageSource = new JCheckBox("Segmentuj obraz w oryginalnych wymiarach");
    private final JButton submitButton = new JButton("Zatwierdź");

    private int clusterCount;

    public ClusterInputDialog(JFrame parent, boolean hasRescaledImage) {
        super(parent, "Algorytm k-means", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(420, 320);
        setResizable(false);
        setLocationRelativeTo(parent);

        setLayout(new GridBagLayout());
        var constr = new GridBagConstraints();
        constr.weightx = 100;
        constr.weighty = 100;

        constr.gridx = 0;
        constr.gridy = 0;
        constr.gridwidth = 1;
        constr.gridheight = 1;
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.insets.set(10,20,5, 20);

        JLabel label = new JLabel("Podaj liczbę klastrów:");
        add(label, constr);

        constr.gridy = 1;
        constr.fill = GridBagConstraints.BOTH;
        constr.insets.set(10,180,5, 180);
        NumberFormat intFormat = NumberFormat.getIntegerInstance();

        NumberFormatter numberFormatter = new NumberFormatter(intFormat);

        numberFormatter.setAllowsInvalid(false);
        clusterCountField =  new JFormattedTextField(numberFormatter);
        clusterCountField.setText("6");
        numberFormatter.setMinimum(0L);
        clusterCountField.setEditable(true);
        add(clusterCountField, constr);

        constr.gridy = 2;
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.insets.set(5,10,5, 10);

        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.setMajorTickSpacing(4);
        slider.setMinorTickSpacing(1);
        slider.setSnapToTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(e->{
            JSlider source = (JSlider) e.getSource();
            clusterCountField.setText("" + source.getValue());
        });
        add(slider, constr);

        constr.gridy = 3;
        constr.gridheight = 1;
        constr.gridwidth = 1;
        constr.insets.set(5,20,5, 20);
        constr.fill = GridBagConstraints.NONE;
        imageSource.setSelected(true);
        imageSource.setEnabled(hasRescaledImage);
        add(imageSource, constr);

        var buttonPanel = new JPanel();
        submitButton.addActionListener(e -> {
            if (e.getSource() == submitButton) {
                clusterCount =  Integer.parseInt(clusterCountField.getText());
                dispose();
            }
        });
        buttonPanel.add(submitButton);
        JButton cancelButton = new JButton("Anuluj");
        buttonPanel.add(cancelButton);
        cancelButton.addActionListener(e -> {
            clusterCount = -1;
            dispose();
        });
        constr.gridy = 4;
        constr.insets.set(5,20,10, 20);
        add(buttonPanel, constr);
    }

    public int getClusterCount() {
        return clusterCount;
    }

    public boolean checkImageSource() {return imageSource.isSelected(); }

}
