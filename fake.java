import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class fake {
    private byte[] array;
    private RandomAccessFile file;

    public fake(String name, int sz) throws Exception {
        file = new RandomAccessFile(name, "rw");
        array = new byte[(int)file.length()];
        file.read(array);
        System.out.println("The number of records in the file is " + file.length()/4);
    }


    public void getbytes(byte[] space, int pos) {
        int in = pos * 4;
        for (int i = 0; i < 4; i++) {
            space[i] = array[in+i];
        }
    }
    
    public void insert(byte[] space, int pos) {
        int in = pos * 4;
        for (int i = 0; i < 4; i++) {
            array[in+i] = space[i];
        }
    }
    
    public void writePool() throws Exception {
        file.seek(0);
        file.write(array);
    }


    public int getFileSize() {
        return array.length/4;
    }
}
