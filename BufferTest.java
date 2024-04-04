import student.TestCase;

/**
 * this class contains test methods for testing the functionality of Buffer.
 * it extends the TestCase class for JUnit testing.
 * 
 * @author Stuti Shah & Lauren Spehlmann
 * @version 03/28/2024
 */
public class BufferTest extends TestCase {
    
    private Buffer buffer;
    
    /**
     * Initializes the buffer to use in testing
     */
    public void setUp() {
        byte[] bytes = {0x03, 0x22, 0x57, 0x09, 0x13, 0x11, 0x55, 0x28};
        buffer = new Buffer(1, bytes);
    }
    
    /**
     * Tests the accessor methods of the buffer class
     */
    public void testAccessors() {
        assertFalse(buffer.getDirty());
        assertEquals(buffer.getBlockID(), 1);
        assertEquals(buffer.getContents()[0], 0x03);
    }
    
    
    /**
     * Tests that the dirty bit fields update correctly
     */
    public void testDirty() {
        buffer.setDirty();
        assertTrue(buffer.getDirty());
    }
}