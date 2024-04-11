import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Project 3 Description:
 * 
 * This project attempts to sort 4 byte records while minimizing disk I/O. To do
 * so, we implemented a buffer pool that is used as temporary storage. It
 * contains blocks with 1024 records each and uses a least recently used
 * strategy when the maximum capacity is reached.
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
    private static byte[] jRecord;
    private static byte[] pRecord;

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
        jRecord = new byte[4];
        pRecord = new byte[4]; // A fourth byte array for partition to
                               // store records in
        try {
            bufferPool = new BufferPool(dataFileName, numBuffers);
            int poolSizeCalc = bufferPool.getFileSize();
            long start = System.currentTimeMillis();
            // creating an instance of the sort class
            Sort sort = new Sort();
            sort.quicksort(0, poolSizeCalc - 1, bufferPool);
            // when the subarray in smaller than 9, do insertion sort as
            // insertion sort is more optimum than quicksort when the array is
            // almost sorted
            for (int i = 1; i < poolSizeCalc; i++) {

                for (int j = i; j > 0; j--) {
                    bufferPool.getbytes(jRecord, j);
                    bufferPool.getbytes(pRecord, j - 1);
                    if (sort.getKey(jRecord) >= sort.getKey(pRecord)) {
                        break;
                    }
                    sort.swap(j, jRecord, j - 1, pRecord, bufferPool);
                }
            }
            bufferPool.writePool();
            long end = System.currentTimeMillis();
            time = end - start;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // printing out stats in the s
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

}
