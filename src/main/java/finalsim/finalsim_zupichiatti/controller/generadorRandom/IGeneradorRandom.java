package finalsim.finalsim_zupichiatti.controller.generadorRandom;


import k1.grupo.p.simulacionestp5colas.modelo.ParametrosGenerador;
import k1.grupo.p.simulacionestp5colas.modelo.Pseudoaleatorio;

public interface IGeneradorRandom {

    Pseudoaleatorio[] generar(ParametrosGenerador parametros);
    Pseudoaleatorio siguientePseudoAleatoreo(Pseudoaleatorio pseudoaleatorio, ParametrosGenerador parametros);
    Pseudoaleatorio siguientePseudoAleatoreo(ParametrosGenerador parametrosGenerador);


}
