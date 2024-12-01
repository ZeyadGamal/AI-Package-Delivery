package code;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {
    private Strategy strategy;

    public NodeComparator(Strategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public int compare(Node n1, Node n2) {
        switch (strategy) {
            case BFS:
                if (n1.getDepth() > n2.getDepth()) {
                    return 1;
                }
                else if (n1.getDepth() < n2.getDepth()) {
                    return -1;
                }
                else {
                    return 0;
                }
            case DFS, IDS:
                if (n1.getDepth() > n2.getDepth()) {
                    return -1;
                }
                else if (n1.getDepth() < n2.getDepth()) {
                    return 1;
                }
                else {
                    return 0;
                }
            case UCS:
                if (n1.getCost() > n2.getCost()) {
                    return 1;
                }
                else if (n1.getCost() < n2.getCost()) {
                    return -1;
                }
                else {
                    return 0;
                }

            case GREEDY1, GREEDY2:
                if (n1.getHeuristic() > n2.getHeuristic()) {
                    return 1;
                }
                else if (n1.getHeuristic() < n2.getHeuristic()) {
                    return -1;
                }
                else {
                    return 0;
                }
            case ASTAR1, ASTAR2:
                if (n1.getEvaluation() > n2.getEvaluation()) {
                    return 1;
                }
                else if (n1.getEvaluation() < n2.getEvaluation()) {
                    return -1;
                }
                else {
                    return 0;
                }
            default:
                return 0;
        }
    }
}
