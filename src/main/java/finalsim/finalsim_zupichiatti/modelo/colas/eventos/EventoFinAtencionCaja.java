package finalsim.finalsim_zupichiatti.modelo.colas.eventos;

import finalsim.finalsim_zupichiatti.controller.cambioDistribucion.ICambioDistribucion;
import finalsim.finalsim_zupichiatti.controller.generadorRandom.IGeneradorRandom;
import finalsim.finalsim_zupichiatti.modelo.ParametrosGenerador;
import finalsim.finalsim_zupichiatti.modelo.Pseudoaleatorio;
import finalsim.finalsim_zupichiatti.modelo.colas.Cliente;
import finalsim.finalsim_zupichiatti.modelo.colas.ParametrosNegocio;
import finalsim.finalsim_zupichiatti.modelo.colas.VectorEstadoNegocio;
import finalsim.finalsim_zupichiatti.modelo.estructurasDatos.TSBHeap;
import lombok.Data;

@Data
public class EventoFinAtencionCaja extends Evento{

    private Pseudoaleatorio randomCantidadArticulos;

    private int cantidadArticulos;

    private Pseudoaleatorio[] randomsDemoraPorArticulo;

    private float[] tiemposDemoraPorArticulo;
    private float tiempoAtencionCaja;




    private Cliente clienteAtencionFinalizada;

    // aca va toda la logica, se supone que aca se muere el objeto cliente
    @Override
    public VectorEstadoNegocio procesarEvento(VectorEstadoNegocio estadoAnterior, ParametrosGenerador parametrosGenerador, Pseudoaleatorio randomCUBase, IGeneradorRandom generadorRandom, ParametrosNegocio parametrosNegocio, ICambioDistribucion generadorVariableAleatoria, TSBHeap<Evento> heapEventos) {
        return null;
    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        EventoFinAtencionCaja evento = new EventoFinAtencionCaja();
        evento.setMomentoEvento(super.getMomentoEvento());

        evento.setTiempoAtencionCaja(tiempoAtencionCaja);
        return evento;

    }
}
