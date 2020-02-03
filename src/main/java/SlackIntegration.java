import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * This class provides functionality for sending messages to a Slack channel, through a Slack app
 * configured at https://api.slack.com/apps/ATFSMNS4Q.
 */
public class SlackIntegration {
    private static final String bigBrainSlackHookURL = "https://hooks.slack.com/services/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";

    /**
     * Send an HTTP POST request.
     * @param URL the destination of the request.
     * @param requestBody what JSON you want to send.
     * @return true if successful, false otherwise.
     */
    private static boolean sendPOST(String URL, JSONObject requestBody) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(bigBrainSlackHookURL))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .header("Content-type", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
            return response.statusCode() == 200;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Send a hello message in the Slack channel, for testing.
     * @return true if successful, false otherwise.
     */
    protected static boolean sendHello() {
        JSONObject requestBody = new JSONObject().put("text", ":clap: Hello! This is a test :)");
        return sendPOST(bigBrainSlackHookURL, requestBody);
    }
}
