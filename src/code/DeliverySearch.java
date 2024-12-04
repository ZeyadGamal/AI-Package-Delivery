package code;

import java.awt.*;
import java.util.*;

public class DeliverySearch extends GenericSearch{

    public DeliverySearch() {
        super();
    }

    public static String solve(String initalState, String traffic, Strategy strategy){

        //parse the input grid string

        String[] parts = initalState.split(";");
        int m = Integer.parseInt(parts[0]);
        int n = Integer.parseInt(parts[1]);
        int p = Integer.parseInt(parts[2]);
        int s = Integer.parseInt(parts[3]);
        String[] customers = parts[4].split(",");
        String[] stores = parts[5].split(",");
        String[] tunnels = parts[6].split(",");

        int[][] grid = new int[m][n];

        ArrayList<int[]> customerLocations = new ArrayList<>();
        ArrayList<int[]> storeLocations = new ArrayList<>();
        Map<Point, Point> tunnelLocations = new HashMap<>();

        // Parse customer locations
        for (int i = 0; i < customers.length; i += 2) {
            int x = Integer.parseInt(customers[i]);
            int y = Integer.parseInt(customers[i + 1]);
            customerLocations.add(new int[]{x, y});
        }

        //System.out.println(customerLocations.size());

        // Parse store locations
        for (int i = 0; i < stores.length; i += 2) {
            int x = Integer.parseInt(stores[i]);
            int y = Integer.parseInt(stores[i + 1]);
            storeLocations.add(new int[]{x, y});
        }

        // Parse tunnel locations
        for (int i = 0; i < tunnels.length; i += 4) {
            int x1 = Integer.parseInt(tunnels[i]);
            int y1 = Integer.parseInt(tunnels[i + 1]);
            Point p1 = new Point(x1, y1);
            int x2 = Integer.parseInt(tunnels[i + 2]);
            int y2 = Integer.parseInt(tunnels[i + 3]);
            Point p2 = new Point(x2, y2);
            tunnelLocations.put(p1, p2);
            tunnelLocations.put(p2, p1);
        }
        Map<String, Integer> trafficCosts = new HashMap<>();
        String[] traffics = traffic.split(";");
        for (String t: traffics){
            String[] trafficData = t.split(",");
            String key1 = trafficData[0] + "," + trafficData[1] + "->" + trafficData[2] + "," + trafficData[3];
            String key2 = trafficData[2] + "," + trafficData[3] + "->" + trafficData[0] + "," + trafficData[1];
            int cost = Integer.parseInt(trafficData[4]);
            trafficCosts.put(key1, cost);
            trafficCosts.put(key2, cost);
        }



        //Trucks states
        ArrayList<State> trucks = new ArrayList<>();
        for (int[] storeLocation : storeLocations) {
            int x = storeLocation[0];
            int y = storeLocation[1];
            State truck = new State(grid, x, y,trafficCosts);
            trucks.add(truck);
        }

        //Products states
        ArrayList<State> products = new ArrayList<>();
        for (int[] customerLocation : customerLocations) {
            int x = customerLocation[0];
            int y = customerLocation[1];
            State product = new State(grid, x, y,trafficCosts);
            products.add(product);
        }

        //System.out.println(products.size());

        //Get Paths for each truck-product
        ArrayList<String> paths = new ArrayList<>();


        Map<State, State> truckProduct = new HashMap<>();

        for (State product : products) {
            int minCost = Integer.MAX_VALUE;
            State bestTruck = null;
            String bestPath = "";
            for (State truck : trucks) {
                // Calculate the path and cost for the current truck to the current product
                String path = path(truck, product, strategy, tunnelLocations);
                paths.add(path);

                String[] pathParts = path.split(";");
                int pathCost = Integer.parseInt(pathParts[1]);
                if (pathCost < minCost) {
                    minCost = pathCost;
                    bestTruck = truck;
                    bestPath = path;
                }
            }
            if (bestTruck != null) {
                // Assign the best truck to the product
                truckProduct.put(product, bestTruck);

            }
        }

        //System.out.println(truckProduct.size());

        String result = "";
        for (Map.Entry<State, State> entry : truckProduct.entrySet()) {
            String path = path(entry.getValue(), entry.getKey(), strategy, tunnelLocations);
            result = "Truck at (" + entry.getValue().getX() + "," + entry.getValue().getY() + ") --> Product at (" + entry.getKey().getX() + "," + entry.getKey().getY() + ") "  + path +  '\n' + result;
        }




        return result;
    }

    public static String path(State truck, State product, Strategy strategy, Map<Point, Point> tunnels){
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(Action.UP);
        actions.add(Action.DOWN);
        actions.add(Action.LEFT);
        actions.add(Action.RIGHT);
        DeliveryProblem problem = new DeliveryProblem(truck,actions,product, tunnels);
        int heurisitcType;
        if (strategy.equals(Strategy.ASTAR1) || strategy.equals(Strategy.GREEDY1)) {
            heurisitcType = 1;
        }
        else{
            heurisitcType = 2;
        }
        String path = search(problem,strategy, heurisitcType);
        return path;
    }



    public void visualizeGrid(){

    }



}
