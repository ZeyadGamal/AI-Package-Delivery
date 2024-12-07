package code;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class HighlightGrid extends JFrame {
    private int rows, cols;
    private List<String[]> paths;
    private Map<Point, Point> tunnels;
    private int currentPathIndex = 0;
    private int padding = 50;

    public HighlightGrid(int rows, int cols, List<String[]> paths, String[] tunnelArray) {
        this.rows = rows;
        this.cols = cols;
        this.paths = paths;
        this.tunnels = new HashMap<>();

        // Parse the tunnel array and store bidirectional tunnels
        for (String tunnel : tunnelArray) {
            String[] coords = tunnel.split(";");
            String[] startCoords = coords[0].split(",");
            String[] endCoords = coords[1].split(",");

            Point start = new Point(Integer.parseInt(startCoords[0]), Integer.parseInt(startCoords[1]));
            Point end = new Point(Integer.parseInt(endCoords[0]), Integer.parseInt(endCoords[1]));

            tunnels.put(start, end);
            tunnels.put(end, start); // Bidirectional
        }

        setTitle("Highlight Grid with Tunnels");
        setSize(600, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JButton prevButton = new JButton("Previous Path");
        JButton nextButton = new JButton("Next Path");

        prevButton.addActionListener(e -> {
            currentPathIndex = (currentPathIndex - 1 + paths.size()) % paths.size();
            repaint();
        });

        nextButton.addActionListener(e -> {
            currentPathIndex = (currentPathIndex + 1) % paths.size();
            repaint();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        int cellWidth = (getWidth() - 2 * padding) / cols;
        int cellHeight = (getHeight() - 2 * padding) / rows;

        // Draw the grid
        g2d.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= cols; i++) {
            int x = padding + i * cellWidth;
            g2d.drawLine(x, padding, x, padding + rows * cellHeight);
        }
        for (int i = 0; i <= rows; i++) {
            int y = padding + i * cellHeight;
            g2d.drawLine(padding, y, padding + cols * cellWidth, y);
        }

        // Draw tunnels
        g2d.setStroke(new BasicStroke(2));
        for (Map.Entry<Point, Point> tunnel : tunnels.entrySet()) {
            Point start = tunnel.getKey();
            Point end = tunnel.getValue();

            int startX = padding + start.x * cellWidth;
            int startY = padding + (rows - start.y) * cellHeight; // Corrected flipped y-coordinate
            int endX = padding + end.x * cellWidth;
            int endY = padding + (rows - end.y) * cellHeight; // Corrected flipped y-coordinate

            g2d.setColor(Color.MAGENTA); // Tunnel color
            g2d.drawLine(startX, startY, endX, endY);
        }

        // Draw the current path
        if (!paths.isEmpty()) {
            String[] path = paths.get(currentPathIndex);

            int x = Integer.parseInt(path[0]);
            int y = Integer.parseInt(path[1]);

            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3));

            for (int i = 2; i < path.length; i++) {
                String action = path[i];

                int newX = x, newY = y;
                if (action.equals("UP")) {
                    newY++;
                } else if (action.equals("DOWN")) {
                    newY--;
                } else if (action.equals("LEFT")) {
                    newX--;
                } else if (action.equals("RIGHT")) {
                    newX++;
                } else if (action.equals("TUNNEL")) {
                    Point start = new Point(x, y);
                    Point end = tunnels.get(start);

                    if (end != null) {
                        newX = end.x;
                        newY = end.y;

                        int startX = padding + x * cellWidth;
                        int startY = padding + (rows - y) * cellHeight; // Corrected flipped y-coordinate
                        int endX = padding + newX * cellWidth;
                        int endY = padding + (rows - newY) * cellHeight; // Corrected flipped y-coordinate

                        g2d.drawLine(startX, startY, endX, endY);
                    }
                }

                int startX = padding + x * cellWidth;
                int startY = padding + (rows - y) * cellHeight; // Corrected flipped y-coordinate
                int endX = padding + newX * cellWidth;
                int endY = padding + (rows - newY) * cellHeight; // Corrected flipped y-coordinate

                g2d.drawLine(startX, startY, endX, endY);
                x = newX;
                y = newY;
            }
        }
    }


    public static void main(String[] args) {
        List<String[]> paths = List.of(
                new String[]{"0", "0", "RIGHT"},
                new String[]{"0", "2", "UP", "RIGHT"},
                new String[]{"0", "1", "DOWN"},
                new String[]{"3", "0", "RIGHT", "RIGHT", "UP", "RIGHT", "UP"},
                new String[]{"0", "2", "RIGHT", "RIGHT", "DOWN"}
        );

        String[] tunnels = {
                "3,0;4,3",
                "1,1;2,2"
        };

        SwingUtilities.invokeLater(() -> {
            HighlightGrid frame = new HighlightGrid(5, 5, paths, tunnels);
            frame.setVisible(true);
        });
    }
}
