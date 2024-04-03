import java.io.FileNotFoundException;

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
    public static void main(String[] args) {
        // This is the main file for the program.
        String dataFileName = args[0];
        int numBuffers = Integer.parseInt(args[1]);
        String statFileName = args[2];
        try {
            BufferPool pool = new BufferPool(dataFileName, numBuffers);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * Sorts the array using the Quicksort algorithm
     *
     * @param A
     *            Array to be sorted
     * @param i
     *            Start index of the array
     * @param j
     *            End index of the array
     */
    static void quicksort(Comparable[] A, int i, int j) { // Quicksort
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
     * @param A
     *            Array to find the pivot in
     * @param i
     *            Start index of the array
     * @param j
     *            End index of the array
     * @return Index of the pivot element
     */
    static int findpivot(Comparable[] A, int i, int j) {
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
    static int partition(
        Comparable[] A,
        int left,
        int right,
        Comparable pivot) {
        while (left <= right) { // Move bounds inward until they meet
            while (A[left].compareTo(pivot) < 0) {
                left++;
            }
            while ((right >= left) && (A[right].compareTo(pivot) >= 0)) {
                right--;
            }
            if (right > left) {
                swap(A, left, right);
            } // Swap out-of-place values
        }
        return left; // Return first position in right partition
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
    static void swap(Comparable[] A, int i, int j) {
        Comparable temp = A[i];
        A[i] = A[j];
        A[j] = temp;
    }
}