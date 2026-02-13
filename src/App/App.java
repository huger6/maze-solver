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
    private boolean firstBoot;

    public App() {
        this.firstBoot = true;
        frame = new JFrame("Maze Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menu = new Menu(e -> initGame());
        
        showMenu();

        frame.setVisible(true);
    }

    public void initGame() {
        // Fix early warning
        if (firstBoot) {
            firstBoot = false;
            return;
        }

        int width = menu.getMazeWidth();
        int height = menu.getMazeHeight();

        if (width < 5 || height < 5) {
            JOptionPane.showMessageDialog(frame,
                "Please insert valid dimensions (minimum: 5).",
                "Invalid input",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (width > 700 || height > 700) {
            JOptionPane.showMessageDialog(frame,
                "Dimensions above 700 might not be visible or very hard to spot!",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
        }

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
