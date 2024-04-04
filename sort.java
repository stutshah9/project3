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
// Byte buffer has the wrap method - ByteBuffer.wrap(4 byte record).getSort() -
// represents the value of the key
public class sort {

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
        sort.bufferPool = bp;
        int pivotindex = findpivot(A, i, j); // Pick a pivot
        swap(A, pivotindex, j); // Stick pivot at end
        // k will be the first position in the right subarray
        int k = partition(A, i, j - 1, A[j]);
        swap(A, k, j); // Put pivot in place
        if ((k - i) > 1) {
            quicksort(A, i, k - 1);
        } // Sort left partition
        if ((j - k) > 1) {
            quicksort(A, k + 1, j);
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


    /**
     * Partitions the array
     *
     * @param A
     *            Array to be partitioned
     * @param left
     *            Left index
     * @param right
     *            Right index
     * @param pivot
     *            Pivot element
     * @return Index of the first position
     */
    public static int partition(int left, int right, int pivot) {
        while (left <= right) { // Move bounds inward until they meet
            while (A[left] < pivot) {
                left++;
            }
            while ((right >= left) && (A[right] >= pivot)) {
                right--;
            }
            if (right > left) {
                swap(A, left, right);
            } // Swap out-of-place values
        }
        return left; // Return first position in right partition
    }


    /**
     * Used to print the array
     * 
     * @param A
     *            Array
     */
    public static void print(int[] A) {
        for (int i = 0; i < A.length; i++) {
            System.out.print(A[i] + " ");
        }
    }


    /**
     * Swaps two elements in an array
     *
     * @param A
     *            Array
     * @param i
     *            Index of the first element
     * @param j
     *            Index of the second element
     */
    private static void swap(int[] A, int i, int j) {
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;
    }
    
    /**
     * Get the key of the element at the specified index
     * 
     */
    private static int getKey(int i) {
        
    }

}
