package Menu;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

public class Menu extends JPanel {
    private JTextField widthField, heightField;
    private JButton generateBtn;

    public Menu(ActionListener onGenerate) {
        this.setLayout(new BorderLayout(10, 20));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        widthField = new JTextField("");
        heightField = new JTextField("");
        generateBtn = new JButton("Generate Maze");
        generateBtn.addActionListener(onGenerate);

        inputPanel.add(new JLabel("Width: ")); inputPanel.add(widthField);
        inputPanel.add(new JLabel("Height: ")); inputPanel.add(heightField);
        inputPanel.add(new JLabel("")); inputPanel.add(generateBtn);

        // Extra info
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        JLabel controls = new JLabel("Controls: ENTER to Solve Maze | ESC to return to the Menu");
        JLabel name = new JLabel("Created by huger6 :)");

        controls.setFont(new Font("Arial", Font.ITALIC, 12));
        name.setFont(new Font("Arial", Font.BOLD, 10));

        controls.setAlignmentX(Component.CENTER_ALIGNMENT);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        controls.setForeground(Color.GRAY);

        infoPanel.add(controls);
        infoPanel.add(Box.createVerticalStrut(5));
        infoPanel.add(name);

        // Add to main menu
        this.add(inputPanel, BorderLayout.CENTER);
        this.add(infoPanel, BorderLayout.SOUTH);

        this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    public int getMazeWidth() {
        try {
            String text = widthField.getText().trim();
            if (text.isEmpty()) return -1;
            return Integer.parseInt(text);
        } catch(NumberFormatException e) {
            return -1;
        }
    }

    public int getMazeHeight() {
        try {
            String text = heightField.getText().trim();
            if (text.isEmpty()) return -1;
            return Integer.parseInt(text);
        } catch(NumberFormatException e) {
            return -1;
        }
    }
}
