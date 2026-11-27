package ass2;

import java.io.IOException;
import java.io.InputStream;

public class InputDevice extends Device {
    private final InputStream in;

    public InputDevice(InputStream in) {
        this.in = in;
    }

    @Override
    public byte read() {
        try {
            int b = in.read();
            return b == -1 ? 0 : (byte) b;
        } catch (IOException e) {
            System.err.println("Input device read error: " + e.getMessage());
            return 0;
        }
    }
}
