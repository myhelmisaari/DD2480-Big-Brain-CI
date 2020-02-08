import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ContinuousIntegrationServerTest {

    @Test
    void cloneTheProject() {
        ContinuousIntegrationServer.cloneTheProject(ContinuousIntegrationServer.gitHubRepoHTTPS);
        File file1 = new File("assessmentDir/src/main/java/HelloWorld.java");
        assertTrue(file1.exists());
        File file2 = new File("assessment/src/main/java/GoodbyeWorld.java");
        assertFalse(file2.exists());
    }


    @Test
    void build() {
        ContinuousIntegrationServer.build(ContinuousIntegrationServer.assessmentRepo);
        File file1 = new File("build/classes/java/main/ContinuousIntegrationServer.class");
        File file2 = new File("build/classes/java/main/SlackIntegration.class");
        File file3 = new File("build/classes/java/main/TestResultsParser.class");
        assertTrue(file1.exists() && file2.exists() && file3.exists());
    }
}