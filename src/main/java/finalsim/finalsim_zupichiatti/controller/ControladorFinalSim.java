package finalsim.finalsim_zupichiatti.controller;

import finalsim.finalsim_zupichiatti.controller.cambioDistribucion.CambioDistribucionExponencialNeg;
import finalsim.finalsim_zupichiatti.controller.cambioDistribucion.CambioDistribucionNormalConvolucion;
import finalsim.finalsim_zupichiatti.controller.cambioDistribucion.CambioDistribucionUniformeAB;
import finalsim.finalsim_zupichiatti.controller.cambioDistribucion.ICambioDistribucion;
import finalsim.finalsim_zupichiatti.controller.generadorRandom.IGeneradorRandom;
import finalsim.finalsim_zupichiatti.controller.utils.ConstantesGenerador;
import finalsim.finalsim_zupichiatti.modelo.ParametrosCambioDistribucion;
import finalsim.finalsim_zupichiatti.modelo.ParametrosGenerador;
import finalsim.finalsim_zupichiatti.modelo.Pseudoaleatorio;
import finalsim.finalsim_zupichiatti.modelo.VaribaleAleatoria;
import finalsim.finalsim_zupichiatti.modelo.colas.ParametrosNegocio;
import finalsim.finalsim_zupichiatti.modelo.colas.VectorEstadoNegocio;
import finalsim.finalsim_zupichiatti.modelo.colas.eventos.*;
import finalsim.finalsim_zupichiatti.modelo.colas.servidores.*;
import finalsim.finalsim_zupichiatti.modelo.estructurasDatos.TSBHeap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ControladorFinalSim {

    @Autowired
    Map<String, IGeneradorRandom> mapGeneradorRandom;

    @Autowired
    Map<String, ICambioDistribucion> generadoresVariableAleatoria;

    public List<VectorEstadoNegocio> generarSimulacion(ParametrosNegocio parametrosNegocio,
                                                       ParametrosGenerador parametrosGenerador){

        if(parametrosNegocio == null) throw new IllegalArgumentException("Debe indicar los parametros de la simulacion");
        parametrosNegocio.validar();
        if(parametrosGenerador == null){
            parametrosGenerador = new ParametrosGenerador();
            parametrosGenerador.setMetodoGeneradorRandom(ConstantesGenerador.LENGUAJE);
        }
        IGeneradorRandom generadorRandom = mapGeneradorRandom.get(parametrosGenerador.getMetodoGeneradorRandom());
        parametrosGenerador.setN(1);
        parametrosGenerador.setPresicion(4);
        Pseudoaleatorio randomCUBase = generadorRandom.generar(parametrosGenerador)[0];

        EventoLlegadaCliente eventoInicial = new EventoLlegadaCliente();
        this.generarEventoInicial(generadorRandom, parametrosGenerador, parametrosNegocio
                                                    ,eventoInicial, randomCUBase);
        EventoFinSimulacion eventoFinal = this.generarEventoFinal(parametrosNegocio);
        VectorEstadoNegocio vectorEstadoAnterior = this.inicializarVectorEstado(eventoInicial, parametrosNegocio);
        vectorEstadoAnterior.setFinSimulacion(eventoFinal);

        TSBHeap<Evento> eventosHeap = new TSBHeap<>();
        eventosHeap.add(eventoInicial);
        eventosHeap.add(eventoFinal);

        Evento eventoActual = eventosHeap.remove();
        VectorEstadoNegocio vectorEstadoActual;
        int cantEventos = 1;
        int ultimaFila = parametrosNegocio.getMostrarFilaDesde() + parametrosNegocio.getCantFilasMostrar();
        EventoFinAtencionCaja finAtencionCliente = null;
        List<VectorEstadoNegocio>  simulacionNegocio = new LinkedList<>();
        simulacionNegocio.add(vectorEstadoAnterior);

        while(true){
            // aca se hace el proceso del evento actual, mas adelante se lo actualiza sacando el siguiente
            vectorEstadoActual = eventoActual.procesarEvento(vectorEstadoAnterior, parametrosGenerador,
                    randomCUBase, generadorRandom, parametrosNegocio, generadoresVariableAleatoria, eventosHeap );

            if(finAtencionCliente != null){
                vectorEstadoActual.eliminarClienteAtendido(finAtencionCliente.getClienteAtencionFinalizada());
                finAtencionCliente = null;
            }
            // se actualiza el randomCUBase
            randomCUBase = vectorEstadoActual.getSiguientePseudoCU();
            if(cantEventos >= parametrosNegocio.getMostrarFilaDesde() && cantEventos < ultimaFila || eventoActual instanceof EventoFinSimulacion ){
                simulacionNegocio.add(vectorEstadoActual);
            }
            // actualizo el vector estado anterior con el actual
            vectorEstadoAnterior = vectorEstadoActual;
            // corto si es fin de simulacion
            if(eventoActual instanceof EventoFinSimulacion) break;
            // guardo que fue un fin de atecion
            if(eventoActual instanceof EventoFinAtencionCaja){
                finAtencionCliente = (EventoFinAtencionCaja) eventoActual;
            }
            // saco el proximo evento
            eventoActual = eventosHeap.remove();
            cantEventos++;

        }

        return simulacionNegocio;
    }

    private void generarEventoInicial(IGeneradorRandom generadorRandom,
                                                 ParametrosGenerador parametrosGenerador,
                                                 ParametrosNegocio parametrosNegocio,
                                                 EventoLlegadaCliente evento,
                                                 Pseudoaleatorio randomCUBase){
        // esto queda comentado porque la primer llegada la fijo en 3
        /*
        ParametrosCambioDistribucion parametrosCambioDistribucion = new ParametrosCambioDistribucion();
        parametrosCambioDistribucion.setLambda(parametrosNegocio.getLambdaExpLlegadaClientes());
        parametrosCambioDistribucion.setPresicion(parametrosGenerador.getPresicion());
        CambioDistribucionExponencialNeg generadorExpNeg = (CambioDistribucionExponencialNeg) generadoresVariableAleatoria.get("EXP_NEG");
        VaribaleAleatoria varibaleAleatoria = generadorExpNeg
                .siguienteRandom(parametrosCambioDistribucion, parametrosGenerador, randomCUBase);

        evento.setTiempoHastaProximaLlegada(varibaleAleatoria.getRandomGenerado());
        evento.setMomentoEvento(varibaleAleatoria.getRandomGenerado());

         */
        evento.setRandomProxLlegada(randomCUBase);
        evento.setTiempoHastaProximaLlegada(3);
        evento.setMomentoEvento(3);

        Pseudoaleatorio randomProxDestino = generadorRandom.siguientePseudoAleatoreo(randomCUBase , parametrosGenerador);
        // aca podria parametrizar los valores desde y hasta para ver cual es el destino
        // tendria que pasarlo dentro de parametros negocio
        if (randomProxDestino.getRandom() >= 0 && randomProxDestino.getRandom() < 0.35 ){
            //aca tengo que setear que va a la despensa
            EventoFinAtencionDespensa eventoFinAtencionDespensa = new EventoFinAtencionDespensa();
            evento.setRandomDestinoCliente(randomProxDestino);
            evento.setDestinoCliente(eventoFinAtencionDespensa);
        }
        else{
            EventoFinAtencionPanaderia eventoFinAtencionPanaderia = new EventoFinAtencionPanaderia();
            evento.setRandomDestinoCliente(randomProxDestino);
            evento.setDestinoCliente(eventoFinAtencionPanaderia);

        }

    }

    private EventoFinSimulacion generarEventoFinal(ParametrosNegocio parametrosNegocio){
        EventoFinSimulacion finSimulacion = new EventoFinSimulacion();
        finSimulacion.setMomentoEvento(parametrosNegocio.getMaxMinutosSimular());
        return finSimulacion;
    }

    private VectorEstadoNegocio inicializarVectorEstado(EventoLlegadaCliente eventoInicial,
                                                        ParametrosNegocio parametrosNegocio){

        VectorEstadoNegocio vectorEstadoNegocio = new VectorEstadoNegocio();
        vectorEstadoNegocio.setReloj(0);
        vectorEstadoNegocio.setColaDespensa(new ArrayDeque<>());
        vectorEstadoNegocio.setColaPanaderia(new ArrayDeque<>());
        vectorEstadoNegocio.setColaCaja(new ArrayDeque<>());
        vectorEstadoNegocio.setProximaLlegadaCliente(eventoInicial);
        vectorEstadoNegocio.setNombreEvento(eventoInicial.getNombreEvento());
        vectorEstadoNegocio.setEmpleadoDespensa(this.generarEmpleadoDespensa());
        vectorEstadoNegocio.setEmpleadosPanaderia(this.generarEmpleadosPanaderia());
        vectorEstadoNegocio.setEmpleadoCaja(this.generarEmpleadoCaja());
        vectorEstadoNegocio.setClientes(new LinkedList<>());

        //vectorEstadoNegocio.setFinAtencionDespensa(new EventoFinAtencionDespensa());
        this.inicializarVectorDeEventos(vectorEstadoNegocio, parametrosNegocio);
        //vectorEstadoNegocio.setFinAtencionCaja(new EventoFinAtencionCaja());

        return vectorEstadoNegocio;


    }

    private Servidor generarEmpleadoDespensa(){
        Servidor servidor = new EmpleadoDespensa();
        servidor.setNombre("Despensa");
        servidor.setEstado(EstadoServidor.getInstanceServidorLibre());
        return servidor;
    }

    private List<Servidor> generarEmpleadosPanaderia(){
        List<Servidor> empleadosPanaderia = new ArrayList<>(2);
        for (int i = 1; i <= 2; i++) {
            Servidor servidor = new EmpleadoPanaderia();
            servidor.setNombre("Panaderia-"+i);
            servidor.setId(i);
            servidor.setEstado(EstadoServidor.getInstanceServidorLibre());
            empleadosPanaderia.add(servidor);
        }
        return empleadosPanaderia;
    }

    private Servidor generarEmpleadoCaja(){
        Servidor servidor = new EmpleadoCaja();
        servidor.setNombre("Caja");
        servidor.setEstado(EstadoServidor.getInstanceServidorLibre());
        return servidor;
    }

    private void inicializarVectorDeEventos(VectorEstadoNegocio vectorEstadoNegocio, ParametrosNegocio parametrosNegocio){
        vectorEstadoNegocio.setFinAtencionPanaderia(new EventoFinAtencionPanaderia[2]);
    }


}
