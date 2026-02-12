import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Maze.generateMaze("prim_maze.txt", 100, 200);

        Maze maze = Maze.fromFile("prim_maze.txt");

        if (maze != null) {
            Pathfinder pf = new Pathfinder(maze);

            List<Node> paths = pf.solve();
            if (paths != null) {
                Maze mazeSolved = pf.getMazeSolved(paths);
                if (mazeSolved != null) {
                    mazeSolved.drawMaze(Paths.get("prim_maze_solved.txt"));
                }
            }

        }
    }
}