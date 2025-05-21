import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Font;

public class MainWindow extends JFrame {
    private final GithubButton githubButton;
    private final ReadmeDisplayPanel readmePanel;

    public MainWindow() {
        super("About Me");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Name Panel
        JPanel namePanel = new JPanel();
        JLabel nameLabel = new JLabel("Adryan Serage", SwingConstants.CENTER);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 24));
        namePanel.add(nameLabel);

        // GitHub Button
        githubButton = new GithubButton();

        // Top Panel Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(namePanel, BorderLayout.NORTH);
        topPanel.add(githubButton, BorderLayout.CENTER);
        
        // README Panel
        readmePanel = new ReadmeDisplayPanel();

        // Main Layout
        add(topPanel, BorderLayout.NORTH);
        add(readmePanel, BorderLayout.CENTER);

        setSize(600, 400);
        setLocationRelativeTo(null);
    }

    public void display() {
        setVisible(true);
    }
}
