package UserInterface;

import Utils.Point;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;

public class RegionChooseDialog extends JDialog {
    private ArrayList<Point> regions = new ArrayList<>();
    private final int DIALOG_WIDTH;
    private final int DIALOG_HEIGHT;
    private double scale;
    private final JFormattedTextField alphaField;
    private final JSlider slider = new JSlider(2, 9, 4);
    public RegionChooseDialog(JFrame owner, BufferedImage image, double scale){
        super(owner, "Algorytm region growing", true);
        this.scale = scale;
        setResizable(false);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if(image.getWidth() >= image.getHeight() ) {
            DIALOG_WIDTH = image.getWidth();
            DIALOG_HEIGHT = image.getHeight() + 170;
        }
        else{
            DIALOG_WIDTH = image.getWidth() * 2 + 15;
            DIALOG_HEIGHT = image.getHeight() + 15;
        }

        double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        int x = (int)(SCREEN_WIDTH - DIALOG_WIDTH) / 2;
        int y = (int)(SCREEN_HEIGHT - DIALOG_HEIGHT) / 2;
        setBounds(x, y, DIALOG_WIDTH, DIALOG_HEIGHT);


        var choseAlphaPanel = new JPanel();
        choseAlphaPanel.setLayout(new GridBagLayout());
        var constr = new GridBagConstraints();
        constr.weightx = 100;
        constr.weighty = 100;

        constr.gridx = 0;
        constr.gridy = 0;
        constr.gridwidth = 1;
        constr.gridheight = 1;
        constr.fill = GridBagConstraints.VERTICAL;
        constr.anchor = GridBagConstraints.EAST;
        constr.insets.set(15, 0, 0, 10);
        var alphaLabel = new JLabel("Wartość parametru α:");
        alphaLabel.setFont(MainFrame.getBasicBoldFont());
        choseAlphaPanel.add(alphaLabel, constr);
        NumberFormat intFormat = NumberFormat.getIntegerInstance();
        NumberFormatter numberFormatter = new NumberFormatter(intFormat);
        numberFormatter.setAllowsInvalid(false);
        alphaField =  new JFormattedTextField(numberFormatter);
        alphaField.setText("4");
        alphaField.setFont(MainFrame.getBasicFont());
        numberFormatter.setMinimum(0L);
        alphaField.setEditable(false);
        alphaField.setBorder(BorderFactory.createEmptyBorder());
        constr.insets.set(15, 10, 0, 0);
        constr.fill = GridBagConstraints.HORIZONTAL;
        constr.gridx = 1;
        choseAlphaPanel.add(alphaField, constr);

        constr.gridx = 0;
        constr.gridwidth = 2;
        constr.gridy = 1;
        constr.insets.set(5, 5, 0, 5);
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.setMajorTickSpacing(2);
        slider.setMinorTickSpacing(1);
        slider.setSnapToTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(e->{
            JSlider source = (JSlider) e.getSource();
            alphaField.setText("" + source.getValue());
        });

        choseAlphaPanel.add(slider, constr);
        var buttonPanel = new JPanel();
        var okButton = new JButton("Zatwierdź");
        var endButton = new JButton("Zakończ");

        buttonPanel.add(okButton);
        okButton.addActionListener(e-> dispose());
        buttonPanel.add(endButton);
        endButton.addActionListener(e->{
            regions = new ArrayList<>();
            dispose();
        });
        constr.gridy = 2;
        constr.insets.set(5, 5, 10, 10);

        choseAlphaPanel.add(buttonPanel, constr);

        if(image.getWidth() >= image.getHeight()){
            var layout = new BorderLayout();
            setLayout(layout);
            ImagePanel imagePanel = new ImagePanel(image);
            add(imagePanel, BorderLayout.NORTH);

            add(choseAlphaPanel, BorderLayout.SOUTH);
        }else{
            var layout = new GridLayout(1, 2);
            setLayout(layout);
            ImagePanel imagePanel = new ImagePanel(image);
            add(imagePanel);
            var sidePanel = new JPanel();
            sidePanel.setLayout(new BorderLayout());
            sidePanel.add(choseAlphaPanel, BorderLayout.SOUTH);
            add(sidePanel);
        }
    }
    public int getAlpha(){return Integer.parseInt(alphaField.getText()); }
    public ArrayList<Point> getRegions(){ return regions; }
    public class ImagePanel extends JPanel {
        private final BufferedImage image;
        private final ArrayList<Ellipse2D> circles = new ArrayList<>();
        private Ellipse2D current_circle = null;

        public ImagePanel(BufferedImage image) {
            setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
            this.image = image;
            addMouseListener(new MouseHandler());
            addMouseMotionListener(new MouseMotionHandler());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            var g2 = (Graphics2D)g;

            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);

            for (Ellipse2D circle: circles) {
                g2.setPaint(new Color(255, 255, 255, 80));
                g2.fill(circle);
                g2.setPaint(Color.BLACK);
                g2.draw(circle);
            }

        }

        private void addPoint(Point2D p){
            int X = (int)(p.getX() * scale);
            int Y = (int)(p.getY() * scale);
            regions.add(new Point(X, Y));

            double x = p.getX();
            double y = p.getY();

            circles.add(new Ellipse2D.Double(x - 5, y - 5, 10, 10));
            repaint();
        }
        private void removePoint(Ellipse2D p){
            int x = (int)((p.getX() + 5)* scale);
            int y = (int)((p.getY() + 5) * scale);
            regions.removeIf(point -> point.getX() == x && point.getY() == y);
            circles.remove(p);
            repaint();
        }
        private Ellipse2D findPoint(Point2D p){
            for (Ellipse2D circle: circles) {
                if(circle.contains(p)) return circle;
            }
            return null;
        }
        private class MouseHandler extends MouseAdapter {
            @Override
            public void mousePressed(MouseEvent e) {
                if(findPoint(e.getPoint()) == null)
                    addPoint(e.getPoint());

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                current_circle = findPoint(e.getPoint());
                if(current_circle != null && e.getClickCount() >= 2){
                    removePoint(current_circle);
                }
            }
        }

        private class MouseMotionHandler implements MouseMotionListener {
            @Override
            public void mouseMoved(MouseEvent e) {
                if(findPoint(e.getPoint()) == null)
                    setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                else
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseDragged(MouseEvent e) {
            }
        }
    }
}
