package Maze;

import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.concurrent.ThreadLocalRandom;

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
                    break;
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
            System.err.println("Error reading maze: " + e.getMessage());
            return null;
        }
    }

    public static void generateMaze(String filename, int width, int height) {
        try {
            Path path = Paths.get(filename);
            String w = "width=" + width;
            String h = "height=" + height;

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                // Set variables
                writer.write(w);
                writer.newLine();
                writer.write(h);
                writer.newLine();

                // Draw random maze
                Maze maze = generateMaze(width, height);
                maze.drawMaze(path);

                System.out.println("Maze generated successfully and saved to " + filename + "!");
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error generating maze: " + e.getMessage());
        }
    }

    public void drawMaze(Path path) {
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (int y = 0; y < height; y++) {
                StringBuilder line = new StringBuilder();
                for (int x = 0; x < width; x++) {
                    line.append(drawNode(x, y));
                }
                writer.write(line.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error drawing maze to file: " + e.getMessage());
        }
    }

    // Get a random maze using Prim algorithm
    public static Maze generateMaze(int width, int height) {
        try {
            Maze maze = new Maze(width, height);

            // Set all nodes as walls
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    maze.getNode(x, y).setWall(true);
                }
            }

            ArrayList<Node> wallList = new ArrayList<>();

            // Get random start point and set it as path
            Node start = maze.getNode(ThreadLocalRandom.current().nextInt(0, width), ThreadLocalRandom.current().nextInt(0, height));

            start.setWall(false);

            // End node (eventually)
            Node lastNodeAdded = start;

            // Set start neighbours
            wallList.addAll(maze.getNeighborsWalls(start));

            while (!wallList.isEmpty()) {
                // Choose random wall
                int index = ThreadLocalRandom.current().nextInt(wallList.size());
                Node wallNode = wallList.get(index);

                // If only one path neighbour, we extend the path
                if (maze.countEmptyNeighbors(wallNode) == 1) {
                    wallNode.setWall(false);
                    lastNodeAdded = wallNode; // update end 

                    // Add new neighbors
                    for (Node node : maze.getNeighborsWalls(wallNode)) {
                        if (!wallList.contains(node)) wallList.add(node);
                    }
                }

                wallList.remove(index);
            }
            
            // Set start and end
            maze.setStart(start);
            maze.setEnd(lastNodeAdded);

            return maze;
        } catch (Exception e) {
            System.err.println("Error generating random maze: " + e.getMessage());
            return null;
        }
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private String drawNode(int x, int y) {
        Node node = getNode(x, y);
        if (node != null) {
            if (node == getStart()) return "S";
            else if (node == getEnd()) return "E";
            else if (node.isWall()) return "#";
            else if (node.isPath()) return ".";
            return " ";
        } else {
            return "";
        }
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

    public List<Node> getNeighborsWalls(Node node) {
        List<Node> neighbors = new ArrayList<>();

        Node up = getNode(node.x, node.y -1);
        if (up != null && up.isWall()) {
            neighbors.add(up);
        }

        Node down = getNode(node.x, node.y + 1);
        if (down != null && down.isWall()) {
            neighbors.add(down);
        }

        Node left = getNode(node.x - 1, node.y);
        if (left != null && left.isWall()) {
            neighbors.add(left);
        }

        Node right = getNode(node.x + 1, node.y);
        if (right != null && right.isWall()) {
            neighbors.add(right);
        }

        return neighbors;
    }

    // Returns number of neighbors that are not walls
    public int countEmptyNeighbors(Node node) {
        int count = 0;

        Node up = getNode(node.x, node.y -1);
        if (up != null && !up.isWall()) {
            count += 1;
        }

        Node down = getNode(node.x, node.y + 1);
        if (down != null && !down.isWall()) {
            count += 1;
        }

        Node left = getNode(node.x - 1, node.y);
        if (left != null && !left.isWall()) {
            count += 1;
        }

        Node right = getNode(node.x + 1, node.y);
        if (right != null && !right.isWall()) {
            count += 1;
        }

        return count;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
