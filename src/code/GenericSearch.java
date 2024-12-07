package code;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class GenericSearch {

    static int pathCost = 0;
    static String plan = "";
    static int nodesExpanded = 0;

    public GenericSearch() {
    }

    public static String search(DeliveryProblem problem, Strategy strategy, int heuristic) {


        nodesExpanded = 0;
        pathCost = 0;

        Comparator<Node> comparator = new NodeComparator(strategy);
        PriorityQueue<Node> queue = new PriorityQueue<Node>(comparator);

        State initialState = problem.getInitialState();

        Node initialNode = new Node(problem.getInitialState(), null, 0, 0, null, 0);

        queue.add(initialNode);

        if (queue.isEmpty()) {
            return "Fail";
        }
        while (!queue.isEmpty()) {
            Node currNode = queue.poll();


            if (problem.isGoal(currNode.getState())) {
                //System.out.println("Depth of Goal: " + currNode.getDepth());
                pathCost = currNode.getCost();
                plan = "";
                while (currNode.getParent() != null) {
                    //System.out.print("("+currNode.getState().getX()+","+currNode.getState().getY()+") ");
                    plan = currNode.getAction().name() + ", " + plan;
                    currNode = currNode.getParent();
                }
                return plan + ";" + pathCost + ";" + nodesExpanded;
            }

            ArrayList<Node> children = problem.expand(currNode, heuristic);

            // System.out.println("children: " + children);

            for (Node child : children) {
                nodesExpanded++;
                queue.add(child);
            }

        }
        return "NULL"; //TODO
    }

    public static String IDS(DeliveryProblem problem, int n, int heuristic) {
        System.out.println("New IDS with Depth = " + n);
        int maxDepth = 0;
        String result = "";
        nodesExpanded = 0;

        boolean newIt = true;

        Comparator<Node> comparator = new NodeComparator(Strategy.DFS);
        PriorityQueue<Node> queue = new PriorityQueue<Node>(comparator);

        State initialState = problem.getInitialState();

        Node initialNode = new Node(problem.getInitialState(), null, 0, 0, null, 0);

        queue.add(initialNode);

        if (queue.isEmpty()) {
            return "Fail";
        }
        while (true) {
            System.out.println("Queue Size = " + queue.size());
            Node currNode = queue.poll();

            if (currNode.getDepth() > maxDepth) {
                maxDepth = currNode.getDepth();
                System.out.println("New Max: " + maxDepth);
            }

            if (problem.isGoal(currNode.getState())) {

                pathCost = currNode.getCost();
                plan = "";
                while (currNode.getParent() != null) {
                    //System.out.print("("+currNode.getState().getX()+","+currNode.getState().getY()+") ");
                    plan = currNode.getAction().name() + ", " + plan;
                    currNode = currNode.getParent();
                }
                return plan + ";" + pathCost + ";" + nodesExpanded;
            }

            if (currNode.getDepth() < n) {
                System.out.println("currNode:" + currNode);
                ArrayList<Node> children = problem.expandIDS(currNode, heuristic, newIt);
                newIt = false;
                System.out.println(children.size());
                System.out.print("New Child's depth: ");
                for (Node child : children) {
                    nodesExpanded++;
                    System.out.print(child.getDepth() + " ");
                    queue.add(child);
                }
                System.out.println();
            }

            if (queue.isEmpty()) {
                if (maxDepth < n) {
                    System.out.println("It fails here!!");
                    result = "Fail";
                } else {
                    result = "R";
                }
                break;
            }
        }
        return result; //TODO
    }

    public static String searchIDS(DeliveryProblem problem, int heuristic) {
        int n = 0;
        String path = "";
        while (true) {
            path = IDS(problem, n, heuristic);
            System.out.println(path);
            if (path.equals("R")) {
                n++;
            } else {
                break;
            }
        }
        return path;
    }
}
