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
    public static void quicksort(int i, int j) { // Quicksort
        int pivotindex = findpivot(i, j); // Pick a pivot
        byte[] pivot = new byte[4];
        byte[] kRecord = new byte[4];
        byte[] jRecord = new byte[4];
        bufferPool.getbytes(pivot, pivotindex * 4);
        bufferPool.getbytes(jRecord, i * 4);
        swap(pivotindex, pivot, j, jRecord); // Stick pivot at end
        // k will be the first position in the right subarray
        int k = partition(i, j - 1, getKey(pivot));
        bufferPool.getbytes(kRecord, k * 4);
        swap(k, kRecord, j, pivot); // Put pivot in place
        if ((k - i) > 1) {
            quicksort(i, k - 1);
        } // Sort left partition
        if ((j - k) > 1) {
            quicksort(k + 1, j);
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
    public static int findpivot(int i, int j) {
        return (i + j) / 2;
    }

// /**
// * Partitions the array
// *
// * @param left
// * Left index
// * @param right
// * Right index
// * @param pivot
// * Pivot element
// * @return Index of the first position
// */
// public static int partition(int left, int right, short pivot) {
// while (left <= right) { // Move bounds inward until they meet
// byte[] leftRecord = new byte[4];
// bufferPool.getbytes(leftRecord, left);
// while (getKey(leftRecord) < pivot) {
// left++;
// bufferPool.getbytes(leftRecord, left);
// }
// byte[] rightRecord = new byte[4];
// bufferPool.getbytes(rightRecord, right);
// while ((right >= left) && (getKey(rightRecord) >= pivot)) {
// bufferPool.getbytes(leftRecord, right);
// right--;
// }
// if (right > left) {
// swap(left, right);
// } // Swap out-of-place values
// }
// return left; // Return first position in right partition
// }


    public static int partition(int left, int right, short pivot) {
        byte[] leftValue = new byte[4];
        byte[] rightValue = new byte[4];
        while (left <= right) {
            while (true) {
                bufferPool.getbytes(leftValue, left * 4);
                if (getKey(leftValue) < pivot) {
                    left++;
                }
                else {
                    break;
                }
            }

            while (true) {
                if (right < left) {
                    break;
                }
                bufferPool.getbytes(rightValue, right * 4);
                if (getKey(rightValue) > pivot) {
                    right--;
                }
                else {
                    break;
                }
            }

            if (right > left) {
                swap(left, leftValue, right, rightValue);
            }
        }
        return left;
    }


    /**
     * Swaps two elements in an array
     *
     * @param i
     *            Index of the first element
     * @param j
     *            Index of the second element
     */
    private static void swap(int i, byte[] iRecord, int j, byte[] jRecord) {
        bufferPool.insert(jRecord, i);
        bufferPool.insert(iRecord, j);
    }


    /**
     * Get the key of the element at the specified index
     * 
     * @param record
     *            The 4 byte array for the record
     * @return The key of the record
     */
    public static short getKey(byte[] record) {
        ByteBuffer buffer = ByteBuffer.wrap(record);
        return buffer.getShort();
    }

}
