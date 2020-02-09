import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestResultsParserTests {
    @Test
    void getSomeResult() {
        createDummyTestResults();
        List<TestResultsParser.TestResult> testResults = TestResultsParser.getResults();
        assertNotNull(testResults);
        assertTrue(testResults.size() > 0);
    }

    @Test
    void noResultsWithoutBuild() {
        List<TestResultsParser.TestResult> testResults = TestResultsParser.getResults();
        assertNotNull(testResults);
        assertEquals(0, testResults.size());
    }

    @Test
    void canParseresultsXML() {
        createDummyTestResults();
        List<TestResultsParser.TestResult> testResults = TestResultsParser.getResults();
        TestResultsParser.TestResult result = testResults.get(0);

        assertEquals(result.testSuiteName, "HelloWorldTest");
        assertEquals(result.testsCount, 2);
        assertEquals(result.skippedCount, 0);
        assertEquals(result.failureCount, 1);
        assertEquals(result.errorCount, 0);
        assertEquals(result.timestamp, "2020-02-03T12:44:44");
        assertEquals(result.hostName, "n168-p9.eduroam.kth.se");
        assertEquals(result.time, "0.012");
    }

    /**
     * Setup a basic test fixture with test results in an XML file.
     */
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
