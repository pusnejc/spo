package ass2;

public class Machine {

    public static final int MAX_ADDRESS = 1048575;  // 1 MB - 1
    private static final int WORD_MASK = 0xFFFFFF;
    private static final int BYTE_MASK = 0xFF;
    private final byte[] memory;
    private int A, X, L, B, S, T, PC, SW;
    private double F;

    public Machine() {
        A = X = L = B = S = T = PC = SW = 0;
        F = 0.0;
        memory = new byte[MAX_ADDRESS + 1];
    }

    private static void addrOutOfBoundsCheck(int addr) {
        if (addr < 0 || addr > MAX_ADDRESS) {
            throw new IllegalArgumentException("Address out of bounds: " + addr);
        }
    }

    public int getByte(int addr) {
        addrOutOfBoundsCheck(addr);
        return memory[addr] & BYTE_MASK;
    }

    public void setByte(int addr, int val) {
        addrOutOfBoundsCheck(addr);
        memory[addr] = (byte) (val & BYTE_MASK);
    }

    public int getWord(int addr) {
        addrOutOfBoundsCheck(addr);
        addrOutOfBoundsCheck(addr + 2);
        return (memory[addr] & BYTE_MASK) << 16 | (memory[addr + 1] & BYTE_MASK) << 8 | (memory[addr + 2] & BYTE_MASK);
    }

    public void setWord(int addr, int val) {
        addrOutOfBoundsCheck(addr);
        addrOutOfBoundsCheck(addr + 2);
        memory[addr] = (byte) ((val >>> 16) & BYTE_MASK);
        memory[addr + 1] = (byte) ((val >>> 8) & BYTE_MASK);
        memory[addr + 2] = (byte) (val & BYTE_MASK);
    }

    public double getFloat(int addr) { throw new UnsupportedOperationException("Float not implemented"); }
    public void setFloat(int addr, double val) { throw new UnsupportedOperationException("Float not implemented"); }

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
}
