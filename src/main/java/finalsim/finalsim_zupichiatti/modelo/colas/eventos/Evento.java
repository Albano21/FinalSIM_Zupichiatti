package finalsim.finalsim_zupichiatti.modelo.colas.eventos;

import finalsim.finalsim_zupichiatti.controller.cambioDistribucion.ICambioDistribucion;
import finalsim.finalsim_zupichiatti.controller.generadorRandom.IGeneradorRandom;
import finalsim.finalsim_zupichiatti.modelo.ParametrosGenerador;
import finalsim.finalsim_zupichiatti.modelo.Pseudoaleatorio;
import finalsim.finalsim_zupichiatti.modelo.colas.Cliente;
import finalsim.finalsim_zupichiatti.modelo.colas.ParametrosNegocio;
import finalsim.finalsim_zupichiatti.modelo.colas.VectorEstadoNegocio;
import finalsim.finalsim_zupichiatti.modelo.estructurasDatos.TSBHeap;

import java.util.Map;

public abstract class Evento implements Comparable<Evento>{

    //Representa el momento en el que se va a producir el evento
    //Cada evento particular tendrá los campos necesarios para definir este atributo, pero
    //no deberá sobrescribir este ya que es el que se deberá usar para organizar
    //el heap de eventos

    protected float momentoEvento;
    protected String nombreEvento;
    protected Cliente cliente;

    public abstract VectorEstadoNegocio procesarEvento(VectorEstadoNegocio estadoAnterior,
                                                       ParametrosGenerador parametrosGenerador,
                                                       Pseudoaleatorio randomCUBase,
                                                       IGeneradorRandom generadorRandom,
                                                       ParametrosNegocio parametrosNegocio,
                                                       Map<String, ICambioDistribucion> generadoresVariableAleatoria,
                                                       TSBHeap<Evento> heapEventos);
    @Override
    public int compareTo(Evento evento){
        return Float.compare(momentoEvento, evento.getMomentoEvento());
    }

    public float getMomentoEvento() {
        return momentoEvento;
    }

    public void setMomentoEvento(float momentoEvento) {
        this.momentoEvento = momentoEvento;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public abstract Object clone() throws CloneNotSupportedException;


}
