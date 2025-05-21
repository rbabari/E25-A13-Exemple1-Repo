import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import java.awt.Font;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.HyperlinkEvent;
import java.awt.Desktop;
import java.net.URI;
import java.util.Arrays;

public class ReadmeDisplayPanel extends JScrollPane {
    private final JEditorPane readmeArea;

    public ReadmeDisplayPanel() {
        readmeArea = new JEditorPane();
        readmeArea.setEditable(false);
        readmeArea.setContentType("text/html");
        readmeArea.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        readmeArea.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Add hyperlink listener
        readmeArea.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                try {
                    Desktop.getDesktop().browse(e.getURL().toURI());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error opening link: " + ex.getMessage());
                }
            }
        });
        
        setViewportView(readmeArea);
        loadReadmeContent();
    }

    private void loadReadmeContent() {
        try {
            String markdownContent = ReadmeFetcher.fetchReadmeContent();
            String htmlContent = convertMarkdownToHtml(markdownContent);
            readmeArea.setText(htmlContent);
        } catch (IOException e) {
            readmeArea.setText("<html><body><p style='color: red'>Error fetching README: " + e.getMessage() + "</p></body></html>");
        }
    }

    private String convertMarkdownToHtml(String markdown) {
        StringBuilder html = new StringBuilder("<html><body style='font-family: Arial; padding: 20px;'>\n");
        
        // Split into lines and process each line
        String[] lines = markdown.split("\n");
        boolean inList = false;
        
        for (String line : lines) {
            line = line.trim();
            
            // Handle existing <img> tags
            if (line.startsWith("<img")) {
                html.append(line).append("<br>\n");
                continue;
            }
            
            // Handle markdown images ![alt](/path)
            if (line.matches("!\\[.*?\\]\\(.*?\\)")) {
                String alt = line.replaceAll("!\\[(.*?)\\]\\(.*?\\)", "$1");
                String src = line.replaceAll("!\\[.*?\\]\\((.*?)\\)", "$1");
                html.append(String.format("<img src=\"%s\" alt=\"%s\" style=\"max-width: 100%%; height: auto;\">\n", src, alt));
                continue;
            }
            
            // Handle image source lines
            if (line.startsWith("src=")) {
                String src = line.substring(4).replaceAll("\"", "");
                String nextLine = "";
                if (lines.length > Arrays.asList(lines).indexOf(line.trim()) + 1) {
                    nextLine = lines[Arrays.asList(lines).indexOf(line.trim()) + 1].trim();
                }
                if (nextLine.startsWith("alt=")) {
                    String alt = nextLine.substring(4).replaceAll("\"", "");
                    String height = nextLine.contains("height=\"") ? 
                        nextLine.replaceAll(".*height=\"(\\d+)\".*", "$1") : "30";
                    String width = nextLine.contains("width=\"") ? 
                        nextLine.replaceAll(".*width=\"(\\d+)\".*", "$1") : "40";
                    html.append(String.format("<img src=\"%s\" alt=\"%s\" height=\"%s\" width=\"%s\" style=\"vertical-align: middle;\">\n", 
                        src, alt, height, width));
                    continue;
                }
            }
            
            // Skip alt lines as they're handled with src
            if (line.startsWith("alt=")) continue;
            
            // Headers
            if (line.startsWith("# ")) {
                html.append("<h1>").append(line.substring(2)).append("</h1>\n");
            } else if (line.startsWith("## ")) {
                html.append("<h2>").append(line.substring(3)).append("</h2>\n");
            }
            // List items
            else if (line.startsWith("- ")) {
                if (!inList) {
                    html.append("<ul style='list-style-type: disc;'>\n");
                    inList = true;
                }
                String item = line.substring(2);
                // Convert markdown links to HTML
                item = item.replaceAll("\\[(.*?)\\]\\((.*?)\\)", "<a href=\"$2\">$1</a>");
                html.append("<li>").append(item).append("</li>\n");
            }
            // End list if line is not a list item
            else {
                if (inList) {
                    html.append("</ul>\n");
                    inList = false;
                }
                // Empty line
                if (line.isEmpty()) {
                    html.append("<br>\n");
                }
                // Regular text
                else {
                    html.append("<p>").append(line).append("</p>\n");
                }
            }
        }
        
        // Close any open list
        if (inList) {
            html.append("</ul>\n");
        }
        
        html.append("</body></html>");
        return html.toString();
    }
}
