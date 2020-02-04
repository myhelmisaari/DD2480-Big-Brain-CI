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
    // needs to be base64 since Slack will find it as publicly accessible otherwise and disable it
    private static final String bigBrainSlackHookURLBase64 = "aHR0cHM6Ly9ob29rcy5zbGFjay5jb20vc2VydmljZXMvVFNaOE41TkJZL0JUSjNRRjg3UC92TzlwTnIwa3lNZHJXbTVNbmprQjNLeWo=";

    /**
     * Send an HTTP POST request.
     * @param URL the destination of the request, base64 encoded.
     * @param requestBody what JSON you want to send.
     * @return true if successful, false otherwise.
     */
    private static boolean sendPOST(String URL, JSONObject requestBody) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(decodeBase64(URL)))
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

    private static String decodeBase64(String encoded) {
        byte[] decodedBytes = java.util.Base64.getDecoder().decode(bigBrainSlackHookURLBase64);
        return new String(decodedBytes);
    }

    /**
     * Send a hello message in the Slack channel, for testing.
     * @return true if successful, false otherwise.
     */
    protected static boolean sendHello() {
        JSONObject requestBody = new JSONObject().put("text", ":clap: Hello! This is a test :)");
        return sendPOST(bigBrainSlackHookURLBase64, requestBody);
    }
}
