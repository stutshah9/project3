import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;

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
    private LinkedList<Buffer> bufferList;
    private int maxSize;
    private static final int RECORDS_IN_BUFFER = 1024;
    private static final int BUFFER_SIZE = RECORDS_IN_BUFFER * 4;

    /**
     * Constructor for the buffer pool class
     * 
     * @throws FileNotFoundException
     *             In case the provided name parameter is not a valid file name
     * @param name
     *            The name of the file to use as input to the buffer pool
     * @param sz
     *            The maximum length of the buffer list
     */
    public BufferPool(String name, int sz) throws FileNotFoundException {
        file = new RandomAccessFile(name, "rw");
        diskReads = 0;
        diskWrites = 0;
        cacheHits = 0;
        bufferList = new LinkedList<Buffer>();
        maxSize = sz;
        // for loop --> create all buffers now with dumby value
        for (int i = 0; i < sz; i++) {
            Buffer buffer = new Buffer(-1, new byte[BUFFER_SIZE]);
            bufferList.add(buffer);
        }
    }


    /**
     * Inserts the given record of size 4 bytes into the specified file position
     * 
     * @param space
     *            The contents of the record to insert into the file
     * @param pos
     *            The index of the record in the file
     */
    @Override
    public void insert(byte[] space, int pos) {
        int index = 0;
        // Check for the block in the buffer pool
        boolean found = false;
        for (index = 0; index < bufferList.size(); index++) {
            Buffer getContents = bufferList.get(index);
            if (getContents.getBlockID() == (pos / RECORDS_IN_BUFFER)
                && !found) {
                // Copy the record into the block and move the block to the
                // front of the list
                System.arraycopy(space, 0, getContents.getContents(), (pos
                    % RECORDS_IN_BUFFER) * 4, 4);
                Buffer accessed = bufferList.remove(index);
                bufferList.add(0, accessed);
                bufferList.get(0).setDirty();
                found = true;
                cacheHits++;
                // Weird things might happen after this swap if we keep
                // iterating through the list because blocks are out of order
                break;
            }
        }
        // If the buffer was not already in the buffer pool, it needs to be
        // added and modified
        if (!found) {
            // Eject the LRU buffer if the buffer pool is full
            if (index == maxSize) {
                eject();
                index--;
            }
            Buffer bufferOverwrite = bufferList.remove(index);
            // Put the new buffer in the buffer pool from the file
            try {
                // Find beginning of block
                int blockID = pos / RECORDS_IN_BUFFER;
                file.seek(blockID * BUFFER_SIZE);
                file.read(bufferOverwrite.getContents());
                diskReads++;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            bufferOverwrite.setBlockID(pos / RECORDS_IN_BUFFER);
            bufferList.add(0, bufferOverwrite);
            // Now insert the record into the block
            System.arraycopy(space, 0, bufferList.get(0).getContents(), (pos
                % RECORDS_IN_BUFFER) * 4, 4);
            bufferList.get(0).setDirty();
        }
    }


    /**
     * Copies the buffer from the given file position into the byte array while
     * minimizing disk accesses
     * 
     * @param space
     *            The contents of the record to insert into the array
     * @param pos
     *            The index of the record in the file
     */
    @Override
    public void getbytes(byte[] space, int pos) {
        int index = 0;
        // Check for the buffer in the buffer pool
        boolean found = false;
        for (index = 0; index < bufferList.size(); index++) {
            Buffer getContents = bufferList.get(index);
            if (getContents.getBlockID() == (pos / RECORDS_IN_BUFFER)
                && !found) {
                // Copy the entire record into the space array that is the
                // parameter to the getBytes() method
                System.arraycopy(getContents.getContents(), (pos
                    % RECORDS_IN_BUFFER) * 4, space, 0, 4);
                // Move the buffer to the front of the list because it was
                // accessed
                Buffer accessed = bufferList.remove(index);
                bufferList.add(0, accessed);
                found = true;
                cacheHits++;
                // Weird things might happen after this swap if we keep
                // iterating through the list
                break;
            }
        }
        // If the buffer was not already in the buffer pool, it needs to be
        // added
        if (!found) {
            // Eject the LRU buffer if the buffer pool is full
            if (index == maxSize) {
                eject();
                index--;
            }
            // Put the new buffer in the buffer pool from the file
            // do not need this --> just use buffer.getContents()
            Buffer bufferOverwrite = bufferList.remove(index);

            try {
                // Find beginning of block
                int blockID = pos / RECORDS_IN_BUFFER;
                file.seek(blockID * BUFFER_SIZE);
                file.read(bufferOverwrite.getContents());
                diskReads++;
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            bufferOverwrite.setBlockID(pos / RECORDS_IN_BUFFER);

            bufferList.add(0, bufferOverwrite);
            // Now copy from the record into the array
            System.arraycopy(bufferList.get(0).getContents(), (pos
                % RECORDS_IN_BUFFER) * 4, space, 0, 4);
        }
    }


    /**
     * Helper method to remove the LRU buffer and write back to the file if
     * necessary
     */
    private void eject() {
        Buffer lru = bufferList.get(maxSize - 1);
        if (lru.getDirty()) {
            // Moves to the correct byte position in the file using the block ID
            try {
                file.seek(lru.getBlockID() * BUFFER_SIZE);
                file.write(lru.getContents());
                diskWrites++;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Method to use after the sort has been completed and the remaining buffers
     * should be written back to the file
     */
    public void writePool() {
        for (Buffer buffer : bufferList) {
            if (buffer.getDirty()) {
                // Moves to the correct byte position in the file using the
                // block ID
                try {
                    file.seek(buffer.getBlockID() * BUFFER_SIZE);
                    file.write(buffer.getContents());
                    diskWrites++;
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
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


    /**
     * Getter method for list of buffers
     * 
     * @return The array list of buffers in the buffer pool
     */
    public LinkedList<Buffer> getBufferList() {
        return bufferList;
    }


    /**
     * Takes the number of bytes in the file and divides it by 4 to calculate
     * how many records there are
     * 
     * @return The number of records in the file
     */
    public int getFileSize() {
        try {
            return (int)(file.length() / 4);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
