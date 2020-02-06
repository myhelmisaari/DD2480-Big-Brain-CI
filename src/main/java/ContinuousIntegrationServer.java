import org.apache.commons.io.FileUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;


import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Collections.singleton;

/**
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
*/
public class ContinuousIntegrationServer extends AbstractHandler
{

    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        cloneTheProject("https://github.com/myhelmisaari/DD2480-Big-Brain-CI.git");
        // 2nd compile the code
        build();

        notifyUser();
    }

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
//
//        Server server = new Server(8083);
//        server.setHandler(new ContinuousIntegrationServer());
//        server.start();
//        server.join();
        cloneTheProject("https://github.com/myhelmisaari/DD2480-Big-Brain-CI.git");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        build();
    }

    /**
     * Clones the assessment branch from the GitHub repository given as argument.
     * @param gitHubHTTPS the https of the repository we want to clone
     * @return the file that contains the file of the GitHub project
     * @throws IOException
     */
    private static void cloneTheProject(String gitHubHTTPS) {
        try {
            // Execute command
            String command = "cmd /c start cmd.exe /C " +
                    "\"git clone "+gitHubHTTPS+" -b assessment --single-branch assessmentDir\"";
            Process child = Runtime.getRuntime().exec(command);
            child.waitFor();
        } catch (IOException | InterruptedException e) {
        }
    }


    /**
     * This method will build (compile an test) the project contained in the file given
     *  as argument
     */
    private static void build(){
        try {
            // Execute command
            String command = "cmd /c start cmd.exe /K" +
                    "\"cd assessmentDir && gradlew build\" " ;
            Process child = Runtime.getRuntime().exec(command);
        } catch (IOException e) {
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
