package Dialogs;

import Utils.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class RegionChooseDialog extends JDialog {
    private ArrayList<Point> regions = new ArrayList<>();
    private final int DIALOG_WIDTH;
    private final int DIALOG_HEIGHT;
    private double scale;

    public RegionChooseDialog(JFrame owner, BufferedImage image, double scale){
        super(owner, "Wybierz region", true);
        this.scale = scale;
        setResizable(false);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        DIALOG_WIDTH = image.getWidth();
        DIALOG_HEIGHT = image.getHeight() + 100;
        double SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        double SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        int x = (int)(SCREEN_WIDTH - DIALOG_WIDTH) / 2;
        int y = (int)(SCREEN_HEIGHT - DIALOG_HEIGHT) / 2;
        setBounds(x, y, DIALOG_WIDTH, DIALOG_HEIGHT);

        var layout = new BorderLayout();
        setLayout(layout);
        ImagePanel imagePanel = new ImagePanel(image);
        add(imagePanel, BorderLayout.NORTH);

        var buttonPanel = new JPanel();
        var okButton = new JButton("Zatwierdź");
        var endButton = new JButton("Zakończ");

        buttonPanel.add(okButton);
        okButton.addActionListener(e->{
            dispose();
        });
        buttonPanel.add(endButton);
        endButton.addActionListener(e->{
            regions = new ArrayList<>();
            dispose();
        });

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public ArrayList<Point> getRegions(){ return regions; }
    public class ImagePanel extends JPanel {
        private final BufferedImage image;
        private final ArrayList<Ellipse2D> circles = new ArrayList<>();

        public ImagePanel(BufferedImage image) {
            //setSize(image.getWidth(), image.getHeight());
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
                g2.setPaint(Color.WHITE);
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

            circles.add(new Ellipse2D.Double(x - 2.5, y - 2.5, 10, 10));
            repaint();
        }
        private void removePoint(Ellipse2D p){
            regions.remove(p);
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
                //TODO usunięcie
                var clicked = findPoint(e.getPoint());
                if(clicked != null && e.getClickCount() >= 1) removePoint(clicked);
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
                //TODO przeciągnięcie
            }
        }
    }
}
