package finalsim.finalsim_zupichiatti.controller.utils;

import lombok.Getter;

/****
 * Esta clase es una especie de "singleton" que permite obtener un objeto que contiene
 * un par de valores key value sobre la distribución sobre el que se va a realizar la prueba de chi cuadrado
 * El Id representa el nombre del bean del generador de la prueba, y distribucion representa el nombre
 * que muestra el combobox de javafx;
 */
@Getter
public class DistribucionEsperadaChiCuadrado {

    private String distribucion;
    private String id;



    private DistribucionEsperadaChiCuadrado(String distribucion, String id) {
        this.distribucion = distribucion;
        this.id = id;
    }

    public static DistribucionEsperadaChiCuadrado getInstanceUniforme(){
        return new DistribucionEsperadaChiCuadrado("Dist. Uniforme",
                ConstantesCambioDistribucion.UNIFORME);
    }
    public static DistribucionEsperadaChiCuadrado getInstanceNormalBoxMuller(){
        return new DistribucionEsperadaChiCuadrado("Dist. Normal Box Muller",
                ConstantesCambioDistribucion.NORMAL_BOXMULLER);
    }
    public static DistribucionEsperadaChiCuadrado getInstanceNormalConvolucion(){
        return new DistribucionEsperadaChiCuadrado("Dist. Normal Convolucion",
                ConstantesCambioDistribucion.NORMAL_CONVOLUCION);
    }
    public static DistribucionEsperadaChiCuadrado getInstanceExpNegativa(){
        return new DistribucionEsperadaChiCuadrado("Dist. Exp. Negativa",
                ConstantesCambioDistribucion.EXP_NEG);
    }
    public static DistribucionEsperadaChiCuadrado getInstanceExponencial(){
        return new DistribucionEsperadaChiCuadrado("Dist. Exponencial",
                ConstantesCambioDistribucion.EXP);
    }
    public static DistribucionEsperadaChiCuadrado getInstancePoisson(){
        return new DistribucionEsperadaChiCuadrado("Dist. Poisson",
                ConstantesCambioDistribucion.POISSON);
    }

    public String toString(){
        return distribucion;
    }
}
