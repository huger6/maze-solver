package App;

import javax.swing.*;
import java.util.List;

import Menu.Menu;
import Pathfinder.Pathfinder;
import Maze.Maze;
import Maze.Node;
import Display.MazeDisplay;

public class App {
    private Menu menu;
    private JFrame frame;

    public App() {
        frame = new JFrame("Maze Solver");
        menu = new Menu(e -> initGame());

        frame.add(menu);
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void initGame() {
        int width = menu.getMazeWidth();
        int height = menu.getMazeHeight();

        System.out.println("Generating maze of " + width + "x" + height);

        Maze maze = Maze.generateMaze(width, height);

        if (maze != null) {
            Pathfinder pf = new Pathfinder(maze);
            System.out.println("Solving maze...");
            List<Node> paths = pf.solve();
            System.out.println("Maze solved!");

            if (paths != null) {
                frame.remove(menu);

                MazeDisplay display = new MazeDisplay(maze, paths, () -> showMenu());

                frame.add(display);

                frame.pack();
                frame.revalidate();
                frame.repaint();
                frame.setLocationRelativeTo(null);

                display.requestFocusInWindow();
            }

        }
    }
    private void showMenu() {
        frame.getContentPane().removeAll();

        frame.add(menu);
        frame.pack();
        frame.revalidate();
        frame.repaint();
        frame.setLocationRelativeTo(null);
    }
}
