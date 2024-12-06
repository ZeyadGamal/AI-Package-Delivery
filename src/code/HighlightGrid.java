package code;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class HighlightGrid extends JFrame {

    private int rows; // Number of rows (cell rows, not points)
    private int cols; // Number of columns (cell columns, not points)
    private JPanel gridPanel;

    /**
     * Constructor that creates a grid of (N+1)x(M+1) intersection points and highlights specified borders.
     *
     * @param rows       Number of rows in the grid (cells, so points = rows + 1).
     * @param cols       Number of columns in the grid (cells, so points = cols + 1).
     * @param bordersData List of border specifications. Each entry specifies:
     *                   [x, y, "UP"/"DOWN"/"LEFT"/"RIGHT"] for specific borders,
     *                   or [x1, y1, "TUNNEL", x2, y2] for drawing a straight line between points.
     */
    public HighlightGrid(int rows, int cols, List<String[]> bordersData) {
        this.rows = rows-1;
        this.cols = cols-1;

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
                drawGrid(g, bordersData);
            }
        };

        gridPanel.setBorder(new EmptyBorder(40, 40, 40, 40)); // Padding around the grid (40px on all sides)
        add(gridPanel, BorderLayout.CENTER);
    }

    /**
     * Draws the grid and highlights the specified borders and tunnels.
     *
     * @param g           Graphics object to draw the grid.
     * @param bordersData List of border specifications.
     */
    private void drawGrid(Graphics g, List<String[]> bordersData) {
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

        // Highlight specified borders and tunnels
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(3)); // Bold red stroke for highlighted borders
        for (String[] borderSpec : bordersData) {
            try {
                if (borderSpec.length == 3) { // Standard borders (UP, DOWN, LEFT, RIGHT)
                    int x = Integer.parseInt(borderSpec[0]);
                    int y = Integer.parseInt(borderSpec[1]);
                    String direction = borderSpec[2].toUpperCase();

                    int startX = x * cellWidth + 40; // Adjust for left padding
                    int startY = (pointRows - 1 - y) * cellHeight + 40; // Flip y-axis and adjust for top padding

                    switch (direction) {
                        case "UP":
                            g2d.drawLine(startX, startY, startX, startY - cellHeight);
                            break;
                        case "DOWN":
                            g2d.drawLine(startX, startY, startX, startY + cellHeight);
                            break;
                        case "LEFT":
                            g2d.drawLine(startX, startY, startX - cellWidth, startY);
                            break;
                        case "RIGHT":
                            g2d.drawLine(startX, startY, startX + cellWidth, startY);
                            break;
                        default:
                            System.out.println("Invalid direction: " + direction);
                    }
                } else if (borderSpec.length == 5 && borderSpec[2].equalsIgnoreCase("TUNNEL")) { // TUNNEL
                    int x1 = Integer.parseInt(borderSpec[0]);
                    int y1 = Integer.parseInt(borderSpec[1]);
                    int x2 = Integer.parseInt(borderSpec[3]);
                    int y2 = Integer.parseInt(borderSpec[4]);

                    int startX = x1 * cellWidth + 40;
                    int startY = (pointRows - 1 - y1) * cellHeight + 40;
                    int endX = x2 * cellWidth + 40;
                    int endY = (pointRows - 1 - y2) * cellHeight + 40;

                    g2d.drawLine(startX, startY, endX, endY);
                } else {
                    System.out.println("Invalid border specification: " + String.join(", ", borderSpec));
                }
            } catch (Exception e) {
                System.out.println("Invalid border specification: " + String.join(", ", borderSpec));
            }
        }
    }

    public static void main(String[] args) {
        // Example usage:
        // Highlight borders and tunnels. Each border is specified as [x, y, "UP"/"DOWN"/"LEFT"/"RIGHT"]
        // or [x1, y1, "TUNNEL", x2, y2].
        List<String[]> bordersData = List.of(
                new String[]{"0", "0", "UP"},         // Bottom-left border (0,0), "UP"
                new String[]{"1", "1", "RIGHT"},     // Border to the right of (1,1)
                new String[]{"2", "2", "DOWN"},      // Border below (2,2)
                new String[]{"3", "3", "LEFT"},      // Border to the left of (3,3)
                new String[]{"0", "0", "TUNNEL", "3", "3"}, // Tunnel from (0,0) to (3,3)
                new String[]{"1", "3", "TUNNEL", "3", "1"}  // Tunnel from (1,3) to (3,1)
        );

        // Create a grid with 4 rows and 4 columns (so 5x5 intersection points)
        SwingUtilities.invokeLater(() -> {
            HighlightGrid frame = new HighlightGrid(4, 8, bordersData);
            frame.setVisible(true);
        });
    }
}
