import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class ContinuousIntegrationServerTest extends ContinuousIntegrationServer {

    @Test
    public void testCloneTheProject() throws IOException {
        ContinuousIntegrationServer c = new ContinuousIntegrationServerTest();
        c.cloneTheProject();

        File file1 = new File("assessment/src/main/java/HelloWorld.java");
        assertTrue(file1.exists());

        File file2 = new File("assessment/src/main/java/GoodbyeWorld.java");
        assertFalse(file2.exists());

    }
}