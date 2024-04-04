import student.TestCase;

/**
 * This class contains the tests for the quicksort methods
 *
 * @author Stuti Shah & Lauren Spehlmann
 * @version 3/27/2024
 */
public class sortTest extends TestCase{

    private int[] array = {2, 10, 25, 34, 14};

    /**
     * Sets up the tests that follow, used for instantiation
     */
    public void setUp() {

    }

    public void testQuicksort() {
        systemOut().clearHistory();
        sort.quicksort(array, 0, array.length - 1);
        sort.print(array);
        String print1 = systemOut().getHistory();
        systemOut().clearHistory();
        int[] expected = {2, 10, 14, 25, 34};
        sort.print(expected);
        String print2 = systemOut().getHistory();
        assertEquals(print1, print2);
    }
}
