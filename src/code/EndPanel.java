package code;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class EndPanel {

    public static void main(String[] args) {
        String grid = "5;3;2;1;3,2,3,0;1,2;2,0,1,1;";
        String traffic = "0,1,0,0,3;0,2,0,1,0;1,0,0,0,3;1,1,0,1,1;1,1,1,0,3;1,2,0,2,0;1,2,1,1,1;2,0,1,0,1;2,1,1,1,0;2,1,2,0,1;2,2,1,2,0;2,2,2,1,0;3,0,2,0,3;3,1,2,1,2;3,1,3,0,3;3,2,2,2,0;3,2,3,1,2;4,0,3,0,0;4,1,3,1,0;4,1,4,0,2;4,2,3,2,1;4,2,4,1,2;";
        String path = DeliveryPlanner.plan(grid, traffic, "UC", false);
        System.out.println(path);

        SwingUtilities.invokeLater(() -> {
            createFrame(grid, path);
        });
    }

    public static List<String[]> parsePath(String input) {
        List<String[]> borderData = new ArrayList<>();

        // Split the input into individual routes based on newlines
        String[] routes = input.split("\n");

        for (String route : routes) {
            if (route.trim().isEmpty()) {
                continue; // Skip empty lines
            }

            // Split into main parts: "Truck at (x,y) --> Product at (x,y) [actions];time;cost"
            String[] mainParts = route.split(";");
            String routePart = mainParts[0].trim();

            // Extract coordinates and actions
            String[] splitRoute = routePart.split("-->");
            String truckPart = splitRoute[0].trim();
            String productPart = splitRoute[1].trim();

            // Extract truck coordinates
            int startX = Integer.parseInt(truckPart.substring(truckPart.indexOf('(') + 1, truckPart.indexOf(',')));
            int startY = Integer.parseInt(truckPart.substring(truckPart.indexOf(',') + 1, truckPart.indexOf(')')));

            // Extract product coordinates and actions
            int actionStartIdx = productPart.indexOf(")");
            String actionsPart = productPart.substring(actionStartIdx + 1).trim();
            actionsPart = actionsPart.replace(",", ""); // Remove commas from actions

            // Collect all actions in an array
            String[] actions = actionsPart.split("\\s+");
            List<String> path = new ArrayList<>();
            path.add(String.valueOf(startX)); // Start X
            path.add(String.valueOf(startY)); // Start Y
            for (String action : actions) {
                if (!action.isEmpty()) {
                    path.add(action);
                }
            }

            // Add path to the result list
            borderData.add(path.toArray(new String[0]));
        }

        return borderData;
    }

    private static void createFrame(String grid, String path) {
        String[] dim = getDimensions(grid);
        int m = Integer.parseInt(dim[0]);
        int n = Integer.parseInt(dim[1]);
        String[] tunnels = getTunnels(grid);

        List<String[]> paths = parsePath(path);

        HighlightGrid frame = new HighlightGrid(n-1, m-1, paths, tunnels);
        frame.setVisible(true);
    }

    private static String[] getDimensions(String grid) {
        return grid.substring(0, 3).split(";");
    }

    private static String[] getTunnels(String grid) {
        String[] tunnels = grid.split(";")[6].split(",");
        ArrayList<String> temp = new ArrayList<>();
        for (int i = 0; i < tunnels.length; i += 4) {
            String tun = "";
            tun += tunnels[i];
            tun += ",";
            tun += tunnels[i + 1];
            tun += ";";
            tun += tunnels[i + 2];
            tun += ",";
            tun += tunnels[i + 3];
            temp.add(tun);
        }
        String[] result = new String[temp.size()];
        result = temp.toArray(result);
        return result;
    }
}

