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
        //case1
        String initialState = "5;3;2;1;3,2,3,0;1,2;2,0,1,1;";
        String traffic = "0,1,0,0,3;0,2,0,1,0;1,0,0,0,3;1,1,0,1,1;1,1,1,0,3;1,2,0,2,0;1,2,1,1,1;2,0,1,0,1;2,1,1,1,0;2,1,2,0,1;2,2,1,2,0;2,2,2,1,0;3,0,2,0,3;3,1,2,1,2;3,1,3,0,3;3,2,2,2,0;3,2,3,1,2;4,0,3,0,0;4,1,3,1,0;4,1,4,0,2;4,2,3,2,1;4,2,4,1,2;";



        //case3
        //String initialState = "7;8;3;2;2,1,4,2,1,4;0,3,0,7;0,2,1,1;";
        //String traffic = "0,1,0,0,2;0,2,0,1,1;0,3,0,2,3;0,4,0,3,1;0,5,0,4,2;0,6,0,5,0;0,7,0,6,3;1,0,0,0,1;1,1,0,1,2;1,1,1,0,2;1,2,0,2,2;1,2,1,1,1;1,3,0,3,0;1,3,1,2,3;1,4,0,4,3;1,4,1,3,3;1,5,0,5,0;1,5,1,4,0;1,6,0,6,1;1,6,1,5,0;1,7,0,7,1;1,7,1,6,2;2,0,1,0,1;2,1,1,1,2;2,1,2,0,0;2,2,1,2,0;2,2,2,1,3;2,3,1,3,0;2,3,2,2,3;2,4,1,4,2;2,4,2,3,2;2,5,1,5,3;2,5,2,4,1;2,6,1,6,3;2,6,2,5,2;2,7,1,7,1;2,7,2,6,2;3,0,2,0,3;3,1,2,1,1;3,1,3,0,1;3,2,2,2,0;3,2,3,1,1;3,3,2,3,2;3,3,3,2,2;3,4,2,4,0;3,4,3,3,1;3,5,2,5,3;3,5,3,4,3;3,6,2,6,2;3,6,3,5,0;3,7,2,7,1;3,7,3,6,0;4,0,3,0,0;4,1,3,1,3;4,1,4,0,3;4,2,3,2,1;4,2,4,1,0;4,3,3,3,1;4,3,4,2,3;4,4,3,4,2;4,4,4,3,3;4,5,3,5,0;4,5,4,4,2;4,6,3,6,0;4,6,4,5,1;4,7,3,7,0;4,7,4,6,1;5,0,4,0,3;5,1,4,1,0;5,1,5,0,2;5,2,4,2,1;5,2,5,1,3;5,3,4,3,1;5,3,5,2,3;5,4,4,4,2;5,4,5,3,3;5,5,4,5,2;5,5,5,4,0;5,6,4,6,0;5,6,5,5,3;5,7,4,7,1;5,7,5,6,2;6,0,5,0,2;6,1,5,1,2;6,1,6,0,2;6,2,5,2,2;6,2,6,1,1;6,3,5,3,0;6,3,6,2,0;6,4,5,4,0;6,4,6,3,0;6,5,5,5,3;6,5,6,4,1;6,6,5,6,0;6,6,6,5,0;6,7,5,7,2;6,7,6,6,1;";

//        String s = plan(initialState,traffic,"DF",false);
//        System.out.println(s);


        String s2 = plan(initialState,traffic,"AS2",true);
        System.out.println(s2);


    }



}
