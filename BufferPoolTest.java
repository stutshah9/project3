import java.io.FileNotFoundException;
import student.TestCase;

/**
 * this class contains test methods for testing the functionality of BufferPool.
 * it extends the TestCase class for JUnit testing.
 * 
 * @author Stuti Shah & Lauren Spehlmann
 * @version 04/03/2024
 */
public class BufferPoolTest extends TestCase {

    private BufferPool bufferPool;

    /**
     * Initializes the buffer pool to use in testing
     */
    public void setUp() {
        try {
            bufferPool = new BufferPool("input.bin", 3);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Tests the insert method by verifying that the disk writes count was
     * incremented
     */
    public void testInsert() {
        bufferPool.insert(new byte[4096], 0, 0);
        assertEquals(bufferPool.getDiskWrites(), 1);
    }

    /**
     * Tests getBytes() when the buffer is not in the pool, is in the buffer,
     * and when the pool has to eject a buffer to make space for the incoming
     * block
     */
    public void testGetBytes() {
        byte[] space = new byte[4096];
        bufferPool.getbytes(space, 4096, 0);
        assertEquals(bufferPool.getDiskReads(), 1);
        bufferPool.getbytes(space, 4096, 0);
        assertEquals(bufferPool.getCacheHits(), 1);
        bufferPool.getbytes(space, 4096, 1);
        bufferPool.getBufferList().get(1).updateBuffer(new byte[4096]);
        assertTrue(bufferPool.getBufferList().get(1).getDirty());
        bufferPool.getbytes(space, 4096, 2);
        bufferPool.getbytes(space, 4096, 3);
        assertEquals(bufferPool.getDiskReads(), 4);
        assertEquals(bufferPool.getDiskWrites(), 1);
        bufferPool.getbytes(space, 4096, 4);
        assertEquals(bufferPool.getDiskWrites(), 1);
    }
}
