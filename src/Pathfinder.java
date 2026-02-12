import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Pathfinder {
    private Maze maze;

    public Pathfinder(Maze maze) {
        this.maze = maze;
    }

    public List<Node> solve() {
        try {
            PriorityQueue<Node> openList = new PriorityQueue<>(); // Interesting nodes to visit based on fCost
            HashSet<Node> closedSet =  new HashSet<>(); // Visited nodes
    
            // Reset costs and parent
            resetNodeValues();
    
            Node start = maze.getStart();
            Node end = maze.getEnd();

            if (start == null || end == null) {
                System.err.println("Start/End of the maze is not defined");
                return null;
            } 
            
            start.setGCost(0);
            start.setHCost(calculateHeuristic(start, end));
            start.calculateFCost();
    
            openList.add(start);

    
            while (!openList.isEmpty()) {
                Node current = openList.poll(); // < fCost
    
                if (current == end) return reconstructPath(current);
    
                closedSet.add(current);
    
                for (Node neighbor : maze.getNeighbors(current)) {
                    if (closedSet.contains(neighbor)) continue;
                    
                    int newGCost = current.getGCost() + 1;
    
                    if (newGCost < neighbor.getGCost() || !openList.contains(neighbor)) {
                        neighbor.setParent(current);
                        neighbor.setGCost(newGCost);
                        neighbor.setHCost(calculateHeuristic(neighbor, end));
                        neighbor.setFCost(neighbor.getGCost() + neighbor.getHCost());
    
                        if (openList.contains(neighbor)) {
                            openList.remove(neighbor);
                        }
                        openList.add(neighbor);
                    }
    
                }
            }
    
            return null;
        } catch(Exception e) {
            System.err.println("Error solving maze: " + e.getMessage());
            return null;
        }
    }

    public Maze getMazeSolved(List<Node> path) {
        int h = maze.getHeight();
        int w = maze.getWidth();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Node node = maze.getNode(x, y);
                if (path.contains(node)) {
                    node.setPath(true);
                }
            }
        }

        return maze;
    }

    private int calculateHeuristic(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    private List<Node> reconstructPath(Node current) {
        LinkedList<Node> path = new LinkedList<>();

        while (current != null) {
            path.addFirst(current);
            current = current.getParent();
        }

        return path;
    }

    private void resetNodeValues() {
        int h = maze.getHeight();
        int w = maze.getWidth();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                Node node = maze.getNode(x, y);
                node.setGCost(1000000000);

                node.setHCost(0);
                node.setFCost(0);
                node.setParent(null);
            }
        }
    }
}
