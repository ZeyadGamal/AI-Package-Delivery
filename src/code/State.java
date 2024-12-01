package code;

import java.util.Map;

public class State {
    private int[][] grid;
    private int x;
    private int y;
    private Map<String, Integer> trafficCosts;
    /*
    private int numOfPackagesDelivered;
    private ArrayList<int[]> customerLocations;

     */


    public State(int[][] grid, int x, int y, Map<String, Integer> trafficCosts) {
        this.grid = grid;
        this.x = x;
        this.y = y;
        this.trafficCosts = trafficCosts;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int[][] getGrid() {
        return grid;
    }

    public Map<String, Integer> getTrafficCosts() {
        return trafficCosts;
    }

    public int calculateHeuristic1() {
        //TODO Manhattan
        return 1;
    }

    public int calculateHeuristic2() {
        //TODO Euclidean
        return 1;
    }
}
