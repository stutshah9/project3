import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import student.TestCase;

/**
 * this class contains test methods for testing the functionality of Quicksort.
 * it extends the TestCase class for JUnit testing.
 * 
 * @author Stuti Shah & Lauren Spehlmann
 * @version 03/23/2024
 */
public class QuicksortTest extends TestCase {

    /**
     * sets up the tests that follow, used for instantiation
     * 
     * @throws Exception
     *             if an error occurs
     */
    public void setUp() throws Exception {
        super.setUp();
        systemOut().clearHistory();
    }


    /**
     * tests the file generation functionality
     * 
     * @throws IOException
     *             if an I/O error occurs
     */
    public void testFileGen() throws IOException {
        String fname = "threeBlock.txt";
        int blocks = 3;
        FileGenerator fg = new FileGenerator(fname, blocks);
        fg.setSeed(33333333); // a non-random number to make generation
                              // deterministic
        fg.generateFile(FileType.ASCII);

        File f = new File(fname);
        long fileNumBytes = f.length();
        long calcedBytes = blocks * FileGenerator.BYTES_PER_BLOCK;
        assertEquals(calcedBytes, fileNumBytes); // size is correct!

        RandomAccessFile raf = new RandomAccessFile(f, "r");
        short firstKey = raf.readShort(); // reads two bytes
        assertEquals(8273, firstKey); // first key looks like ' Q', translates
                                      // to 8273

        raf.seek(8); // moves to byte 8, which is beginning of third record
        short thirdKey = raf.readShort();
        assertEquals(8261, thirdKey); // third key looks like ' E', translates
                                      // to 8261

        raf.close();
    }


    /**
     * tests the file checking functionality
     * 
     * @throws Exception
     *             if an error occurs during the test
     */
    public void testCheckFile() throws Exception {

        String fname = "checkme.txt";
        FileGenerator fg = new FileGenerator(fname, 1);
        fg.setSeed(42);
        fg.generateFile(FileType.ASCII);
        // Notice we *re-generate* this file each time the test runs.
        // That file persists after the test is over

        assertFalse(CheckFile.check(fname));

        // hmmm... maybe do some sorting on that file ...
        // then we can do:
        // assertTrue(CheckFile.check(fname));
    }


    /**
     * This method is a demonstration of the file generator and file checker
     * functionality.
     * It calls generateFile to create a small binary file.
     * It then calls the file checker to see if it is sorted (presumably not
     * since we don't call a sort method in this test).
     * 
     * @throws Exception
     *             if an I/O error or file not found exception occurs
     */
    public void testSorting() throws Exception {
        String fname = "input.bin";
        FileGenerator fg = new FileGenerator(fname, 1);
        fg.generateFile(FileType.BINARY);

        assertFalse(CheckFile.check(fname)); // file shouldn't be sorted

        String[] args = new String[3];
        args[0] = fname; // the file to be sorted.
        args[1] = "1"; // number of buffers, can impact performance
        args[2] = "stats.txt"; // filename for sorting stats
        Quicksort.main(args);
        // Now the file *should* be sorted, so lets check!

        // TODO: In a real test, the following should work:
        assertTrue(CheckFile.check(fname));
    }

}