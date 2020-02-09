import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ContinuousIntegrationServerTest {

    @Test
    void cloneTheProject_HelloWorldExists() {
        ContinuousIntegrationServer.cloneTheProject(ContinuousIntegrationServer.GITHUB_REPO_HTTPS);
        File file = new File("assessmentDir/src/main/java/HelloWorld.java");
        assertTrue(file.exists());
    }

    @Test
    void cloneTheProject_GoodByeWorldDoesNotExist() {
        ContinuousIntegrationServer.cloneTheProject(ContinuousIntegrationServer.GITHUB_REPO_HTTPS);
        File file = new File("assessmentDir/src/main/java/GoodByeWorld.java");
        assertFalse(file.exists());
    }


    @Test
    void build_ContinuousIntegrationServerExists() {
        ContinuousIntegrationServer.build(ContinuousIntegrationServer.ASSESSMENT_REPO);
        File file = new File("build/classes/java/main/ContinuousIntegrationServer.class");
        assertTrue(file.exists());
    }

    @Test
    void build_DummyServerDoesNotExist() {
        ContinuousIntegrationServer.build(ContinuousIntegrationServer.ASSESSMENT_REPO);
        File file = new File("build/classes/java/main/DummyServer.class");
        assertFalse(file.exists());
    }
}