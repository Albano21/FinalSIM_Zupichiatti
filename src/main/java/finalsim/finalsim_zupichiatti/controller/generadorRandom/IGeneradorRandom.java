package finalsim.finalsim_zupichiatti.controller.generadorRandom;


import finalsim.finalsim_zupichiatti.modelo.ParametrosGenerador;
import finalsim.finalsim_zupichiatti.modelo.Pseudoaleatorio;

public interface IGeneradorRandom {

    Pseudoaleatorio[] generar(ParametrosGenerador parametros);
    Pseudoaleatorio siguientePseudoAleatoreo(Pseudoaleatorio pseudoaleatorio, ParametrosGenerador parametros);
    Pseudoaleatorio siguientePseudoAleatoreo(ParametrosGenerador parametrosGenerador);


}
