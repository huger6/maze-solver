import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.util.Scanner;

public class Maze {
    private final int width, height;
    private Node[][] grid;
    private Node start;
    private Node end;

    public Maze(int width, int height) {
        // Create and store maze info
        this.width = width;
        this.height = height;
        this.grid = new Node[width][height];

        // Create each Node for the maze
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j] = new Node(i, j);
            }
        }
    }

    public static Maze fromFile(String filename) {
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
        
            int w = 0, h = 0;
        
            Maze maze = null;
        
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains("width=")) {
                    w = Integer.parseInt(line.split("width=")[1]);
                } else if (line.contains("height=")) {
                    h = Integer.parseInt(line.split("height=")[1]);
        
                    if (!(w > 0 && h > 0)) {
                        scanner.close();
                        return null;
                    }
                    
                    maze = new Maze(w, h);
                }
            }
            
            int currentY = 0;
            while (scanner.hasNextLine() && currentY < h) {
                String line = scanner.nextLine();
                
                if (line.trim().isEmpty()) continue; 

                for (int x = 0; x < Math.min(w, line.length()); x++) {
                    char c = line.charAt(x);
                    Node node = maze.getNode(x, currentY);
                    if (node != null) {
                        if (c == '#') node.setWall(true);
                        else if (c == 'S') maze.setStart(node);
                        else if (c == 'E') maze.setEnd(node);
                    }
                }
                currentY++;
            }
        
            scanner.close();
            System.out.println("Maze loaded successfully!");
            return maze;
        } catch (Exception e) {
            System.out.println("Error reading maze: " + e.getMessage());
            return null;
        }
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Node getNode(int x, int y) {
        if (isValid(x, y)) {
            return grid[x][y];
        }
        return null;
    }

    public List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();

        Node up = getNode(node.x, node.y - 1);
        if (up != null && !up.isWall()) {
            neighbors.add(up);
        }

        Node down = getNode(node.x, node.y + 1);
        if (down != null && !down.isWall()) {
            neighbors.add(down);
        }

        Node left = getNode(node.x - 1, node.y);
        if (left != null && !left.isWall()) {
            neighbors.add(left);
        }

        Node right = getNode(node.x + 1, node.y);
        if (right != null && !right.isWall()) {
            neighbors.add(right);
        }

        return neighbors;
    }

    // Setters

    public void setStart(Node node) {
        start = node;
    }

    public void setEnd(Node node) {
        end = node;
    }

    // Getters

    public Node getStart() {
        return start;
    }

    public Node getEnd() {
        return end;
    }
}
