package code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeliveryPlanner {

    public static String plan(String initalState, String traffic, String strategy, boolean visualize){
        Strategy strat = null;
        switch (strategy){
            case "BF":
                strat = Strategy.BFS;
                break;
            case "DF":
                strat = Strategy.DFS;
                break;
            case "UC":
                strat = Strategy.UCS;
                break;
            case "ID":
                strat = Strategy.IDS;
                break;
            case "GR1":
                strat = Strategy.GREEDY1;
                break;
            case "GR2":
                strat = Strategy.GREEDY2;
                break;
            case "AS1":
                strat = Strategy.ASTAR1;
                break;
            case "AS2":
                strat = Strategy.ASTAR2;
                break;
            default:
                break;
        }

        return DeliverySearch.solve(initalState,traffic,strat);
    }

    public static void main(String[] args) {
        String initialState = "8;4;5;3;4,0,1,3,2,1,0,0,6,2;0,1,3,0,0,2;5,2,7,1;";
        String traffic = "0,1,0,0,2;0,2,0,1,2;0,3,0,2,2;1,0,0,0,2;1,1,0,1,2;1,1,1,0,3;1,2,0,2,1;1,2,1,1,0;1,3,0,3,3;1,3,1,2,3;2,0,1,0,3;2,1,1,1,3;2,1,2,0,2;2,2,1,2,1;2,2,2,1,1;2,3,1,3,0;2,3,2,2,0;3,0,2,0,3;3,1,2,1,3;3,1,3,0,3;3,2,2,2,3;3,2,3,1,1;3,3,2,3,3;3,3,3,2,0;4,0,3,0,3;4,1,3,1,0;4,1,4,0,0;4,2,3,2,0;4,2,4,1,0;4,3,3,3,1;4,3,4,2,2;5,0,4,0,2;5,1,4,1,1;5,1,5,0,1;5,2,4,2,1;5,2,5,1,0;5,3,4,3,2;5,3,5,2,0;6,0,5,0,3;6,1,5,1,2;6,1,6,0,1;6,2,5,2,2;6,2,6,1,2;6,3,5,3,1;6,3,6,2,2;7,0,6,0,3;7,1,6,1,0;7,1,7,0,3;7,2,6,2,3;7,2,7,1,0;7,3,6,3,2;7,3,7,2,3;";

//        String s = plan(initialState,traffic,"DF",false);
//        System.out.println(s);


        String s2 = plan(initialState,traffic,"AS1",false);
        System.out.println(s2);


    }



}
