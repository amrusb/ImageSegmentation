import UserInterface.MainFrame;

import java.awt.*;
import javax.swing.*;

public class Main {
    private static final MainFrame frame = new MainFrame();
    public static void main(String[] args){
        EventQueue.invokeLater(()->{
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }

}
