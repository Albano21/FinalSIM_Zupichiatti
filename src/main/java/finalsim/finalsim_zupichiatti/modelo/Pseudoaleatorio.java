package finalsim.finalsim_zupichiatti.modelo;

public class Pseudoaleatorio {

    private int i;
    private float random;
    private int axi;
    private int semilla;

    public Pseudoaleatorio() {
    }


    public Pseudoaleatorio(float random, int axi, int semilla) {
        this.random = random;
        this.axi = axi;
        this.semilla = semilla;
    }

    public Pseudoaleatorio(int i, float random, int axi, int semilla) {
        this.i = i;
        this.random = random;
        this.axi = axi;
        this.semilla = semilla;
    }

    public Pseudoaleatorio(int i, float random) {
        this.i = i;
        this.random = random;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public float getRandom() {
        return random;
    }


    public void setRandom(float random) {
        this.random = random;
    }

    public int getAxi() {
        return axi;
    }

    public void setAxi(int axi) {
        this.axi = axi;
    }

    public int getSemilla() {
        return semilla;
    }

    public void setSemilla(int semilla) {
        this.semilla = semilla;
    }

    @Override
    public String toString() {
        return "Pseudoaleatorio{" +
                "i=" + i +
                ", random=" + random +
                ", axi=" + axi +
                ", semilla=" + semilla +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Pseudoaleatorio pseudoaleatorio = new Pseudoaleatorio();
        pseudoaleatorio.setAxi(axi);
        pseudoaleatorio.setRandom(random);
        pseudoaleatorio.setI(i);
        pseudoaleatorio.setSemilla(semilla);
        return pseudoaleatorio;
    }
}
