package ass2;

import java.io.IOException;
import java.io.OutputStream;

public class OutputDevice extends Device {
    private final OutputStream out;

    public OutputDevice(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(byte value) {
        try {
            out.write(value & Machine.BYTE_MASK);
            out.flush();
        } catch (IOException e) {
            System.err.println("Output device write error: " + e.getMessage());
        }
    }
}
