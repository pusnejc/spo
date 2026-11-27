package ass2;

public class Machine {

    public static final int MAX_ADDRESS = 1048575;  // 1 MB - 1
    public static final int MAX_DEVICES = 256;
    public static final int WORD_MASK = 0xFFFFFF;
    public static final int BYTE_MASK = 0xFF;
    public static final int NIBBLE_MASK = 0xF;
    public static final int CC_MASK = 0xC0;
    public static final int CC_LT = 0x00;
    public static final int CC_EQ = 0x40;
    public static final int CC_GT = 0x80;
    private int A, X, L, B, S, T, PC, SW;
    private double F;
    private final byte[] memory;
    private final Device[] devices;

    public Machine() {
        A = X = L = B = S = T = PC = SW = 0;
        F = 0.0;
        memory = new byte[MAX_ADDRESS + 1];
        devices = new Device[MAX_DEVICES];
        devices[0] = new InputDevice(System.in);
        devices[1] = new OutputDevice(System.out);
        devices[2] = new OutputDevice(System.err);
        for (int i = 3; i < MAX_DEVICES; i++) {
            devices[i] = new FileDevice(String.format("%02X.dev", i));
        }
    }

    // ================ Registers ================

    public int getA() { return A; }
    public int getX() { return X; }
    public int getL() { return L; }
    public int getB() { return B; }
    public int getS() { return S; }
    public int getT() { return T; }
    public int getPC() { return PC; }
    public int getSW() { return SW; }
    public double getF() { return F; }

    public void setA(int val) { A = val & WORD_MASK; }
    public void setX(int val) { X = val & WORD_MASK; }
    public void setL(int val) { L = val & WORD_MASK; }
    public void setB(int val) { B = val & WORD_MASK; }
    public void setS(int val) { S = val & WORD_MASK; }
    public void setT(int val) { T = val & WORD_MASK; }
    public void setPC(int val) { PC = val & WORD_MASK; }
    public void setSW(int val) { SW = val & WORD_MASK; }
    public void setF(double val) { F = val; }

    public int getReg(int reg) {
        return switch (reg) {
            case 0 -> getA();
            case 1 -> getX();
            case 2 -> getL();
            case 3 -> getB();
            case 4 -> getS();
            case 5 -> getT();
            case 8 -> getPC();
            case 9 -> getSW();
            case 6 -> throw new IllegalArgumentException("F register returns double, use getF()");
            default -> throw new IllegalArgumentException("Register does not exist: " + reg);
        };
    }

    public void setReg(int reg, int val) {
        switch (reg) {
            case 0 -> setA(val);
            case 1 -> setX(val);
            case 2 -> setL(val);
            case 3 -> setB(val);
            case 4 -> setS(val);
            case 5 -> setT(val);
            case 8 -> setPC(val);
            case 9 -> setSW(val);
            case 6 ->
                throw new IllegalArgumentException("F register takes double, use setF(val)");
            default ->
                throw new IllegalArgumentException("Register does not exist: " + reg);
        }
    }

    private void setCC(int cmp) {
        int cc = cmp < 0 ? CC_LT : cmp == 0 ? CC_EQ : CC_GT;
        SW = (SW & ~CC_MASK) | cc;
    }

    private int getCC() { return SW & CC_MASK; }
    private boolean isLess() { return getCC() == CC_LT; }
    private boolean isEqual() { return getCC() == CC_EQ; }
    private boolean isGreater() { return getCC() == CC_GT; }

    // interprets a 24-bit word as a signed (two's complement) value.
    public static int signed(int val) {
        return (val << 8) >> 8;
    }

    // ===========================================

    // ================= Memory =================

    private static void addrCheck(int addr) {
        if (addr < 0 || addr > MAX_ADDRESS) {
            throw new IllegalArgumentException("Address out of bounds: " + addr);
        }
    }

    public int getByte(int addr) {
        addrCheck(addr);
        return memory[addr] & BYTE_MASK;
    }

    public void setByte(int addr, int val) {
        addrCheck(addr);
        memory[addr] = (byte) (val & BYTE_MASK);
    }

    public int getWord(int addr) {
        addrCheck(addr);
        addrCheck(addr + 2);
        return (memory[addr] & BYTE_MASK) << 16 | (memory[addr + 1] & BYTE_MASK) << 8 | (memory[addr + 2] & BYTE_MASK);
    }

    public void setWord(int addr, int val) {
        addrCheck(addr);
        addrCheck(addr + 2);
        memory[addr] = (byte) ((val >>> 16) & BYTE_MASK);
        memory[addr + 1] = (byte) ((val >>> 8) & BYTE_MASK);
        memory[addr + 2] = (byte) (val & BYTE_MASK);
    }

    public double getFloat(int addr) { throw new UnsupportedOperationException("Float not implemented"); }
    public void setFloat(int addr, double val) { throw new UnsupportedOperationException("Float not implemented"); }

    // ==========================================

    // ================= Devices =================

    private static void deviceCheck(int num) {
        if (num < 0 || num >= MAX_DEVICES) {
            throw new IllegalArgumentException("Device does not exist: " + num);
        }
    }

    public Device getDevice(int num) {
        deviceCheck(num);
        return devices[num];
    }

    public void setDevice(int num, Device device) {
        deviceCheck(num);
        devices[num] = device;
    }

    // ===========================================

    // ================= Errors =================

    private void error(String msg) {
        System.err.printf("Error at PC=0x%06X: %s%n", PC, msg);
    }

    private void notImplemented(String mnemonic) {
        error("instruction not implemented: " + mnemonic);
    }

    private void invalidOpcode(int opcode) {
        error(String.format("invalid opcode 0x%02X", opcode));
    }

    private void invalidAddressing() {
        error("invalid addressing mode");
    }

    // ==========================================

    // ================ Execution ================

    private int fetch() {
        int pcValue = getPC();
        int nextByte = getByte(pcValue);
        setPC(pcValue + 1);
        return nextByte;
    }

    public void execute() {
        int fstByte = fetch();

        if (execF1(fstByte)) return;

        int sndByte = fetch();

        if (execF2(fstByte, sndByte)) return;
    }

    private boolean execF1(int opcode) {
        switch (opcode) {
            case Opcode.FIX, Opcode.FLOAT, Opcode.NORM,
                Opcode.SIO, Opcode.HIO, Opcode.TIO -> notImplemented(Opcode.mnemonic(opcode));
            default -> { return false; }
        }
        return true;
    }

    private boolean execF2(int opcode, int operand) {
        int r1 = (operand >>> 4) & NIBBLE_MASK;
        int r2 = operand & NIBBLE_MASK;
        switch (opcode) {
            case Opcode.ADDR -> setReg(r2, getReg(r2) + getReg(r1));
            case Opcode.CLEAR -> setReg(r1, 0);
            case Opcode.COMPR -> setCC(Integer.compare(signed(getReg(r1)), signed(getReg(r2))));
            case Opcode.DIVR -> {
                int divisor = signed(getReg(r1));
                if (divisor == 0) {
                    error("division by zero");
                } else {
                    setReg(r2, signed(getReg(r2)) / divisor);
                }
            }
            case Opcode.MULR -> setReg(r2, getReg(r2) * getReg(r1));
            case Opcode.RMO -> setReg(r2, getReg(r1));
            case Opcode.SHIFTL -> {
                int n = r2 + 1;
                int val = getReg(r1);
                setReg(r1, (val << n) | (val >>> (24 - n)));
            }
            case Opcode.SHIFTR -> {
                int n = r2 + 1;
                int val = signed(getReg(r1));
                setReg(r1, val >> n);
            }
            case Opcode.SUBR -> setReg(r2, getReg(r2) - getReg(r1));
            case Opcode.SVC -> notImplemented(Opcode.mnemonic(opcode));
            case Opcode.TIXR -> {
                int newX = getX() + 1;
                setX(newX);
                setCC(Integer.compare(signed(newX), signed(getReg(r1))));
            }
            default -> { return false; }
        }
        return true;
    }

    // ===========================================
}
