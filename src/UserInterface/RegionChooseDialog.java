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
    private final JButton okButton = new JButton("Zatwierdź");
    private final JSlider slider = new JSlider(2, 9, 4);
    public RegionChooseDialog(JFrame owner, BufferedImage image, double scale){
        super(owner, "Algorytm region growing", true);
        this.scale = scale;
        setResizable(false);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if(image.getWidth() >= image.getHeight() ) {
            DIALOG_WIDTH = image.getWidth() + 15;
            DIALOG_HEIGHT = image.getHeight() + 100;
        }
        else{
            DIALOG_WIDTH = image.getWidth() + 225;
            DIALOG_HEIGHT = image.getHeight() + 40;
        }

        double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        int x = (int)(SCREEN_WIDTH - DIALOG_WIDTH) / 2;
        int y = (int)(SCREEN_HEIGHT - DIALOG_HEIGHT) / 2;
        setBounds(x, y, DIALOG_WIDTH, DIALOG_HEIGHT);


        var alphaLabel = new JLabel("Wartość parametru α:");
        alphaLabel.setFont(MainFrame.getBasicBoldFont());

        NumberFormat intFormat = NumberFormat.getIntegerInstance();
        NumberFormatter numberFormatter = new NumberFormatter(intFormat);
        numberFormatter.setAllowsInvalid(false);
        alphaField =  new JFormattedTextField(numberFormatter);
        alphaField.setText("4");
        alphaField.setFont(MainFrame.getBasicFont());
        numberFormatter.setMinimum(0L);
        alphaField.setEditable(false);
        alphaField.setBorder(BorderFactory.createEmptyBorder());

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

        var buttonPanel = new JPanel();
        var endButton = new JButton("Zakończ");

        buttonPanel.add(okButton);
        okButton.setEnabled(false);
        okButton.addActionListener(e-> dispose());
        buttonPanel.add(endButton);
        endButton.addActionListener(e->{
            regions = new ArrayList<>();
            dispose();
        });


        if(image.getWidth() >= image.getHeight()){
            var layout = new BorderLayout();
            setLayout(layout);
            ImagePanel imagePanel = new ImagePanel(image);
            imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            add(imagePanel, BorderLayout.NORTH);
            var bottomPanel = new JPanel();
            bottomPanel.setLayout(new GridBagLayout());
            var c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = c.gridheight = 1;
            c.insets.set(0,0,5,5);
            bottomPanel.add(slider, c);
            c.gridx = 1;
            c.insets.set(0,5,5,5);
            bottomPanel.add(alphaLabel, c);
            c.gridx = 2;
            c.insets.set(0,5,5,5);
            bottomPanel.add(alphaField, c);
            c.gridx = 3;
            c.anchor = GridBagConstraints.CENTER;
            bottomPanel.add(buttonPanel, c);
            add(bottomPanel, BorderLayout.SOUTH);
        }else{
            var layout = new BorderLayout();
            setLayout(layout);
            ImagePanel imagePanel = new ImagePanel(image);
            imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            add(imagePanel, BorderLayout.CENTER);

            var sidePanel = new JPanel();
            sidePanel.setLayout(new GridBagLayout());
            var c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.fill = GridBagConstraints.VERTICAL;
            c.anchor = GridBagConstraints.EAST;
            c.insets.set(15, 5, 5, 5);
            sidePanel.add(alphaLabel, c);

            c.insets.set(15, 5, 5, 5);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 1;
            sidePanel.add(alphaField, c);

            c.gridx = 0;
            c.gridwidth = 2;
            c.gridy = 1;
            c.insets.set(5, 5, 0, 5);
            sidePanel.add(slider, c);

            c.gridy = 2;
            c.insets.set(5, 5, 15, 5);
            sidePanel.add(buttonPanel,c);
            add(sidePanel, BorderLayout.EAST);
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
                g2.setPaint(new Color(255, 255, 255, 182));
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
                okButton.setEnabled(!regions.isEmpty());

            }

            @Override
            public void mouseClicked(MouseEvent e) {
                current_circle = findPoint(e.getPoint());
                if(current_circle != null && e.getClickCount() >= 2){
                    removePoint(current_circle);
                }
                okButton.setEnabled(!regions.isEmpty());
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
