package ass2;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileDevice extends Device {
    private final String filename;
    private RandomAccessFile raf; // null until first use
    private boolean failed;       // true if opening failed, so we don't retry

    public FileDevice(String filename) {
        this.filename = filename;
    }

    private boolean open() {
        if (raf != null) return true;
        if (failed) return false;
        try {
            raf = new RandomAccessFile(filename, "rw");
            return true;
        } catch (IOException e) {
            failed = true;
            System.err.println("Cannot open file device '" + filename + "': " + e.getMessage());
            return false;
        }
    }

    @Override
    public byte read() {
        if (!open()) return 0;
        try {
            int b = raf.read();
            return b == -1 ? 0 : (byte) b;
        } catch (IOException e) {
            System.err.println("File device '" + filename + "' read error: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public void write(byte value) {
        if (!open()) return;
        try {
            raf.write(value & Machine.BYTE_MASK);
        } catch (IOException e) {
            System.err.println("File device '" + filename + "' write error: " + e.getMessage());
        }
    }
}
