import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private static final Color MAIN_COLOR = new Color(57, 54, 70);
    private static final Color SECONDARY_COLOR = new Color(70, 69, 87);
    private static final Color TERTIARY_COLOR = new Color(109, 93, 110);
    private static final Color TEXT_COLOR = new Color(244, 238, 224);
    private static final MainMenuBar menuBar = new MainMenuBar();
    public MainFrame(){
        setTitle("Segmentacja obrazu");
        int width = (int)(SCREEN_WIDTH * 3 / 4);
        int height = (int)(SCREEN_HEIGHT * 3 / 4);
        int x = (int)(SCREEN_WIDTH - width) / 2;
        int y = (int)(SCREEN_HEIGHT - height) / 2;

        setBounds(x, y, width, height);

        setJMenuBar(menuBar);
    }

    public static Color getMainColor() { return MAIN_COLOR;}
    public static Color getSecondaryColor() { return SECONDARY_COLOR; }
    public static Color getTertiaryColor() { return TERTIARY_COLOR; }
    public static Color getTextColor() { return TEXT_COLOR; }
}
