public class Main {
    public static void main(String[] args) {
        Maze maze = Maze.fromFile("maze.txt");

        if (maze != null) {
            System.out.println("Maze loaded");
        }

        Maze.generateMaze("prim_maze.txt", 30, 20);
    }
}