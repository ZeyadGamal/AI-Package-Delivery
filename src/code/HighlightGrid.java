package code;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class HighlightGrid extends JFrame {

    private int rows; // Number of rows (cell rows, not points)
    private int cols; // Number of columns (cell columns, not points)
    private JPanel gridPanel;
    private List<String[]> bordersData; // Path data
    private Map<Point, Point> tunnels; // Map to store tunnel connections
    private int currentPathIndex = 0; // Tracks which path is being shown

    /**
     * Constructor that creates a grid of (N+1)x(M+1) intersection points and highlights specified borders.
     *
     * @param rows        Number of rows in the grid (cells, so points = rows + 1).
     * @param cols        Number of columns in the grid (cells, so points = cols + 1).
     * @param bordersData List of path specifications. Each entry specifies:
     *                    [x, y, "ACTION1", "ACTION2", ...].
     * @param tunnelArray Array of tunnels in the format "x1,y1;x2,y2".
     */
    public HighlightGrid(int rows, int cols, List<String[]> bordersData, String[] tunnelArray) {
        this.rows = rows;
        this.cols = cols;
        this.bordersData = bordersData;
        this.tunnels = parseTunnels(tunnelArray);

        setTitle("Grid with Highlighted Borders and Tunnels");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main layout
        setLayout(new BorderLayout());

        // Panel for the grid
        gridPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGrid(g); // Always draw the grid
                if (currentPathIndex < bordersData.size()) {
                    drawPath(g, bordersData.get(currentPathIndex)); // Draw the current path
                }
            }
        };

        gridPanel.setBorder(new EmptyBorder(40, 40, 40, 40)); // Padding around the grid (40px on all sides)
        add(gridPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        // Previous button
        JButton prevButton = new JButton("Previous");
        prevButton.addActionListener(e -> {
            currentPathIndex--;
            if (currentPathIndex < 0) {
                currentPathIndex = bordersData.size() - 1; // Wrap to the last path
            }
            repaint(); // Refresh the panel
        });
        buttonPanel.add(prevButton);

        // Next button
        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> {
            currentPathIndex++;
            if (currentPathIndex >= bordersData.size()) {
                currentPathIndex = 0; // Wrap to the first path
            }
            repaint(); // Refresh the panel
        });
        buttonPanel.add(nextButton);

        // Add button panel to the frame
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Parses the tunnel array and creates a bidirectional map of tunnel entrances and exits.
     *
     * @param tunnelArray Array of tunnels in the format "x1,y1;x2,y2".
     * @return A map where each key is an entrance and each value is the corresponding exit.
     */
    private Map<Point, Point> parseTunnels(String[] tunnelArray) {
        Map<Point, Point> tunnelMap = new HashMap<>();
        for (String tunnel : tunnelArray) {
            String[] points = tunnel.split(";");
            String[] start = points[0].split(",");
            String[] end = points[1].split(",");

            Point entrance = new Point(Integer.parseInt(start[0]), Integer.parseInt(start[1]));
            Point exit = new Point(Integer.parseInt(end[0]), Integer.parseInt(end[1]));

            // Add bidirectional mapping
            tunnelMap.put(entrance, exit);
            tunnelMap.put(exit, entrance);
        }
        return tunnelMap;
    }

    /**
     * Draws the grid with dotted borders and intersection points.
     *
     * @param g Graphics object to draw the grid.
     */
    private void drawGrid(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        int panelWidth = gridPanel.getWidth();
        int panelHeight = gridPanel.getHeight();

        int pointRows = rows + 1; // Points are one more than the number of rows
        int pointCols = cols + 1; // Points are one more than the number of columns

        // Calculate spacing between points
        int cellWidth = (panelWidth - 80) / (pointCols); // Adjust for 40px padding on left/right
        int cellHeight = (panelHeight - 80) / (pointRows); // Adjust for 40px padding on top/bottom

        // Draw the grid
        for (int i = 0; i < pointRows; i++) {
            for (int j = 0; j < pointCols; j++) {
                int x = j * cellWidth + 40; // Adjust for left padding
                int y = i * cellHeight + 40; // Adjust for top padding

                // Draw the intersection point
                g2d.setColor(Color.BLACK);
                g2d.fillOval(x - 2, y - 2, 5, 5); // Small circle for points

                // Draw dotted borders around each intersection point
                g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1, new float[]{5, 5}, 0));
                if (i < pointRows - 1) g2d.drawLine(x, y, x, y + cellHeight); // Down
                if (j < pointCols - 1) g2d.drawLine(x, y, x + cellWidth, y); // Right
            }
        }
    }

    /**
     * Draws a single path based on the actions specified in bordersData.
     *
     * @param g    Graphics object to draw the path.
     * @param spec The path specification: [x, y, "ACTION1", "ACTION2", ...].
     */
    private void drawPath(Graphics g, String[] spec) {
        Graphics2D g2d = (Graphics2D) g;
        int panelWidth = gridPanel.getWidth();
        int panelHeight = gridPanel.getHeight();

        int pointRows = rows + 1;
        int pointCols = cols + 1;

        // Calculate spacing between points
        int cellWidth = (panelWidth - 80) / (pointCols);
        int cellHeight = (panelHeight - 80) / (pointRows);

        try {
            int x = Integer.parseInt(spec[0]);
            int y = Integer.parseInt(spec[1]);

            int startX = x * cellWidth + 40;
            int startY = (pointRows - 1 - y) * cellHeight + 40;

            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3)); // Bold red stroke for paths

            for (int i = 2; i < spec.length; i++) {
                String action = spec[i].toUpperCase();

                if (action.equals("UP")) {
                    g2d.drawLine(startX, startY, startX, startY - cellHeight);
                    startY -= cellHeight;
                } else if (action.equals("DOWN")) {
                    g2d.drawLine(startX, startY, startX, startY + cellHeight);
                    startY += cellHeight;
                } else if (action.equals("LEFT")) {
                    g2d.drawLine(startX, startY, startX - cellWidth, startY);
                    startX -= cellWidth;
                } else if (action.equals("RIGHT")) {
                    g2d.drawLine(startX, startY, startX + cellWidth, startY);
                    startX += cellWidth;
                } else if (action.equals("TUNNEL")) {
                    System.out.println("TunnelFound");
                    Point currentPoint = new Point(x, y);
                    if (tunnels.containsKey(currentPoint)) {
                        Point exitPoint = tunnels.get(currentPoint);
                        int endX = exitPoint.x * cellWidth + 40;
                        int endY = (pointRows - 1 - exitPoint.y) * cellHeight + 40;
                        g2d.drawLine(startX, startY, endX, endY);
                        x = exitPoint.x;
                        y = exitPoint.y;
                        startX = endX;
                        startY = endY;
                    }
                } else {
                    System.out.println("Invalid action: " + action);
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid specification: " + String.join(", ", spec));
        }
    }

    public static void main(String[] args) {
        // Example usage:
        List<String[]> bordersData = List.of(
                new String[]{"0", "3", "UP", "TUNNEL", "RIGHT"}, // Tunnels to 3,3
                new String[]{"2", "2", "DOWN", "LEFT"}          // Basic path
        );

        String[] tunnelArray = {"0,0;3,3", "1,1;2,2"}; // Multiple bidirectional tunnels

        SwingUtilities.invokeLater(() -> {
            HighlightGrid frame = new HighlightGrid(4, 4, bordersData, tunnelArray);
            frame.setVisible(true);
        });
    }
}

