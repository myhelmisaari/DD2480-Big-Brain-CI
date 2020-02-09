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


/**
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
*/
public class ContinuousIntegrationServer extends AbstractHandler
{
    private static final String gitHubRepoHTTPS = "https://github.com/myhelmisaari/DD2480-Big-Brain-CI.git";
    private static final String assessmentRepo = "assessmentDir/";
    private static final int port = 8083;

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
        File localPath = new File(assessmentRepo);
        FileUtils.deleteDirectory(localPath);

        // Clone the Repo
        cloneTheProject(gitHubRepoHTTPS);
        //Wait that the repo finish cloning
        try {
            TimeUnit.SECONDS.sleep(7);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Build the assessment branch
        build(assessmentRepo);
        // notify the user
        notifyUser();
    }

    public static void main(String[] args) throws Exception
    {
        Server server = new Server(port);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
    }

    /**
     * Clones the assessment branch from the GitHub repository given as argument.
     * @param gitHubHTTPS the https of the repository we want to clone
     */
    private static void cloneTheProject(String gitHubHTTPS) {
        try {
            // Execute command
            String command = "cmd /c start cmd.exe /C " +
                    "\"git clone " + gitHubHTTPS + " -b assessment --single-branch " + assessmentRepo + " \"";
            Process child = Runtime.getRuntime().exec(command);
            child.waitFor();
        } catch (IOException | InterruptedException e) {
            SlackIntegration.sendMessage("Clone failed for " + gitHubRepoHTTPS);
        }
    }


    /**
     * This method will build (compile an test) the project contained in the
     * directory given as argument
     * @param assessmentRepo the directory that contains the project we want to build
     */
    private static void build(String assessmentRepo){
        try {
            // Execute command
            String command = "cmd /c start cmd.exe /C" +

                    "\"cd " + assessmentRepo + "  && gradlew build\" " ;
            Process child = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            System.err.println("Error when building");
            SlackIntegration.sendMessage("Build failed for " + gitHubRepoHTTPS);
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
