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
     *            The file's contents for the requested block
     */
    public Buffer(int id, byte[] bytes) {
        contents = bytes;
        blockID = id;
        dirtyBit = false;
    }


    /**
     * Changes the contents of the byte array and makes a note to update the
     * file accordingly
     * 
     * @param bytes
     *            The new contents of the buffer
     */
    public void updateBuffer(byte[] bytes) {
        dirtyBit = true;
        contents = bytes;
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
     * Getter method for the byte array
     * 
     * @return The contents of the block from the file stored in the buffer
     */
    public byte[] getContents() {
        return contents;
    }
}
