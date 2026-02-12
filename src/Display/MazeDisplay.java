package Display;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import Maze.Maze;
import Maze.Node;

public class MazeDisplay extends JPanel {
    private Maze maze;
    private List<Node> fullPath; 
    private int currentStep = 0;
    private int cellSize = 0;

    private Runnable onExit;

    public MazeDisplay(Maze maze, List<Node> fullPath, Runnable onExit) {
        this.maze = maze;
        this.fullPath = fullPath;
        this.onExit = onExit;

        // Set cellzile based on the screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        // 20% margin
        double maxW = screenSize.getWidth() * 0.9;
        double maxH = screenSize.getHeight() * 0.9;

        // Make sure maze fits
        int cellW = (int) (maxW / maze.getWidth());
        int cellH = (int) (maxH / maze.getHeight());

        this.cellSize = Math.min(cellW, cellH);

        if (this.cellSize < 1) this.cellSize = 1;

        setPreferredSize(new Dimension(maze.getWidth() * cellSize, maze.getHeight() * cellSize));

        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    showSolution();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    MazeDisplay.this.onExit.run();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Clear panel
        
        for (int y = 0; y < maze.getHeight(); y++) {
            for (int x = 0; x < maze.getWidth(); x++) {
                Node node = maze.getNode(x, y);

                if (node.isWall()) g.setColor(Color.DARK_GRAY);
                else if (maze.getStart() == node) g.setColor(Color.GREEN);
                else if (maze.getEnd() == node) g.setColor(Color.RED);
                else if (node.isPath()) g.setColor(Color.YELLOW);
                else g.setColor(Color.LIGHT_GRAY); // Normal path

                g.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);

                g.setColor(Color.BLACK);
                g.drawRect(x * cellSize, y * cellSize, cellSize, cellSize);
            }
        }
    }

    private void showSolution() {
        Timer timer = new Timer(50, e -> {
            if (currentStep < fullPath.size()) {
                fullPath.get(currentStep).setPath(true);
                currentStep++;
                repaint(); // draw again
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }
}