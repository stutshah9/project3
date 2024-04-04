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
     * Tests the insert method by verifying that the disk reads count was
     * incremented only when the block was not in the pool
     */
    public void testInsert() {
        bufferPool.insert(new byte[4], 0);
        assertEquals(bufferPool.getDiskReads(), 1);
        bufferPool.insert(new byte[4], 0);
        assertEquals(bufferPool.getDiskReads(), 1);
        assertEquals(bufferPool.getCacheHits(), 1);
        bufferPool.insert(new byte[4], 1020);
        bufferPool.insert(new byte[4], 2049);
        bufferPool.insert(new byte[4], 3073);
        bufferPool.insert(new byte[4], 5000);
        bufferPool.insert(new byte[4], 6500);
        assertEquals(bufferPool.getBufferList().size(), 3);
    }

    /**
     * Tests getBytes() when the buffer is not in the pool, is in the buffer,
     * and when the pool has to eject a buffer to make space for the incoming
     * block
     */
    public void testGetBytes() {
        byte[] space = new byte[4];
        bufferPool.getbytes(space, 0);
        assertEquals(bufferPool.getDiskReads(), 1);
        bufferPool.getbytes(space, 0);
        assertEquals(bufferPool.getCacheHits(), 1);
        bufferPool.getbytes(space, 1020);
        bufferPool.getbytes(space, 2049);
        bufferPool.getbytes(space, 3073);
        assertEquals(bufferPool.getDiskReads(), 3);
        assertEquals(bufferPool.getDiskWrites(), 0);
        bufferPool.getbytes(space, 4097);
        bufferPool.getbytes(new byte[4], 5000);
        bufferPool.getbytes(new byte[4], 6500);
        assertEquals(bufferPool.getDiskWrites(), 0);
    }
    
    /**
     * Tests writing back the remainder of the pool so that it only outputs to 
     * the disk when necessary
     */
    public void testWritePool() {
        bufferPool.insert(new byte[4], 1020);
        bufferPool.insert(new byte[4], 2049);
        bufferPool.getbytes(new byte[4], 3073);
        bufferPool.writePool();
        assertEquals(bufferPool.getDiskWrites(), 2);
    }
    
    /**
     * Make sure that the file size is calculated
     */
    public void testGetFileSize() {
        assertEquals(bufferPool.getFileSize(), 3072);
    }
    
}
