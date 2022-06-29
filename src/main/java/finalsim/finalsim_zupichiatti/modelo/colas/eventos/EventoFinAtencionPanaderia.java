package finalsim.finalsim_zupichiatti.modelo.colas.eventos;

import finalsim.finalsim_zupichiatti.controller.cambioDistribucion.ICambioDistribucion;
import finalsim.finalsim_zupichiatti.controller.generadorRandom.IGeneradorRandom;
import finalsim.finalsim_zupichiatti.modelo.ParametrosGenerador;
import finalsim.finalsim_zupichiatti.modelo.Pseudoaleatorio;
import finalsim.finalsim_zupichiatti.modelo.colas.ParametrosNegocio;
import finalsim.finalsim_zupichiatti.modelo.colas.VectorEstadoNegocio;
import finalsim.finalsim_zupichiatti.modelo.estructurasDatos.TSBHeap;
import lombok.Data;

@Data
public class EventoFinAtencionPanaderia extends Evento{

    private Pseudoaleatorio randomTiempoAtencion;
    private float tiempoAtencion;

    public EventoFinAtencionPanaderia(){
        this.setNombreEvento("Fin At. Panaderia");
    }

    // aca va la logica del evento, deberia fijarse si esta libre la caja sino ponerlo en la cola
    @Override
    public VectorEstadoNegocio procesarEvento(VectorEstadoNegocio estadoAnterior, ParametrosGenerador parametrosGenerador, Pseudoaleatorio randomCUBase, IGeneradorRandom generadorRandom, ParametrosNegocio parametrosNegocio, ICambioDistribucion generadorVariableAleatoria, TSBHeap<Evento> heapEventos) {
        return null;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        EventoFinAtencionPanaderia evento = new EventoFinAtencionPanaderia();
        evento.setMomentoEvento(super.getMomentoEvento());
        evento.setRandomTiempoAtencion( (Pseudoaleatorio) randomTiempoAtencion.clone());
        evento.setTiempoAtencion(tiempoAtencion);
        return evento;
    }


}
