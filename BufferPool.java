import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;

/**
 * This class represents a buffer pool
 * 
 * @author Stuti Shah & Lauren Spehlmann
 * @version 03/30/2024
 */
public class BufferPool implements BufferPoolADT {

    private RandomAccessFile file;
    private int diskReads;
    private int diskWrites;
    private int cacheHits;
    private ArrayList<Buffer> bufferList;
    private int maxSize;

    /**
     * Constructor for the buffer pool class
     * 
     * @throws FileNotFoundException
     *             In case the provided name parameter is not a valid file name
     * @param input
     *            The name of the file to use as input to the buffer pool
     * @param output
     *            The name of the file to use as output to the buffer pool
     * @param sz
     *            The maximum length of the buffer list
     */
    public BufferPool(String name, int sz) throws FileNotFoundException {
        file = new RandomAccessFile(name, "rw");
        diskReads = 0;
        diskWrites = 0;
        cacheHits = 0;
        bufferList = new ArrayList<Buffer>();
        maxSize = sz;
    }


    /**
     * Inserts the given buffer of size 4096 into the specified file position
     * 
     * @param space
     *            The contents of the block to insert into the file
     * @param sz
     *            The size of the byte array, which is the size of the block and
     *            always
     *            4096 in this implementation
     * @param pos
     *            The block ID, which is the index of the block in the file
     */
    @Override
    public void insert(byte[] space, int sz, int pos) {
        try {
            file.seek(pos);
            file.write(space);
            diskWrites = getDiskWrites() + 1;
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Copies the buffer from the given file position into the byte array while
     * minimizing disk accesses
     * 
     * @param space
     *            The contents of the block to insert into the file
     * @param sz
     *            The size of the byte array, which is the size of the block and
     *            always
     *            4096 in this implementation
     * @param pos
     *            The block ID, which is the index of the block in the file
     */
    @Override
    public void getbytes(byte[] space, int sz, int pos) {
        // Check for the buffer in the buffer pool
        boolean found = false;
        for (int i = 0; i < bufferList.size(); i++) {
            if (bufferList.get(i).getBlockID() == pos) {
                // Copy the entire block into the space array that is the
                // parameter to the getBytes() method
                System.arraycopy(bufferList.get(i).getContents(), 0, space, 0,
                    4096);
                // Move the buffer to the front of the list because it was
                // accessed
                Buffer accessed = bufferList.remove(i);
                bufferList.add(0, accessed);
                found = true;
                cacheHits = getCacheHits() + 1;
                break; // Weird things might happen after this swap if we keep
                       // iterating through the list
            }
        }
        // If the buffer was not already in the buffer pool, it needs to be
        // added
        if (found == false) {
            // Eject the LRU buffer if the buffer pool is full
            if (bufferList.size() == maxSize) {
                eject();
            }
            // Put the buffer in the buffer pool from the file
            byte[] contents = new byte[4096];
            try {
                file.seek(pos * 4096);
                file.read(contents, 4096, 0);
                diskReads = getDiskReads() + 1;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            bufferList.add(0, new Buffer(pos, contents));
        }
    }


    /**
     * Helper method to remove the LRU buffer and write back to the file if
     * necessary
     */
    private void eject() {
        Buffer lru = bufferList.remove(maxSize - 1);
        if (lru.getDirty()) {
            // Moves to the correct byte position in the file using the block ID
            try {
                file.seek(lru.getBlockID() * 4096);
                file.write(lru.getContents());
                diskWrites = getDiskWrites() + 1;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Getter method for cache hits, used to write to stat file
     * 
     * @return The number of cache hits that occurred during the sorting
     */
    public int getCacheHits() {
        return cacheHits;
    }


    /**
     * Getter method for disk writes, used to write to stat file
     * 
     * @return The number of disk writes that occurred during the sorting
     */
    public int getDiskWrites() {
        return diskWrites;
    }


    /**
     * Getter method for disk reads, used to write to stat file
     * 
     * @return The number of disk reads that occurred during the sorting
     */
    public int getDiskReads() {
        return diskReads;
    }

}
