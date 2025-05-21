import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReadmeFetcher {
    public static String fetchReadmeContent() throws IOException {
        String urlStr = "https://raw.githubusercontent.com/adryserage/adryserage/refs/heads/main/README.md";
        URL url = URI.create(urlStr).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } finally {
            conn.disconnect();
        }
        return content.toString();
    }
}
