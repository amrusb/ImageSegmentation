package Dialogs;

import javax.swing.*;
import java.awt.*;

public class ClusterInputDialog extends JDialog {
    //private JTextField clusterCountField = new JTextField(3);;
    private final JComboBox<Integer> clusterCountBox = new JComboBox<>();
    private final JCheckBox imageSource = new JCheckBox("Segmentuj oryginalne wymiary");
    private final JButton submitButton = new JButton("Zatwierdź");

    private int clusterCount;

    public ClusterInputDialog(JFrame parent, boolean hasRescaledImage) {
        super(parent, "K-Means Clustering", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(300, 200);
        setResizable(false);
        setLocationRelativeTo(parent);

        setLayout(new GridBagLayout());
        var constr = new GridBagConstraints();
        constr.weightx = 100;
        constr.weighty = 100;

        constr.gridx = 0;
        constr.gridy = 0;
        constr.gridwidth = 2;
        constr.gridheight = 1;
        constr.insets.set(10,20,5, 20);

        JLabel label = new JLabel("Podaj liczbę klastrów:");
        add(label, constr);

        clusterCountBox.setEditable(true);
        for (int i = 2; i < 11; i++) {
            clusterCountBox.addItem(i);
        }
        constr.gridy = 1;
        constr.insets.set(5,40,5, 40);
        add(clusterCountBox, constr);

        constr.gridy = 2;
        constr.insets.set(5,20,5, 20);
        imageSource.setSelected(true);
        imageSource.setEnabled(hasRescaledImage);
        add(imageSource, constr);

        var buttonPanel = new JPanel();
        submitButton.addActionListener(e -> {
            if (e.getSource() == submitButton) {

                clusterCount =  (int)clusterCountBox.getSelectedItem();
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
        constr.gridy = 3;
        constr.insets.set(5,20,10, 20);
        add(buttonPanel, constr);
    }

    public int getClusterCount() {
        return clusterCount;
    }

    public boolean checkImageSource() {return imageSource.isSelected(); }

}
