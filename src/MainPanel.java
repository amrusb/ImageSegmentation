import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {

    public MainPanel(){
        setBackground(MainFrame.getMainColor());
        setLayout(new BorderLayout());
    }
}

class ImagePanel extends JPanel{
    public ImagePanel(){
        setBackground(MainFrame.getSecondaryColor());
        add(new ImageFrame());
    }
}

class ImageFrame extends JComponent{

    public void paintComponent(Graphics g){
        g.drawImage(ImageReader.getImage(), 10, 10, null);
    }
}

class InfoPanel extends JPanel{

}