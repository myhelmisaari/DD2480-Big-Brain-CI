import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class ContinuousIntegrationServerTest extends ContinuousIntegrationServer {

    @Test
    public void testCloneTheProject() throws IOException {
        ContinuousIntegrationServer c = new ContinuousIntegrationServerTest();
        c.cloneTheProject();
        File file = new File("assessment/src/main/java/HelloWorld.java");
        assertTrue(file.exists());
    }
}