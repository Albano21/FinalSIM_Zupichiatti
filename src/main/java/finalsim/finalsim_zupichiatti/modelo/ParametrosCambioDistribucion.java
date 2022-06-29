package finalsim.finalsim_zupichiatti.modelo;


public class ParametrosCambioDistribucion {

    private int n;
    private float unifA, unifB;
    private float media;
    private float desvEst;
    private float lambda;
    private int presicion;
    private int kInicial;
    private int dEmpiricos;

    public ParametrosCambioDistribucion(){
        this.presicion = 4;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public float getUnifA() {
        return unifA;
    }

    public void setUnifA(float unifA) {
        this.unifA = unifA;
    }

    public float getUnifB() {
        return unifB;
    }

    public void setUnifB(float unifB) {
        this.unifB = unifB;
    }

    public float getMedia() {
        return media;
    }

    public void setMedia(float media) {
        this.media = media;
    }

    public float getDesvEst() {
        return desvEst;
    }

    public void setDesvEst(float desvEst) {
        this.desvEst = Math.abs(desvEst);
    }

    public float getLambda() {
        return lambda;
    }

    public void setLambda(float lambda) {
        this.lambda = Math.abs(lambda);
    }

    public int getPresicion() {
        return presicion;
    }

    public void setPresicion(int presicion) {
        this.presicion = presicion;
    }

    public int getKInicial() {
        return kInicial;
    }

    public void setKInicial(int kInicial) {
        this.kInicial = Math.abs(kInicial);
    }

    public int getDEmpiricos() {
        return dEmpiricos;
    }

    public void setDEmpiricos(int dEmpiricos) {
        this.dEmpiricos = dEmpiricos;
    }
}
