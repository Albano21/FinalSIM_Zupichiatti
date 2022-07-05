package finalsim.finalsim_zupichiatti.modelo.colas;

import lombok.Data;

@Data
public class ParametrosNegocio {

    // parametros para las distribuciones
    // ver si voy a parametrizar algo mas

    // parametro lamda de llegadas de cliente (por defecto 0.2)
    private float lambdaExpLlegadaClientes;

    // parametros a y b para demora de despensa (por defecto 2.5 y 3.5)
    private float minimoDemoraDespensa;
    private float maximoDemoraDespensa;

    // parametros a y b para demora de panaderia (por defecto 3 y 5)
    private float minimoDemoraPanaderia;
    private float maximoDemoraPanaderia;

    // parametros media y desviacion para demora de caja
    private float mediaDemoraCaja;
    private float desviacionEstCaja;

    // parametros para saber cuantos y cuales vectores se muestran
    private float maxMinutosSimular;
    private int cantFilasMostrar;
    private int mostrarFilaDesde;

    public void validar() {

        StringBuilder stringBuilder = new StringBuilder();

        // validacion minutos a simular
        if (maxMinutosSimular < 1) stringBuilder.append("Se debe simular al menos 1 minuto\n");
        // validacion llegadas
        if (lambdaExpLlegadaClientes < 0)
            stringBuilder.append("En promedio debe llegar más de 1 cliente por unidad de tiempo\n");
        // validacion demora despensa
        if (minimoDemoraDespensa <= 0) stringBuilder.append("La demora minima de despensa debe ser mayor a 0\n");
        if (maximoDemoraDespensa <= minimoDemoraDespensa || maximoDemoraDespensa <= 0)
            stringBuilder.append("La demora maxima de despensa debe ser mayor a la demora minima\n");
        // validacion demora panaderia
        if (minimoDemoraPanaderia <= 0) stringBuilder.append("La demora minima de panaderia debe ser mayor a 0\n");
        if (maximoDemoraPanaderia <= minimoDemoraPanaderia || maximoDemoraPanaderia <= 0)
            stringBuilder.append("La demora maxima de panaderia debe ser mayor a la demora minima\n");
        // ver si esta bien validacion demora caja
        if (mediaDemoraCaja < 1) stringBuilder.append("La media de demora en caja debe ser mayor a 1\n");
        if (desviacionEstCaja <= 0) stringBuilder.append("La desviacion estandar de demora en caja debe ser mayor a 0\n");
        if (desviacionEstCaja >= mediaDemoraCaja) stringBuilder.append("La desviacion estandar de demora en caja debe se menor a la media\n");

        if (stringBuilder.length() > 0) throw new IllegalArgumentException(stringBuilder.toString());
    }




}
