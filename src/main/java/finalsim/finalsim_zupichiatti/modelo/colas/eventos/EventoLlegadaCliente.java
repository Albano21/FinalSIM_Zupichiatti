package finalsim.finalsim_zupichiatti.modelo.colas.eventos;

import finalsim.finalsim_zupichiatti.controller.cambioDistribucion.ICambioDistribucion;
import finalsim.finalsim_zupichiatti.controller.generadorRandom.IGeneradorRandom;
import finalsim.finalsim_zupichiatti.modelo.ParametrosGenerador;
import finalsim.finalsim_zupichiatti.modelo.Pseudoaleatorio;
import finalsim.finalsim_zupichiatti.modelo.colas.ParametrosNegocio;
import finalsim.finalsim_zupichiatti.modelo.colas.VectorEstadoNegocio;
import finalsim.finalsim_zupichiatti.modelo.colas.servidores.Servidor;
import finalsim.finalsim_zupichiatti.modelo.estructurasDatos.TSBHeap;
import lombok.Data;

@Data
public class EventoLlegadaCliente extends Evento{

    private Pseudoaleatorio randomProxLlegada;
    private float tiempoHastaProximaLlegada;

    public EventoLlegadaCliente(){this.setNombreEvento("Llegada Cliente");}

    // aca va toda la logica para procesar el evento,
    // aca ve para cual servidor va despensa o panaderia, y si esta libre al que tenga que ir
    @Override
    public VectorEstadoNegocio procesarEvento(VectorEstadoNegocio estadoAnterior, ParametrosGenerador parametrosGenerador, Pseudoaleatorio randomCUBase, IGeneradorRandom generadorRandom, ParametrosNegocio parametrosNegocio, ICambioDistribucion generadorVariableAleatoria, TSBHeap<Evento> heapEventos) {
        return null;



    }

    // Devuelve cual empleado de panaderia esta libre en caso de que vaya a panaderia
    private Servidor obtenerEmpleadoLibrePanaderia(VectorEstadoNegocio vectorEstadoNegocio){

        for(Servidor empleadoPanaderia:vectorEstadoNegocio.getEmpleadosPanaderia()){
            if(empleadoPanaderia.estaLibre()) return empleadoPanaderia;
        }
        return null;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        EventoLlegadaCliente evento = new EventoLlegadaCliente();
        evento.setMomentoEvento(super.getMomentoEvento());
        evento.setRandomProxLlegada((Pseudoaleatorio) randomProxLlegada.clone());
        evento.setTiempoHastaProximaLlegada(tiempoHastaProximaLlegada);
        return evento;
    }

}
