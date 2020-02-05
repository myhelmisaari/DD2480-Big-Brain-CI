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

import static java.util.Collections.singleton;

/**
 Skeleton of a ContinuousIntegrationServer which acts as webhook
 See the Jetty documentation for API documentation of those classes.
*/
public class ContinuousIntegrationServer extends AbstractHandler
{
    private static ProjectConnection connection;
    public void handle(String target,
                       Request baseRequest,
                       HttpServletRequest request,
                       HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        // here you do all the continuous integration tasks
        // for example
        // 1st clone your repository
        cloneTheProject();
        // 2nd compile the code
        build();
        notifyUser();
    }

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        Server server = new Server(8083);
        server.setHandler(new ContinuousIntegrationServer());
        server.start();
        server.join();
    }

    /**
     * Clones the assessment branch from the GitHub repository.
     */
    private static void cloneTheProject() throws IOException{
        File localPath = new File("assessment/");
        FileUtils.deleteDirectory(localPath);
        localPath = new File("assessment/");
        try {
            Git.cloneRepository()
                    .setURI("https://github.com/myhelmisaari/DD2480-Big-Brain-CI.git")
                    .setDirectory(localPath)// #1
                    .setBranchesToClone(singleton("refs/heads/assessment"))
                    .setBranch("refs/heads/assessment")
                    .call();
        } catch (GitAPIException ex) {
            System.out.println("exception");
        }
    }

    private static void build(){
        connection = GradleConnector.newConnector()
                .forProjectDirectory(new File("assesment/DD2480-Big-Brain-CI")).connect();
        BuildLauncher build = connection.newBuild();
        try {
            build.run();
        }finally {
            connection.close();
        }
    }

    private static void notifyUser(){

    }
}
