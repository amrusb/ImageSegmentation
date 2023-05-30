package GUIparts;

import javax.swing.*;
import java.awt.*;

public class ProgressDialog extends JDialog {
    private JProgressBar progressBar;
    JPanel panel = new JPanel(new BorderLayout(10, 10));
    public ProgressDialog(Frame parent, String title) {
        super(parent, title, ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        progressBar = new JProgressBar();
        progressBar.setMaximum(0);
        progressBar.setMaximum(100);
        progressBar.setStringPainted(true);
        progressBar.setIndeterminate(true);

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(progressBar, BorderLayout.CENTER);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(parent);
    }

    public void setProgress(int value) {
        progressBar.setValue(value);
    }
}