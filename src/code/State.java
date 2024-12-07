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

    public int calculateHeuristic1(State goal) {
        //System.out.println("MAN");
        //Manhattan
        int goalX = goal.getX();
        int goalY = goal.getY();

        return Math.abs(goalX - getX()) + Math.abs(goalY - getY());
    }

    public int calculateHeuristic2(State goal) {
        //System.out.println("EUC");
        //Euclidean
        int goalX = goal.getX();
        int goalY = goal.getY();

        return (int) Math.sqrt(Math.pow(goalX - getX(), 2) + Math.pow(goalY - getY(), 2));
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
}
