import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class MainMenuBar extends JMenuBar {
    private static final JMenu programMenu = new JMenu("Program");
    private static final JMenuItem infoItem = new JMenuItem("Informacje");
    private static final JMenuItem exitItem = new JMenuItem("Wyjście");
    private static final JMenu imageMenu = new JMenu("Plik");
    private static final JMenuItem openItem = new JMenuItem("Otwórz");
    private static final JMenuItem saveItem = new JMenuItem("Zapisz");
    private static final JMenuItem saveAsItem = new JMenuItem("Zapisz jako");
    private static JFileChooser imageChooser = null;
    public MainMenuBar(){
        programMenu.add(infoItem);
        programMenu.add(exitItem);
        add(programMenu);

        imageMenu.add(openItem);
        openItem.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        openItem.addActionListener(e->{
            imageChooser = new JFileChooser();
            imageChooser.setCurrentDirectory(new File("."));
            imageChooser.setFileFilter(new FileNameExtensionFilter("Pliki obrazów", "jpg", "png"));
            imageChooser.showOpenDialog(null);
            String filePath = imageChooser.getSelectedFile().getAbsolutePath();
            ImageReader.setFilePath(filePath);
            System.out.println(filePath);
        });
        imageMenu.add(saveItem);
        saveItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        imageMenu.add(saveAsItem);
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke("ctrl shift S"));
        add(imageMenu);
    }

}
