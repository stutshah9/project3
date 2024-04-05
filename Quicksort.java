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
    public static void main(String[] args) throws Exception{
        // This is the main file for the program.
        String dataFileName = args[0];
        int numBuffers = Integer.parseInt(args[1]);
        String statFileName = args[2];
        try {
            fake pool = new fake(dataFileName, numBuffers);
            Sort.quicksort(pool, 0, pool.getFileSize() - 1);
            pool.writePool();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}