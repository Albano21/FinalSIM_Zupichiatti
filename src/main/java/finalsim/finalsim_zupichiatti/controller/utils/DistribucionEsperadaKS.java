package finalsim.finalsim_zupichiatti.controller.utils;

import lombok.Getter;

/****
 * Esta clase es una especie de "singleton" que permite obtener un objeto que contiene
 * un par de valores key value sobre la distribución sobre el que se va a realizar la prueba de chi cuadrado
 * El Id representa el nombre del bean del generador de la prueba, y distribucion representa el nombre
 * que muestra el combobox de javafx;
 */
@Getter
public class DistribucionEsperadaKS {

    private String distribucion;
    private String id;



    private DistribucionEsperadaKS(String distribucion, String id) {
        this.distribucion = distribucion;
        this.id = id;
    }

    public static DistribucionEsperadaKS getInstanceUniforme(){
        return new DistribucionEsperadaKS("Dist. Uniforme",
                ConstantesCambioDistribucion.UNIFORME);
    }
    public static DistribucionEsperadaKS getInstanceNormalBoxMuller(){
        return new DistribucionEsperadaKS("Dist. Normal Box Muller",
                ConstantesCambioDistribucion.NORMAL_BOXMULLER);
    }
    public static DistribucionEsperadaKS getInstanceNormalConvolucion(){
        return new DistribucionEsperadaKS("Dist. Normal Convolucion",
                ConstantesCambioDistribucion.NORMAL_CONVOLUCION);
    }
    public static DistribucionEsperadaKS getInstanceExpNegativa(){
        return new DistribucionEsperadaKS("Dist. Exp. Negativa",
                ConstantesCambioDistribucion.EXP_NEG);
    }
    public static DistribucionEsperadaKS getInstanceExponencial(){
        return new DistribucionEsperadaKS("Dist. Exponencial",
                ConstantesCambioDistribucion.EXP);
    }
    public static DistribucionEsperadaKS getInstancePoisson(){
        return new DistribucionEsperadaKS("Dist. Poisson",
                ConstantesCambioDistribucion.POISSON);
    }

    public String toString(){
        return distribucion;
    }
}
