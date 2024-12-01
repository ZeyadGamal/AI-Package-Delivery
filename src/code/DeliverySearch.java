package code;

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
        ArrayList<int[]> tunnelLocations = new ArrayList<>();

        // Parse customer locations
        for (int i = 0; i < customers.length; i += 2) {
            int x = Integer.parseInt(customers[i]);
            int y = Integer.parseInt(customers[i + 1]);
            customerLocations.add(new int[]{x, y});
        }

        // Parse store locations
        for (int i = 0; i < stores.length; i += 2) {
            int x = Integer.parseInt(stores[i]);
            int y = Integer.parseInt(stores[i + 1]);
            storeLocations.add(new int[]{x, y});
        }

        // Parse tunnel locations
        for (int i = 0; i < tunnels.length; i += 2) {
            int x = Integer.parseInt(tunnels[i]);
            int y = Integer.parseInt(tunnels[i + 1]);
            tunnelLocations.add(new int[]{x, y});
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

        //Get Paths for each truck-product
        ArrayList<String> paths = new ArrayList<>();


        for (State truck : trucks) {
            for (State product : products) {
                System.out.println("Truck: "+ truck.getX() + "," + truck.getY());
                System.out.println("Product: "+ product.getX() + "," + product.getY());
                String path = path(truck, product, strategy);
                System.out.println(path);
                paths.add(path);
            }
        }




        return "";
    }

    public static String path(State truck, State product, Strategy strategy){
        ArrayList<Action> actions = new ArrayList<>();
        actions.add(Action.UP);
        actions.add(Action.DOWN);
        actions.add(Action.LEFT);
        actions.add(Action.RIGHT);
        actions.add(Action.TUNNEL);
        DeliveryProblem problem = new DeliveryProblem(truck,actions,product);
        String path = search(problem,strategy,1);
        return path;
    }

    public void visualizeGrid(){

    }


    public static void main(String[] args) {
        String initialState = "5;3;2;1;3,2,3,0;1,2;2,0,1,1;";
        String traffic = "0,1,0,0,3;0,2,0,1,0;1,0,0,0,3;1,1,0,1,1;1,1,1,0,3;1,2,0,2,0;1,2,1,1,1;2,0,1,0,1;2,1,1,1,0;2,1,2,0,1;2,2,1,2,0;2,2,2,1,0;3,0,2,0,3;3,1,2,1,2;3,1,3,0,3;3,2,2,2,0;3,2,3,1,2;4,0,3,0,0;4,1,3,1,0;4,1,4,0,2;4,2,3,2,1;4,2,4,1,2;";

        solve(initialState,traffic,Strategy.DFS);
    }
}
