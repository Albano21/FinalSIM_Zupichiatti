package finalsim.finalsim_zupichiatti.controller.cambioDistribucion;


import k1.grupo.p.simulacionestp5colas.modelo.ParametrosCambioDistribucion;
import k1.grupo.p.simulacionestp5colas.modelo.ParametrosGenerador;
import k1.grupo.p.simulacionestp5colas.modelo.Pseudoaleatorio;
import k1.grupo.p.simulacionestp5colas.modelo.VaribaleAleatoria;
import k1.grupo.p.simulacionestp5colas.modelo.bondadAjuste.Intervalo;

public interface ICambioDistribucion {

    Pseudoaleatorio[] cambiarDistribucion(ParametrosCambioDistribucion parametros, ParametrosGenerador... parametrosGenerador);

    Intervalo[] generarDistFrecuenciaInicial(Pseudoaleatorio[] pseudoaleatorios, ParametrosCambioDistribucion parametrosCambioDistribucion);

    VaribaleAleatoria siguienteRandom(ParametrosCambioDistribucion parametrosCambioDistribucion, ParametrosGenerador parametrosGenerador,
                                      Pseudoaleatorio randomCUBase);

}
