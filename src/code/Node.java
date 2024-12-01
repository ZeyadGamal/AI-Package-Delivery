package code;

public class Node {
    private State state;
    private Node Parent;
    private int cost;
    private int depth;
    private Action action; //Action taken by parent to reach this node
    private int heuristic;

    public Node(State state, Node parent, int cost, int depth, Action action, int heuristic) {
        this.state = state;
        this.Parent = parent;
        this.cost = cost;
        this.depth = depth;
        this.action = action;
        this.heuristic = heuristic;
    }

    public Node getParent() {
        return Parent;
    }

    public State getState() {
        return state;
    }

    public int getCost() {
        return cost;
    }

    public int getDepth() {
        return depth;
    }

    public Action getAction() {
        return action;
    }
    public int getHeuristic() {
        return heuristic;
    }

    public int getEvaluation(){
        return heuristic + cost;
    }
}

