/**
 * This class represents a single buffer that contains 1 block from the file
 * 
 * @author Stuti Shah & Lauren Spehlmann
 * @version 3/28/24
 */
public class Buffer {
    private boolean dirtyBit;
    private int blockID;
    private byte[] contents;

    /**
     * Buffer constructor, initializes the dirtyBit to false because no updates
     * have occurred and sets the block ID and contents
     * 
     * @param id
     *            The index of the block in the file
     * @param bytes
     *            An array of size 4096 with the file's contents for the
     *            requested block
     */
    public Buffer(int id, byte[] bytes) {
        contents = bytes;
        blockID = id;
        dirtyBit = false;
    }


    /**
     * Sets the dirty bit to true, used when a record is copied into the buffer
     * during a swap in quicksort
     */
    public void setDirty() {
        dirtyBit = true;
    }


    /**
     * Getter method for the dirty bit
     * 
     * @return True if the buffer's contents were changed
     */
    public boolean getDirty() {
        return dirtyBit;
    }


    /**
     * Getter method for the blockID
     * 
     * @return The index at which the block occurs in the file
     */
    public int getBlockID() {
        return blockID;
    }


    /**
     * Setter method for the blockID
     * 
     * @param blockid
     *            The blockID of the buffer
     */
    public void setBlockID(int blockid) {
        blockID = blockid;
    }


    /**
     * Getter method for the byte array
     * 
     * @return The contents of the block from the file stored in the buffer
     */
    public byte[] getContents() {
        return contents;
    }


    /**
     * Setter method for the contents
     * 
     * @param content
     *            The contents of the buffer
     */
    public void setContents(byte[] content) {
        contents = content;
    }
}
