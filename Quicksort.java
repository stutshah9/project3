import java.io.FileNotFoundException;
import java.nio.ByteBuffer;

/**
 * {Project Description Here}
 */

/**
 * The class containing the main method.
 *
 * @author Stuti Shah & Lauren Spehlmann
 * @version 3/27/2024
 */

// On my honor:
//
// - I have not used source code obtained from another student,
// or any other unauthorized source, either modified or
// unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

public class Quicksort {

    private static BufferPool bufferPool;

    /**
     * @param args
     *            Command line parameters.
     *            <data-file-name> is the file to be sorted. The sorting takes
     *            place in that file, so this program does modify the input data
     *            file. Be careful to keep a copy of the original when you do
     *            your testing.
     * 
     *            <numb-buffers> determines the number of buffers allocated for
     *            the buffer pool. This value will be in the range 1–20.
     * 
     *            <stat-file-name> is the name of a file that your program will
     *            generate to store runtime statistics; see below for more
     *            information.
     */
    public static void main(String[] args) throws Exception {
        // This is the main file for the program.
        String dataFileName = args[0];
        int numBuffers = Integer.parseInt(args[1]);
        String statFileName = args[2];
        try {
            bufferPool = new BufferPool(dataFileName, numBuffers);
            int poolSizeCalc = bufferPool.getFileSize();
            quicksort(0, poolSizeCalc - 1);
            bufferPool.writePool();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Sorts the buffer pool using the Quicksort algorithm
     *
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
        bufferPool.getbytes(pivot, pivotindex);
        bufferPool.getbytes(jRecord, j);
        swap(pivotindex, pivot, j, jRecord); // Stick pivot at end
        // k will be the first position in the right subarray
        int k = partition(i, j - 1, getKey(pivot));
        bufferPool.getbytes(kRecord, k);
        swap(k, kRecord, j, pivot); // Put pivot in place
        if ((k - i) > 1 && !checkDuplicates(i, k - 1, getKey(kRecord))) {
            quicksort(i, k - 1);
        } // Sort left partition
        if ((j - k) > 1 && !checkDuplicates(k + 1, j, getKey(kRecord))) {
            quicksort(k + 1, j);
        } // Sort right partition
    }
    
    
    public static boolean checkDuplicates(int i, int j, short key) {
        byte[] bytes = new byte[4];
        for (int k = i; k <= j; k++) {
            bufferPool.getbytes(bytes, k);
            if (getKey(bytes) != key) {
                return false;
            }
        }
        return true;
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
     * @param left
     *            Left index
     * @param right
     *            Right index
     * @param pivotKey
     *            Pivot element
     * @return Index of the first position
     */
    public static int partition(int left, int right, short pivotKey) {
        byte[] leftRecord = new byte[4];
        byte[] rightRecord = new byte[4];
        while (left <= right) { // Move bounds inward until they meet
            bufferPool.getbytes(leftRecord, left);
            while (getKey(leftRecord) < pivotKey) {
                left++;
                bufferPool.getbytes(leftRecord, left);
            }
            bufferPool.getbytes(rightRecord, right);
            while ((right >= left) && (getKey(rightRecord) >= pivotKey)) {
                right--;
                if (right >= 0) {
                    bufferPool.getbytes(rightRecord, right);
                }
            }
            if (right > left) {
                swap(left, leftRecord, right, rightRecord);
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
