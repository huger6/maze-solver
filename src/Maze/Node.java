package Maze;

public class Node implements Comparable<Node> {
    public final int x, y;

    private int gCost; // start until now
    private int hCost; // heuristic 
    private int fCost; // total

    private Node parent;

    private boolean isWall;

    private boolean isPath;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.isWall = false;
        this.isPath = false;
    }

    public Node(int x, int y, boolean isWall) {
        this.x = x;
        this.y = y;
        this.isWall = isWall;
        this.isPath = false;
    }

    public void calculateFCost() {
        this.fCost = this.gCost + this.hCost;
    }

    @Override
    public int compareTo(Node node) {
        if (this.fCost < node.fCost) return -1;
        if (this.fCost > node.fCost) return 1;

        if (this.hCost < node.hCost) return -1;
        if (this.hCost > node.hCost) return 1;
        
        return 0;
    }

    // Setters
    public void setWall(boolean isWall) {
        this.isWall = isWall;
    }

    public void setPath(boolean isPath) {
        this.isPath = isPath;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setGCost(int newCost) {
        gCost = newCost;
    }

    public void setHCost(int newCost) {
        hCost = newCost;
    }

    public void setFCost(int newCost) {
        fCost = newCost;
    }

    // Getters
    public boolean isWall() {
        return isWall;
    }

    public boolean isPath() {
        return isPath;
    }

    public Node getParent() {
        return parent;
    }

    public int getGCost() {
        return gCost;
    }

    public int getHCost() {
        return hCost;
    }

    public int getFCost() {
        return fCost;
    }
}
