package finalsim.finalsim_zupichiatti.controller.utils;

import lombok.Getter;

/****
 * Esta clase es una especie de "singleton" que permite obtener un objeto que contiene
 * un par de valores key value sobre el método que se va a utilizar para generar la lista de pseudoaleatorios
 * El Id representa el nombre del bean del generador random, y distribucion representa el nombre
 * que muestra el combobox de javafx;
 */
@Getter
public class MetodoGeneradorRandom {

    private String metodo;
    private String id;

    private MetodoGeneradorRandom(String metodo, String id){
        this.metodo = metodo;
        this.id = id;
    }

    public static MetodoGeneradorRandom getInstanceLineal(){
        return new MetodoGeneradorRandom("Cong. Lineal", ConstantesGenerador.LINEAL);
    }
    public static MetodoGeneradorRandom getInstanceMultiplicativo(){
        return new MetodoGeneradorRandom("Cong. Multiplicativo", ConstantesGenerador.MULTIPLICATIVO);
    }
    public static MetodoGeneradorRandom getInstanceLenguaje(){
        return new MetodoGeneradorRandom("Lenguaje", ConstantesGenerador.LENGUAJE);
    }
    @Override
    public String toString(){
        return metodo;
    }

}
