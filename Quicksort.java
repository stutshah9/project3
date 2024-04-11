import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.util.Random;

/**
 * {Project Description Here}
 * 
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
    private static byte[] pivot;
    private static byte[] kRecord;
    private static byte[] jRecord;
    private static byte[] pRecord;
    private static Random random = new Random();

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
        long time = 0;
        pivot = new byte[4];
        kRecord = new byte[4];
        jRecord = new byte[4];
        pRecord = new byte[4]; // A fourth byte array for partition to
                               // store records in
        try {
            bufferPool = new BufferPool(dataFileName, numBuffers);
            int poolSizeCalc = bufferPool.getFileSize();
            long start = System.currentTimeMillis();
            quicksort(0, poolSizeCalc - 1);
            bufferPool.writePool();
            long end = System.currentTimeMillis();
            time = end - start;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(
                new FileWriter(statFileName, true)));
            writer.write("RUNTIME STATS\n");
            writer.write("File name: " + dataFileName + "\n");
            writer.write("Cache hits: " + bufferPool.getCacheHits() + "\n");
            writer.write("Disk reads: " + bufferPool.getDiskReads() + "\n");
            writer.write("Disk writes: " + bufferPool.getDiskWrites() + "\n");
            writer.write("Sort execution time: " + time + "ms\n\n");
            writer.close();
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
        if (checkDuplicates(i, j, pivot)) {
            return;
        }
        if ((j - i) < 9) {
            // when the subarray in smaller than 9, do insertion sort as
            // insertion sort is more optimum than quicksort when the array is
            // almost sorted
            for (int a = i; a <= j; a++) {
                for (int b = a; b > 0; b--) {
                    bufferPool.getbytes(jRecord, b);
                    bufferPool.getbytes(pRecord, b - 1);
                    if (getKey(jRecord) >= getKey(pRecord)) {
                        break;
                    }
                    swap(b, jRecord, b - 1, pRecord);
                }
            }
            return;
        }
        int pivotindex = findpivot(i, j); // Pick a pivot
        bufferPool.getbytes(pivot, pivotindex);
        bufferPool.getbytes(jRecord, j);
        swap(pivotindex, pivot, j, jRecord); // Stick pivot at end
        // k will be the first position in the right subarray
        short key = getKey(pivot);
        int k = partition(i, j - 1, key);
        bufferPool.getbytes(kRecord, k);
        swap(k, kRecord, j, pivot); // Put pivot in place
        // already tried 9 but that does not work
        if ((k - i) > 1) {
            quicksort(i, k - 1); // Sort left partition
        }
        if ((j - k) > 1) {
            quicksort(k + 1, j); // Sort right partition
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
     * @param bytes
     *            The array to fill for getbytes
     * @return True if all value in the range of the pool are the same
     */
    public static boolean checkDuplicates(int i, int j, byte[] bytes) {
        bufferPool.getbytes(bytes, j);
        short key = getKey(bytes);
        for (int k = i; k < j; k++) {
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
        while (left <= right) { // Move bounds inward until they meet
            bufferPool.getbytes(jRecord, left);
            while (getKey(jRecord) < pivotKey) {
                left++;
                bufferPool.getbytes(jRecord, left);
            }
            bufferPool.getbytes(pRecord, right);
            while ((right >= left) && (getKey(pRecord) >= pivotKey)) {
                right--;
                if (right >= 0) {
                    bufferPool.getbytes(pRecord, right);
                }
            }
            if (right > left) {
                swap(left, jRecord, right, pRecord);
            } // Swap out-of-place values
        }
        return left; // Return first position in right partition
    }


    /**
     * Swaps two elements in an array
     *
     * @param i
     *            Index of the first element
     * @param recordI
     *            The byte array for i
     * @param j
     *            Index of the second element
     * @param recordI
     *            The byte array for i
     */
    private static void swap(int i, byte[] recordI, int j, byte[] recordJ) {
        bufferPool.insert(recordJ, i);
        bufferPool.insert(recordI, j);
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
