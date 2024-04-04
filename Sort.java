import java.nio.ByteBuffer;

/**
 * This class contains the quicksort methods
 *
 * @author Stuti Shah & Lauren Spehlmann
 * @version 3/27/2024
 */

// Instead of arrays take in buffer pool - buffer pool has insert and getByte
// A[j] replace that with bufferPool.getByte()
// Create getKey() method in sort class - BufferPool.getByte() - but this cannot
// be compared with pivot so you have to use the getKey method on this to get
// the key
// Byte buffer has the wrap method - ByteBuffer.wrap(4 byte record).getShort() -
// represents the value of the key
public class Sort {

    private static BufferPool bufferPool;

    /**
     * Sorts the buffer pool using the Quicksort algorithm
     *
     * @param bp
     *            BufferPool to be sorted
     * @param i
     *            Start index of the array
     * @param j
     *            End index of the array
     */
    public static void quicksort(BufferPool bp, int i, int j) { // Quicksort
        Sort.bufferPool = bp;
        int pivotindex = findpivot(bp, i, j); // Pick a pivot
        swap(bp, pivotindex, j); // Stick pivot at end
        // k will be the first position in the right subarray
        byte[] jRecord = new byte[4];
        bp.getbytes(jRecord, j);
        int k = partition(bp, i, j - 1, getKey(jRecord));
        swap(bp, k, j); // Put pivot in place
        if ((k - i) > 1) {
            quicksort(bp, i, k - 1);
        } // Sort left partition
        if ((j - k) > 1) {
            quicksort(bp, k + 1, j);
        } // Sort right partition
    }


    /**
     * Finds the pivot element
     * 
     * @param i
     *            Start index of the array
     * @param j
     *            End index of the array
     * @return Index of the pivot element
     */
    public static int findpivot(BufferPool bp, int i, int j) {
        return (i + j) / 2;
    }


    /**
     * Partitions the array
     *
     * @param left
     *            Left index
     * @param right
     *            Right index
     * @param pivot
     *            Pivot element
     * @return Index of the first position
     */
    public static int partition(BufferPool bp, int left, int right, int pivot) {
        while (left <= right) { // Move bounds inward until they meet
            byte[] leftRecord = new byte[4];
            bp.getbytes(leftRecord, left);
            while (getKey(leftRecord) < pivot) {
                bp.getbytes(leftRecord, left);
                left++;
            }
            byte[] rightRecord = new byte[4];
            bp.getbytes(rightRecord, right);
            while ((right >= left) && (getKey(rightRecord) >= pivot)) {
                bp.getbytes(leftRecord, right);
                right--;
            }
            if (right > left) {
                swap(bp, left, right);
            } // Swap out-of-place values
        }
        return left; // Return first position in right partition
    }


    /**
     * Swaps two elements in an array
     *
     * @param i
     *            Index of the first element
     * @param j
     *            Index of the second element
     */
    private static void swap(BufferPool bp, int i, int j) {
        byte[] iRecord = new byte[4];
        bp.getbytes(iRecord, i);
        byte[] jRecord = new byte[4];
        bp.getbytes(jRecord, j);
        bp.insert(jRecord, i);
        bp.insert(iRecord, j);
    }


    /**
     * Get the key of the element at the specified index
     * 
     * @param record
     *            The 4 byte array for the record
     * @return The key of the record
     */
    public static int getKey(byte[] record) {
        ByteBuffer buffer = ByteBuffer.wrap(record);
        return buffer.getShort();
    }

}