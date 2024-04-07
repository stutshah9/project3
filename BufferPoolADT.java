/**
 * ADT for buffer pools using the message-passing style
 * 
 * @author OpenDSA
 * @version 3/27/2024
 */
public interface BufferPoolADT {
    /**
     * Copy 4 bytes from "space" to position "pos" in the buffered storage
     * 
     * @param space
     *            The byte array with the record to insert
     * @param pos
     *            The record index
     */
    public void insert(byte[] space, int pos);


    /**
     * Copy 4 bytes from position "pos" of the buffered storage to "space"
     * 
     * @param space
     *            The byte array to fill with the desired record contents
     * @param pos
     *            The record index
     */
    public void getbytes(byte[] space, int pos);
}
