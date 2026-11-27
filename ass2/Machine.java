package spo.ass2;

public class Machine {

    private int A, X, L, B, S, T, PC, SW;
    private double F;
    private final int regMask = 0xFFFFFF;

    public Machine() {
        A = X = L = B = S = T = PC = SW = 0;
        F = 0.0;

    }

    public int getA() { return A; }
    public int getX() { return X; }
    public int getL() { return L; }
    public int getB() { return B; }
    public int getS() { return S; }
    public int getT() { return T; }
    public int getPC() { return PC; }
    public int getSW() { return SW; }
    public double getF() { return F; }
    
    public void setA(int val) { A = val & regMask; }
    public void setX(int val) { X = val & regMask; }
    public void setL(int val) { L = val & regMask; }
    public void setB(int val) { B = val & regMask; }
    public void setS(int val) { S = val & regMask; }
    public void setT(int val) { T = val & regMask; }
    public void setPC(int val) { PC = val & regMask; }
    public void setSW(int val) { SW = val & regMask; }
    public void setF(double val) { F = val; }

    public int getReg(int reg) {
        switch (reg) {
            case 0: return getA();
            case 1: return getX();
            case 2: return getL();
            case 3: return getB();
            case 4: return getS();
            case 5: return getT();
            case 8: return getPC();
            case 9: return getSW();        
            case 6: throw new IllegalArgumentException("F register returns double, use getF()");
            default: throw new IllegalArgumentException("Register does not exist: " + reg);
        }
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
