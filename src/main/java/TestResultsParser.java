import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * This class can parse the gradle test results generated in build/test-results/test/.
 */
public class TestResultsParser {
    private static final File resultsFolder = new File(ContinuousIntegrationServer.ASSESSMENT_REPO + "build/test-results/test/");
    private static List<TestResult> parsedTestResults;

    /**
     * @return the results of the tests.
     */
    public static List<TestResult> getResults() {
        parsedTestResults = new LinkedList<>();

        // find the right xml file(s)
        try {
            for (File file : resultsFolder.listFiles((dir, name) -> name.endsWith(".xml") && name.startsWith("TEST-"))) {
                parseResultFile(file);
            }
        } catch (NullPointerException e) {
            // will be empty if no matching files were found
            return  parsedTestResults;
        }

        return parsedTestResults;
    }

    public static File getResultsFolder() { return resultsFolder; }

    /**
     * Parse a single test results XML file.
     * @param resultsFile the file to parse
     */
    private static void parseResultFile(File resultsFile) {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(resultsFile);
            Node testSuiteNode = document.getFirstChild();

            parsedTestResults.add(new TestResult(
                    getAttributeStringValue(testSuiteNode, "name"),
                    getAttributeStringValue(testSuiteNode, "hostname"),
                    getAttributeIntValue(testSuiteNode, "tests"),
                    getAttributeIntValue(testSuiteNode, "skipped"),
                    getAttributeIntValue(testSuiteNode, "errors"),
                    getAttributeIntValue(testSuiteNode, "failures"),
                    getAttributeStringValue(testSuiteNode, "time"),
                    getAttributeStringValue(testSuiteNode, "timestamp")
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getAttributeStringValue(Node node, String attribute) {
        return node.getAttributes().getNamedItem(attribute).getTextContent();
    }

    private static int getAttributeIntValue(Node node, String attribute) {
        return Integer.parseInt(getAttributeStringValue(node, attribute));
    }

    /**
     * A TestResult is a model for the data generated after running tests in gradle.
     */
    static class TestResult {
        protected final String testSuiteName;
        protected final String hostName;
        protected final int testsCount;
        protected final int skippedCount;
        protected final int errorCount;
        protected final int failureCount;
        protected final String time;
        protected final String timestamp;

        public TestResult(String testSuiteName, String hostName, int testsCount, int skippedCount, int errorCount, int failureCount, String time, String timestamp) {
            this.testSuiteName = testSuiteName;
            this.hostName = hostName;
            this.testsCount = testsCount;
            this.skippedCount = skippedCount;
            this.errorCount = errorCount;
            this.failureCount = failureCount;
            this.time = time;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "Results{" +
                    "testSuiteName='" + testSuiteName + '\'' +
                    ", hostName='" + hostName + '\'' +
                    ", testsCount=" + testsCount +
                    ", skippedCount=" + skippedCount +
                    ", errorCount=" + errorCount +
                    ", failureCount=" + failureCount +
                    ", time='" + time + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    '}';
        }
    }
}
