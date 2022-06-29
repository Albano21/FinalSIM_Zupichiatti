package finalsim.finalsim_zupichiatti.controller.cambioDistribucion;


import finalsim.finalsim_zupichiatti.modelo.ParametrosCambioDistribucion;
import finalsim.finalsim_zupichiatti.modelo.ParametrosGenerador;
import finalsim.finalsim_zupichiatti.modelo.Pseudoaleatorio;
import finalsim.finalsim_zupichiatti.modelo.VaribaleAleatoria;
import finalsim.finalsim_zupichiatti.modelo.bondadAjuste.Intervalo;

public interface ICambioDistribucion {

    Pseudoaleatorio[] cambiarDistribucion(ParametrosCambioDistribucion parametros, ParametrosGenerador... parametrosGenerador);

    Intervalo[] generarDistFrecuenciaInicial(Pseudoaleatorio[] pseudoaleatorios, ParametrosCambioDistribucion parametrosCambioDistribucion);

    VaribaleAleatoria siguienteRandom(ParametrosCambioDistribucion parametrosCambioDistribucion, ParametrosGenerador parametrosGenerador,
                                      Pseudoaleatorio randomCUBase);

}
