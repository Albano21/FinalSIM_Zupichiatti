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
public class EventoFinAtencionDespensa extends Evento{

    private Pseudoaleatorio randomTiempoAtencion;
    private float tiempoAtencion;

    public EventoFinAtencionDespensa(){
        this.setNombreEvento("Fin At. Despensa");
    }

    // aca va la logica del evento, tendria que controlar que este disponible la caja sino lo mete a la cola
    @Override
    public VectorEstadoNegocio procesarEvento(VectorEstadoNegocio estadoAnterior, ParametrosGenerador parametrosGenerador, Pseudoaleatorio randomCUBase, IGeneradorRandom generadorRandom, ParametrosNegocio parametrosNegocio, ICambioDistribucion generadorVariableAleatoria, TSBHeap<Evento> heapEventos) {
        return null;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        EventoFinAtencionDespensa evento = new EventoFinAtencionDespensa();
        evento.setMomentoEvento(super.getMomentoEvento());
        evento.setRandomTiempoAtencion( (Pseudoaleatorio) randomTiempoAtencion.clone());
        evento.setTiempoAtencion(tiempoAtencion);
        return evento;
    }


}
