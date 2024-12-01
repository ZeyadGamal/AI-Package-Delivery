package code;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class DeliveryProblem {
    private State initialState;
    private ArrayList<Action> actions;
    private State goalState;
    private ArrayList<Point> visitedStates;

    public DeliveryProblem(State initialState, ArrayList<Action> actions, State goalState) {
        this.initialState = initialState;
        this.actions = actions;
        this.goalState = goalState;
        this.visitedStates = new ArrayList<>();
    }

    public boolean isGoal(State currState){

        return currState.getX() == goalState.getX() && currState.getY() == goalState.getY();
    }

    public ArrayList<Action> getActions(){
        return actions;
    }

    public State getInitialState(){
        return initialState;
    }

    public ArrayList<Node> expand(Node currNode, int heuristicType){
        State currState = currNode.getState();
        ArrayList<Node> children = new ArrayList<Node>();

        for (Action action : this.getActions()){
            int newX = currNode.getState().getX();
            int newY = currNode.getState().getY();

            switch (action){
                case UP:
                    newY +=1;
                    break;
                case DOWN:
                    newY -=1;
                    break;
                case LEFT:
                    newX -=1;
                    break;
                case RIGHT:
                    newX +=1;
                    break;
                case TUNNEL:
                    //TODO Implement tunnel logic
                    newX +=1; //PLACEHOLDER
                    break;
            }


            if (newX < 0 || newX >= currState.getGrid().length || newY < 0 || newY >= currState.getGrid()[0].length){
                continue;
            }

            //System.out.println("New X, New Y: " + newX + " " + newY);


            int cost = calculateCost(currState.getX(), currState.getY(), newX, newY);

            if (cost == 0) //Blocked Path
                continue;



            //Check if product reached
            /*
            ArrayList<int[]> customers = new ArrayList<>(currState.getCustomerLocations());
            int packagesDelivered = currState.getNumOfPackagesDelivered();

            for (int[] location : customers) {
                if (location[0] == newX && location[1] == newY){
                    packagesDelivered++;
                    customers.remove(location);

                }
            }

             */
            State newState = new State(currState.getGrid(), newX, newY,currState.getTrafficCosts());

            if (isGoal(newState)) {
                System.out.println("GOAL!!!!");
            }

            Point newStatePoint = new Point(newX, newY);

            if (checkVisited(newStatePoint)){
                continue;
            }

            visitedStates.add(newStatePoint);

            int newCost = currNode.getCost() + cost;


            int heuristic = 0;
            if (heuristicType == 1){
                heuristic = currState.calculateHeuristic1();
            }
            else {
                heuristic = currState.calculateHeuristic2();
            }

            Node child = new Node(newState, currNode, newCost, currNode.getDepth()+1, action, heuristic);

            children.add(child);

            //System.out.println(child.getState().getX() + " " + child.getState().getY());
        }
        return children;
    }


    public int calculateCost(int srcX, int srcY, int dstX, int dstY){
        String key = srcX + "," + srcY + "->" + dstX + "," + dstY;
        Map<String, Integer> trafficMap = initialState.getTrafficCosts();
        return trafficMap.get(key);
    }

    public boolean checkVisited(Point currPoint){
        for (Point point : visitedStates){
            if (point.getX() == currPoint.getX() && point.getY() == currPoint.getY()){
                return true;
            }
        }
        return false;
    }
}
