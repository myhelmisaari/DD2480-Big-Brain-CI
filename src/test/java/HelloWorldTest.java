import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloWorldTest {

    @Test
    void returnOneTest() {
        System.out.println("Testing ------------------------------------");
        assertEquals(1,HelloWorld.returnOne());
    }
    @Test
    void returnOneTestFalse() {
        assertEquals(0,HelloWorld.returnOne());
    }
}