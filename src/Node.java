public class Node implements Comparable<Node> {
    public final int x, y;

    private int gCost; // start until now
    private int hCost; // heuristic 
    private int fCost; // total

    private Node parent;

    private boolean isWall;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.isWall = false;
    }

    public Node(int x, int y, boolean isWall) {
        this.x = x;
        this.y = y;
        this.isWall = isWall;
    }

    public void calculateFCost() {
        this.fCost = this.gCost + this.hCost;
    }

    @Override
    public int compareTo(Node node) {
        if (this.fCost < node.fCost) return -1;
        if (this.fCost > node.fCost) return 1;
        return 0;
    }

    // Setters
    public void setWall(boolean isWall) {
        this.isWall = isWall;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    // Getters
    public boolean isWall() {
        return isWall;
    }

    public Node getParent() {
        return parent;
    }

    public int getFCost() {
        return fCost;
    }
}
