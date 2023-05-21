import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MainFrame extends JFrame {
    private static final double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 20);
    private static final Font HEADER_2_FONT = new Font("SansSerif", Font.BOLD, 13);
    private static final Font BASIC_FONT = new Font("SansSerif", Font.BOLD, 14);
    private static final JLabel imageLabel = new JLabel();
    public MainFrame(){
        setTitle("Segmentacja obrazu");
        int width = (int)(SCREEN_WIDTH * 3 / 4);
        int height = (int)(SCREEN_HEIGHT * 3 / 4);
        int x = (int)(SCREEN_WIDTH - width) / 2;
        int y = (int)(SCREEN_HEIGHT - height) / 2;

        setBounds(x, y, width, height);
        setLayout(new GridBagLayout());
        var constr = new GridBagConstraints();
        constr.anchor = GridBagConstraints.CENTER;
        constr.gridx = 0;
        constr.gridy = 0;
        add(imageLabel, constr);
        setJMenuBar(new MainMenuBar(this));
    }

    public static void setImageLabel(BufferedImage image){
        Image rescaledImage = rescaleImage(image);
        ImageIcon imageIcon = new ImageIcon(rescaledImage);

        imageLabel.setIcon(imageIcon);
    }
    private static Image rescaleImage(BufferedImage image){
        Image displayImage = image;

        int frameWidth = MainFrame.getFrameWidth();
        int frameHeight = MainFrame.getFrameHeight();

        int width = image.getWidth();
        int height = image.getHeight();

        if (width >= frameWidth || height >= frameHeight) {
            double scale = Math.min((double) frameWidth / (width), (double) frameHeight / (height));
            width = (int) (scale * width);
            height = (int) (scale * height);
            displayImage = image.getScaledInstance(width, height, Image.SCALE_FAST);
        }
        return  displayImage;
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
