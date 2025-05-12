import javax.swing.*;
import java.awt.*;
import java.net.URI;

public class GithubButton extends JButton {
    private static final String GITHUB_URL = "https://github.com/adryserage";

    public GithubButton() {
        super("Visit My GitHub");
        setFont(new Font("Arial", Font.PLAIN, 14));
        addActionListener(_ -> openGithubProfile());
    }

    private void openGithubProfile() {
        try {
            Desktop.getDesktop().browse(new URI(GITHUB_URL));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Error opening GitHub: " + ex.getMessage(),
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
