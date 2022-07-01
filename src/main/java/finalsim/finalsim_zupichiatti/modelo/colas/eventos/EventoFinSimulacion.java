package finalsim.finalsim_zupichiatti.modelo.colas.eventos;

import finalsim.finalsim_zupichiatti.controller.cambioDistribucion.ICambioDistribucion;
import finalsim.finalsim_zupichiatti.controller.generadorRandom.IGeneradorRandom;
import finalsim.finalsim_zupichiatti.modelo.ParametrosGenerador;
import finalsim.finalsim_zupichiatti.modelo.Pseudoaleatorio;
import finalsim.finalsim_zupichiatti.modelo.colas.ParametrosNegocio;
import finalsim.finalsim_zupichiatti.modelo.colas.VectorEstadoNegocio;
import finalsim.finalsim_zupichiatti.modelo.estructurasDatos.TSBHeap;

import java.util.Map;

public class EventoFinSimulacion extends Evento{

    public EventoFinSimulacion() {
        this.setNombreEvento("Fin Simulación");
    }
    @Override
    public VectorEstadoNegocio procesarEvento(VectorEstadoNegocio estadoAnterior,
                                              ParametrosGenerador parametrosGenerador,
                                              Pseudoaleatorio randomCUBase,
                                              IGeneradorRandom generadorRandom,
                                              ParametrosNegocio parametrosNegocio,
                                              Map<String, ICambioDistribucion> generadoresVariableAleatoria,
                                              TSBHeap<Evento> heapEventos) {

        VectorEstadoNegocio vectorEstadoActual = (VectorEstadoNegocio) estadoAnterior.clone();
        vectorEstadoActual.setReloj(this.momentoEvento);
        vectorEstadoActual.setNombreEvento(this.nombreEvento);

        return vectorEstadoActual;

    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        EventoFinSimulacion evento = new EventoFinSimulacion();
        evento.setMomentoEvento(super.getMomentoEvento());
        return evento;
    }
}
