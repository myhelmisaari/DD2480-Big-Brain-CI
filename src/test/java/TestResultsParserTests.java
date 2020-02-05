import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestResultsParserTests {
    @Test
    void getSomeResult() {
        createDummyTestResults();
        List<TestResultsParser.TestResult> testResults = TestResultsParser.getResults();
        assertNotNull(testResults);
        assertTrue(testResults.size() > 0);
    }

    private void createDummyTestResults() {
        String dummyXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<testsuite name=\"HelloWorldTest\" tests=\"2\" skipped=\"0\" failures=\"1\" errors=\"0\" timestamp=\"2020-02-03T12:44:44\" hostname=\"n168-p9.eduroam.kth.se\" time=\"0.012\">\n" +
                "</testsuite>\n";
        try {
            File dummyFile = new File(TestResultsParser.getResultsFolder() + "/TEST-dummy.xml");
            dummyFile.createNewFile();
            FileWriter writer = new FileWriter(dummyFile);
            writer.write(dummyXML);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
