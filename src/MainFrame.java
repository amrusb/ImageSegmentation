import GUIparts.BottomPanel;
import ImageOperations.ImageRescaler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainFrame extends JFrame {
    private static final double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private static final Font HEADER_FONT = new Font("Monospaced", Font.BOLD, 16);
    private static final Font HEADER_2_FONT = new Font("SansSerif", Font.BOLD, 15);
    private static final Font BASIC_FONT = new Font("SansSerif", Font.PLAIN, 14);
    private static final JLabel imageLabel = new JLabel();
    public MainFrame(){
        setTitle("Segmentacja obrazu");
        int width = (int)(SCREEN_WIDTH * 3 / 4);
        int height = (int)(SCREEN_HEIGHT * 3 / 4);
        int x = (int)(SCREEN_WIDTH - width) / 2;
        int y = (int)(SCREEN_HEIGHT - height) / 2;

        setBounds(x, y, width, height);
        setLayout(new BorderLayout());
        var mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        var constr = new GridBagConstraints();
        constr.anchor = GridBagConstraints.CENTER;
        constr.gridx = 0;
        constr.gridy = 0;
        mainPanel.add(imageLabel, constr);
        add(mainPanel, BorderLayout.CENTER);
        imageLabel.setFont(HEADER_FONT);
        imageLabel.setText("Otwórz plik   CTRL + O");
        setJMenuBar(new MainMenuBar(this));

        add(new BottomPanel(), BorderLayout.SOUTH);
    }

    public static void setImageLabel(BufferedImage image) {
        imageLabel.setText("");
        int frameWidth = MainFrame.getFrameWidth();
        int frameHeight = MainFrame.getFrameHeight();

        int width = image.getWidth();
        int height = image.getHeight();

        if (width >= frameWidth || height >= frameHeight) {
            double scale = Math.min((double) frameWidth / (width), (double) frameHeight / (height));
            BufferedImage displayImage = ImageRescaler.rescaleImage(image, scale);
            ImageIcon imageIcon = new ImageIcon(displayImage);
            imageLabel.setIcon(imageIcon);
            Main.setRescaledImage(displayImage);
        }
        else{
            ImageIcon imageIcon = new ImageIcon(image);
            imageLabel.setIcon(imageIcon);
        }
    }

    public static int getFrameWidth(){
        return (int)(SCREEN_WIDTH * 3 / 4);
    }
    public static int getFrameHeight(){
        return (int)(SCREEN_HEIGHT * 3 / 4);
    }

    public static Font getHeaderFont(){return HEADER_FONT; }

    public static Font getHeader2Font() {return HEADER_2_FONT;}

    public static Font getBasicFont() {return BASIC_FONT;}

}