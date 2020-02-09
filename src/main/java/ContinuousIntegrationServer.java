import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class ContinuousIntegrationServer extends AbstractHandler
{

    protected static final String GITHUB_REPO_HTTPS = "https://github.com/myhelmisaari/DD2480-Big-Brain-CI.git";
    protected static final String ASSESSMENT_REPO = "assessmentDir/";
    private static final int PORT = 8083;

    /**
     * Handles the HTTP requests that are sent to the CI-server.
     * Provides the main logic of the CI-server.
     * @param target  the target of the request.
     * @param baseRequest the original unwrapped request object.
     * @param request the request either as the Request object or a wrapper of that request.
     * @param response the response as the Response object or a wrapper of that request.
     */
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        // Delete the repo if it exists
        File localPath = new File(ASSESSMENT_REPO);
        FileUtils.deleteDirectory(localPath);

        // Clone the Repo
        cloneTheProject(GITHUB_REPO_HTTPS);
        //Wait that the repo finish cloning
        try {
            TimeUnit.SECONDS.sleep(7);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Build the assessment branch
        build(ASSESSMENT_REPO);
        // notify the user
        notifyUser();
    }

    public static void main(String[] args) throws Exception
    {
        Server server = new Server(PORT);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
    }

    /**
     * Clones the assessment branch from the GitHub repository given as argument.
     * @param gitHubHTTPS the https of the repository we want to clone
     */
    protected static void cloneTheProject(String gitHubHTTPS) {
        try {
            // Execute command
            String command = "cmd /c start cmd.exe /C " +
                    "\"git clone " + gitHubHTTPS + " -b assessment --single-branch " + ASSESSMENT_REPO + " \"";
            Process child = Runtime.getRuntime().exec(command);
            child.waitFor();
        } catch (IOException | InterruptedException e) {
            SlackIntegration.sendMessage("Clone failed for " + GITHUB_REPO_HTTPS);
        }
    }


    /**
     * This method will build (compile an test) the project contained in the
     * directory given as argument
     * @param ASSESSMENT_REPO the directory that contains the project we want to build
     */
    protected static void build(String ASSESSMENT_REPO){
        try {
            // Execute command
            String command = "cmd /c start cmd.exe /C" +
                    "\"cd " + ASSESSMENT_REPO + "  && gradlew build\" " ;
            Process child = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            System.err.println("Error when building");
            SlackIntegration.sendMessage("Build failed for " + GITHUB_REPO_HTTPS);
        }
    }

    /**
     * This functions notify the user of what append when building.
     * It will notify the user sending him a message on Slack with information on
     *      testSuiteName on the hostName, on the number of tests, on skippedCount
     *      on the number of errors, on the number of failure, on the time taken to run the tests
     *      and on the timestamp
     */
    private static void notifyUser() {
        List<TestResultsParser.TestResult> testResults = TestResultsParser.getResults();

        String message = testResults.toString();
        SlackIntegration.sendMessage(message);
    }
}
