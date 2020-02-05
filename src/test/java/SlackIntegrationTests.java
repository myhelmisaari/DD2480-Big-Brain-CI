import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SlackIntegrationTests {

    @Test
    void canSendHello() {
        boolean success = SlackIntegration.sendHello();
        assertTrue(success);
    }
}
