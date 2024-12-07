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
    private Map<Point, Point> tunnels;

    public DeliveryProblem(State initialState, ArrayList<Action> actions, State goalState, Map<Point, Point> tunnels) {
        this.initialState = initialState;
        this.actions = actions;
        this.goalState = goalState;
        this.visitedStates = new ArrayList<>();
        this.tunnels = tunnels;
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
        int cost = 0;

        Point currPoint = new Point(currState.getX(), currState.getY());
        if (checkTunnel(currPoint)){
            Point tunnelExit = tunnels.get(currPoint);
            int tunnelExitX = (int) tunnelExit.getX();
            int tunnelExitY = (int) tunnelExit.getY();

            State tunnelExitState = new State(currState.getGrid(), tunnelExitX, tunnelExitY, currState.getTrafficCosts());
            int tunnelCost = Math.abs(currState.getX() - tunnelExitX) + Math.abs(currState.getY() - tunnelExitY);

            if (!checkVisited(tunnelExit)){
                int exitCost = currNode.getCost() + tunnelCost;
                int heuristic = (heuristicType == 1) ? currState.calculateHeuristic1(goalState) : currState.calculateHeuristic2(goalState);
                Node tunnelExitNode = new Node(tunnelExitState, currNode, exitCost, currNode.getDepth() + 1, Action.TUNNEL, heuristic);
                children.add(tunnelExitNode);
                visitedStates.add(tunnelExit);

            }
        }


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
            }
            if (newX < 0 || newX >= currState.getGrid().length || newY < 0 || newY >= currState.getGrid()[0].length){
                continue;
            }
            Point newPoint = new Point(newX, newY);

            cost = calculateCost(currState.getX(), currState.getY(), newX, newY);

            if (cost == 0) //Blocked Path
                continue;

//            if (checkTunnel(newPoint)){
//
//                Point tunnelPoint = tunnels.get(newPoint);
//                newX = (int) tunnelPoint.getX();
//                newY = (int) tunnelPoint.getY();
//                action = Action.TUNNEL;
//
//
//
//                cost = Math.abs((int) newPoint.getX() - (int) tunnelPoint.getX()) + Math.abs((int) newPoint.getY() - (int) tunnelPoint.getY());
//            }




            //System.out.println("New X, New Y: " + newX + " " + newY);






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



            Point newStatePoint = new Point(newX, newY);

            if (checkVisited(newStatePoint)){
                continue;
            }
            if (isGoal(newState)) {
                //System.out.println("GOAL!!!!");
            }
            visitedStates.add(newStatePoint);

            int newCost = currNode.getCost() + cost;

            //System.out.println(newCost + " = " + currNode.getCost() + " + " + cost);


            int heuristic = 0;
            if (heuristicType == 1){
                heuristic = currState.calculateHeuristic1(goalState);
            }
            else {
                heuristic = currState.calculateHeuristic2(goalState);
            }

            Node child = new Node(newState, currNode, newCost, currNode.getDepth()+1, action, heuristic);

            children.add(child);

            //System.out.println(child.getState().getX() + " " + child.getState().getY());
        }
        return children;
    }

    public ArrayList<Node> expandIDS(Node currNode, int heuristicType, boolean newIt){
        State currState = currNode.getState();
        ArrayList<Node> children = new ArrayList<Node>();
        int cost = 0;

        if (newIt){
            visitedStates.clear();
        }

        Point currPoint = new Point(currState.getX(), currState.getY());
        if (checkTunnel(currPoint)){
            Point tunnelExit = tunnels.get(currPoint);
            int tunnelExitX = (int) tunnelExit.getX();
            int tunnelExitY = (int) tunnelExit.getY();

            State tunnelExitState = new State(currState.getGrid(), tunnelExitX, tunnelExitY, currState.getTrafficCosts());
            int tunnelCost = Math.abs(currState.getX() - tunnelExitX) + Math.abs(currState.getY() - tunnelExitY);

            if (!checkVisited(tunnelExit)){
                int exitCost = currNode.getCost() + tunnelCost;
                int heuristic = (heuristicType == 1) ? currState.calculateHeuristic1(goalState) : currState.calculateHeuristic2(goalState);
                Node tunnelExitNode = new Node(tunnelExitState, currNode, exitCost, currNode.getDepth() + 1, Action.TUNNEL, heuristic);
                System.out.println("Added Exit Tunnel");
                children.add(tunnelExitNode);
                visitedStates.add(tunnelExit);

            }
        }

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
            }
            Point newPoint = new Point(newX, newY);
//            if (checkTunnel(newPoint)){
//                Point tunnelPoint = tunnels.get(newPoint);
//                newX = (int) tunnelPoint.getX();
//                newY = (int) tunnelPoint.getY();
//                action = Action.TUNNEL;
//
//                cost = Math.abs((int) newPoint.getX() - (int) tunnelPoint.getX()) + Math.abs((int) newPoint.getY() - (int) tunnelPoint.getY());
//            }


            if (newX < 0 || newX >= currState.getGrid().length || newY < 0 || newY >= currState.getGrid()[0].length){
                continue;
            }

            //System.out.println("New X, New Y: " + newX + " " + newY);
            if (!action.equals(Action.TUNNEL)) {
                cost = calculateCost(currState.getX(), currState.getY(), newX, newY);
            }

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



            Point newStatePoint = new Point(newX, newY);

            if (checkVisited(newStatePoint)){
                continue;
            }
            if (isGoal(newState)) {
                //System.out.println("GOAL!!!!");
            }
            visitedStates.add(newStatePoint);

            int newCost = currNode.getCost() + cost;

            //System.out.println(newCost + " = " + currNode.getCost() + " + " + cost);


            int heuristic = 0;
            if (heuristicType == 1){
                heuristic = currState.calculateHeuristic1(goalState);
            }
            else {
                heuristic = currState.calculateHeuristic2(goalState);
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

    public boolean checkTunnel(Point point){

        return tunnels.containsKey(point);

    }
}
