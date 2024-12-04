package code;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class GenericSearch {

    static int pathCost = 0;
    static String plan = "";
    static int nodesExpanded = 0;

    public GenericSearch() {}

     public static String search(DeliveryProblem problem, Strategy strategy, int heuristic) {


        nodesExpanded = 0;

         Comparator<Node> comparator = new NodeComparator(strategy);
         PriorityQueue<Node> queue = new PriorityQueue<Node>(comparator);

         State initialState = problem.getInitialState();

         Node initialNode = new Node(problem.getInitialState(), null, 0, 0, null, 0);

         queue.add(initialNode);


        if (queue.isEmpty()){
            return "Failure";
         }
        while (!queue.isEmpty()){
            Node currNode = queue.poll();

            //System.out.println("currNode: " + currNode.getState().getX() + " " + currNode.getState().getY());

            if (problem.isGoal(currNode.getState())){

                pathCost = currNode.getCost();
                plan = "";
                while (currNode.getParent() != null){
                    //System.out.print("("+currNode.getState().getX()+","+currNode.getState().getY()+") ");
                    plan = currNode.getAction().name() + ", " + plan;
                    currNode = currNode.getParent();
                }
                return plan + ";" + pathCost + ";" + nodesExpanded;
            }

            ArrayList<Node> children =problem.expand(currNode, heuristic);

           // System.out.println("children: " + children);

            for (Node child : children){
                nodesExpanded++;
                queue.add(child);
            }

        }
        return "NULL"; //TODO
     }
}
