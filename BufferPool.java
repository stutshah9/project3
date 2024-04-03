import java.io.FileNotFoundException;
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
    private int bufferSize;

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
        bufferSize = sz;
    }


    /**
     * 
     * 
     * @param space
     * The contents of the block to insert into the file
     * @param sz
     * The size of the byte array, which is the size of the block and always
     * 4096 in this implementation
     * @param pos
     * The block ID, which is the index of the block in the file
     */
    @Override
    public void insert(byte[] space, int sz, int pos) {
        // TODO Auto-generated method stub

    }


    /**
     * 
     */
    @Override
    public void getbytes(byte[] space, int sz, int pos) {
        // TODO Auto-generated method stub

    }

}
