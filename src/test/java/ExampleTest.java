import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExampleTest {

    @Test
    public void test() {
        Example example = new Example();
        assertEquals(3, example.calcula(1L, 2L));
    }
}
