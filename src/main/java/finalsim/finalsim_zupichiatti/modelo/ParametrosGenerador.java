package finalsim.finalsim_zupichiatti.modelo;

import lombok.Data;

@Data
public class ParametrosGenerador {

    private int n;
    private int x0;
    private int g;
    private int k;
    private int c;
    private int presicion;
    private String metodoGeneradorRandom;

    public ParametrosGenerador() {
    }

    public ParametrosGenerador(int n, int presicion) {
        this.n = n;
    }

    public ParametrosGenerador(int n, int x0, int g, int k, int c,int presicion) {
        this.n = n;
        this.x0 = x0;
        this.g = g;
        this.k = k;
        this.c = c;
    }

    public ParametrosGenerador(int n, int x0, int g, int k,int presicion) {
        this.n = n;
        this.x0 = x0;
        this.g = g;
        this.k = k;
    }

}
