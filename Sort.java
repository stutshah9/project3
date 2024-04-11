import java.nio.ByteBuffer;
import java.util.Random;

/**
 * The class containing the quicksort methods in an optimized way
 *
 * @author Stuti Shah & Lauren Spehlmann
 * @version 3/27/2024
 */
public class Sort {
    private byte[] pivot;
    private byte[] kRecord;
    private byte[] jRecord;
    private byte[] pRecord;
    private Random random = new Random();

    /**
     * The constructor for the sort class that initalizes the byte arrays
     */
    public Sort() {
        pivot = new byte[4];
        kRecord = new byte[4];
        jRecord = new byte[4];
        pRecord = new byte[4];
    }


    /**
     * Sorts the buffer pool using the Quicksort algorithm
     *
     * @param i
     *            Start index of the array
     * @param j
     *            End index of the array
     * @param bufferPool
     *            The buffer pool used for the sort
     */
    public void quicksort(int i, int j, BufferPool bufferPool) { // Quicksort
        int pivotindex = findpivot(i, j, bufferPool); // Pick a pivot
        bufferPool.getbytes(pivot, pivotindex);
        bufferPool.getbytes(jRecord, j);
        swap(pivotindex, pivot, j, jRecord, bufferPool); // Stick pivot at end
        // k will be the first position in the right subarray
        short key = getKey(pivot);
        int k = partition(i, j - 1, key, bufferPool);
        bufferPool.getbytes(kRecord, k);
        swap(k, kRecord, j, pivot, bufferPool); // Put pivot in place
        // if partition returned the first index --> check duplicates --> if
        // true, terminate
        if (k == i) {
            if (checkDuplicates(i + 1, j, key, pivot, bufferPool)) {
                return;
            }
        }
        if ((k - i) > 8 && !checkDuplicates(i, k - 1, key, pivot, bufferPool)) {
            quicksort(i, k - 1, bufferPool); // Sort left partition
        }
        if ((j - k) > 8 && !checkDuplicates(k + 1, j - 1, getKey(kRecord),
            pivot, bufferPool)) {
            quicksort(k + 1, j, bufferPool); // Sort right partition
        }
    }


    /**
     * Returns true if all values in the subarray are duplicates to optimize
     * quicksort
     * 
     * @param i
     *            Beginning index in the subarray
     * @param j
     *            End index in the subarray
     * @param key
     *            Pivot key
     * @param bytes
     *            The array to fill for getbytes
     * @param bufferPool
     *            The buffer pool used for the sort
     * @return True if all value in the range of the pool are the same
     */
    public boolean checkDuplicates(
        int i,
        int j,
        short key,
        byte[] bytes,
        BufferPool bufferPool) {
        // loops through the records between i and j until a non duplicate is
        // found
        for (int k = i; k <= j; k++) {
            bufferPool.getbytes(bytes, k);
            if (getKey(bytes) != key) {
                return false;
            }
        }
        return true;
    }


// UNSURE ABOUT THIS - pivot: choose 3 random pivots and take the median of them
    /**
     * Finds the pivot element
     * 
     * @param i
     *            Start index of the array
     * @param j
     *            End index of the array
     * @param bufferPool
     *            The buffer pool used for the sort
     * @return Index of the pivot element
     */
    private int findpivot(int i, int j, BufferPool bufferPool) {
        // choose 3 random ints between i and j, get the key of each of these
        // records and calculate their median
        int pivot1 = random.nextInt(j - i + 1) + i;
        int pivot2 = random.nextInt(j - i + 1) + i;
        int pivot3 = random.nextInt(j - i + 1) + i;

        byte[] pivot1Array = new byte[4];
        byte[] pivot2Array = new byte[4];
        byte[] pivot3Array = new byte[4];

        bufferPool.getbytes(pivot1Array, pivot1);
        bufferPool.getbytes(pivot2Array, pivot2);
        bufferPool.getbytes(pivot3Array, pivot3);

        short a = getKey(pivot1Array);
        short b = getKey(pivot2Array);
        short c = getKey(pivot3Array);

        if ((a <= b && b <= c) || (c <= b && b <= a)) {
            return pivot2;
        }
        else if ((b <= a && a <= c) || (c <= a && a <= b)) {
            return pivot1;
        }
        else {
            return pivot3;
        }
    }


    /**
     * Partitions the array - referred from OpenDSA
     *
     * @param left
     *            Left index
     * @param right
     *            Right index
     * @param pivotKey
     *            Pivot element
     * @param bufferPool
     *            The buffer pool used for the sort
     * @return Index of the first position
     */
    private int partition(
        int left,
        int right,
        short pivotKey,
        BufferPool bufferPool) {
        while (left <= right) { // Move bounds inward until they meet
            bufferPool.getbytes(jRecord, left);
            while (getKey(jRecord) < pivotKey) {
                left++;
                bufferPool.getbytes(jRecord, left);
            }
            bufferPool.getbytes(pRecord, right);
            while ((right >= left) && (getKey(pRecord) >= pivotKey)) {
                right--;
                if (right >= 0) { // to avoid out of bounds exceptions
                    bufferPool.getbytes(pRecord, right);
                }
            }
            if (right > left) {
                swap(left, jRecord, right, pRecord, bufferPool);
            } // Swap out-of-place values
        }
        return left; // Return first position in right partition
    }


    /**
     * Swaps two elements in an array
     * Taking in the byte arrays makes the swap more efficient
     * 
     * @param i
     *            Index of the first element
     * @param recordI
     *            The byte array for i
     * @param j
     *            Index of the second element
     * @param recordJ
     *            The byte array for j
     * @param bufferPool
     *            The buffer pool used for the sort
     */
    public void swap(
        int i,
        byte[] recordI,
        int j,
        byte[] recordJ,
        BufferPool bufferPool) {
        bufferPool.insert(recordI, j);
        bufferPool.insert(recordJ, i);
    }


    /**
     * Get the key of the element at the specified index
     * 
     * @param record
     *            The 4 byte array for the record
     * @return The key of the record
     */
    public short getKey(byte[] record) {
        ByteBuffer buffer = ByteBuffer.wrap(record); // using the ByteBuffer
                                                     // library and the
                                                     // preexisting wrap method
        return buffer.getShort();
    }
}
