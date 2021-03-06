package finalsim.finalsim_zupichiatti.modelo.colas.eventos;

import finalsim.finalsim_zupichiatti.controller.cambioDistribucion.CambioDistribucionExponencialNeg;
import finalsim.finalsim_zupichiatti.controller.cambioDistribucion.CambioDistribucionUniformeAB;
import finalsim.finalsim_zupichiatti.controller.cambioDistribucion.ICambioDistribucion;
import finalsim.finalsim_zupichiatti.controller.generadorRandom.IGeneradorRandom;
import finalsim.finalsim_zupichiatti.controller.utils.CommonFunc;
import finalsim.finalsim_zupichiatti.controller.utils.ConstantesCambioDistribucion;
import finalsim.finalsim_zupichiatti.modelo.ParametrosCambioDistribucion;
import finalsim.finalsim_zupichiatti.modelo.ParametrosGenerador;
import finalsim.finalsim_zupichiatti.modelo.Pseudoaleatorio;
import finalsim.finalsim_zupichiatti.modelo.VaribaleAleatoria;
import finalsim.finalsim_zupichiatti.modelo.colas.Cliente;
import finalsim.finalsim_zupichiatti.modelo.colas.EstadoCliente;
import finalsim.finalsim_zupichiatti.modelo.colas.ParametrosNegocio;
import finalsim.finalsim_zupichiatti.modelo.colas.VectorEstadoNegocio;
import finalsim.finalsim_zupichiatti.modelo.colas.servidores.EstadoServidor;
import finalsim.finalsim_zupichiatti.modelo.colas.servidores.Servidor;
import finalsim.finalsim_zupichiatti.modelo.estructurasDatos.TSBHeap;
import lombok.Data;

import java.util.Map;

@Data
public class EventoLlegadaCliente extends Evento{

    private Pseudoaleatorio randomProxLlegada;
    private float tiempoHastaProximaLlegada;

    private Pseudoaleatorio randomDestinoCliente;

    private Evento destinoCliente;

    public EventoLlegadaCliente(){this.setNombreEvento("Llegada Cliente");}

    // aca va toda la logica para procesar el evento,
    // aca ve para cual servidor va despensa o panaderia, y si esta libre al que tenga que ir
    @Override
    public VectorEstadoNegocio procesarEvento(VectorEstadoNegocio estadoAnterior,
                                              ParametrosGenerador parametrosGenerador,
                                              Pseudoaleatorio randomCUBase,
                                              IGeneradorRandom generadorRandom,
                                              ParametrosNegocio parametrosNegocio,
                                              Map<String, ICambioDistribucion> generadoresVariableAleatoria,
                                              TSBHeap<Evento> heapEventos) {

        VectorEstadoNegocio vectorEstadoActual = (VectorEstadoNegocio) estadoAnterior.clone();
        vectorEstadoActual.setNombreEvento(this.nombreEvento);
        vectorEstadoActual.setReloj(momentoEvento);

        ParametrosCambioDistribucion parametrosCambioDistribucion = new ParametrosCambioDistribucion();
        parametrosCambioDistribucion.setLambda(parametrosNegocio.getLambdaExpLlegadaClientes());
        parametrosCambioDistribucion.setPresicion(parametrosGenerador.getPresicion());
        //--------------------------PROXIMA LLEGADA----------------------
        // saca el proximo random y variable para la proxima llegada
        CambioDistribucionExponencialNeg generadorExpNeg = (CambioDistribucionExponencialNeg) generadoresVariableAleatoria.get(ConstantesCambioDistribucion.EXP_NEG);
                // new CambioDistribucionExponencialNeg();
                //(CambioDistribucionExponencialNeg) generadoresVariableAleatoria.get("EXP_NEG");
        VaribaleAleatoria tiempoProximaLlegada = generadorExpNeg.
                siguienteRandom(parametrosCambioDistribucion,parametrosGenerador,randomCUBase);
        randomCUBase = tiempoProximaLlegada.getSiguienteRandomBase();

        // se genera la proxima llegada
        EventoLlegadaCliente proximaLlegada = new EventoLlegadaCliente();
        proximaLlegada.setRandomProxLlegada(randomCUBase);
        proximaLlegada.setTiempoHastaProximaLlegada(tiempoProximaLlegada.getRandomGenerado());
        double momentoEventoProxLlegada = vectorEstadoActual.getReloj()+proximaLlegada.getTiempoHastaProximaLlegada();
        momentoEventoProxLlegada = CommonFunc.round(momentoEventoProxLlegada,4);
        proximaLlegada.setMomentoEvento(momentoEventoProxLlegada);

        // manejo cliente de proxima llegada
        Pseudoaleatorio randomProxDestino = generadorRandom.siguientePseudoAleatoreo(randomDestinoCliente, parametrosGenerador);
        // aca podria parametrizar los valores desde y hasta para ver cual es el destino
        // tendria que pasarlo dentro de parametros negocio
        if (randomProxDestino.getRandom() >= 0 && randomProxDestino.getRandom() < 0.35 ){
            //aca tengo que setear que va a la despensa
            EventoFinAtencionDespensa eventoFinAtencionDespensa = new EventoFinAtencionDespensa();
            proximaLlegada.setRandomDestinoCliente(randomProxDestino);
            proximaLlegada.setDestinoCliente(eventoFinAtencionDespensa);
        }
        else{
            EventoFinAtencionPanaderia eventoFinAtencionPanaderia = new EventoFinAtencionPanaderia();
            proximaLlegada.setRandomDestinoCliente(randomProxDestino);
            proximaLlegada.setDestinoCliente(eventoFinAtencionPanaderia);
        }

        // se actualiza en el vector actual
        vectorEstadoActual.setProximaLlegadaCliente(proximaLlegada);
        heapEventos.add(proximaLlegada);

        //-----------------------MANEJO LLEGADA ACTUAL-------------------

        // hago el generador uniforme AB que se va a usar para fin at despensa o panaderia segun corresponda
        CambioDistribucionUniformeAB generadorUniformeAB = (CambioDistribucionUniformeAB) generadoresVariableAleatoria.get(ConstantesCambioDistribucion.UNIFORME);
        Cliente cliente = new Cliente();

        //aca seria si el destino es despensa
        if (destinoCliente.getClass().equals(EventoFinAtencionDespensa.class)){
            Servidor empleadoDespensa = this.obtenerEmpleadoDespensa(vectorEstadoActual);
            // si el empleado esta ocupado
            if(empleadoDespensa == null){
                cliente.setEstado(EstadoCliente.getInstanceEsperandoDespensa());
                vectorEstadoActual.agregarCliente(cliente);
                vectorEstadoActual.agregarClienteColaDespensa(cliente);
                vectorEstadoActual.actualizarMaximoLargoColaDespensa();
            }
            // si el empleado esta libre
            else{
                // actualiza el cliente
                vectorEstadoActual.agregarCliente(cliente);
                cliente.setEstado(EstadoCliente.getInstanceSiendoAtendidoDespensa());
                cliente.setServidorActual(empleadoDespensa);
                // actualiza el servidor
                empleadoDespensa.setEstado(EstadoServidor.getInstanceServidorOcupado());
                empleadoDespensa.setClienteActual(cliente);
                // setea los valores para calcular la variable demora despensa
                parametrosCambioDistribucion.setUnifA(parametrosNegocio.getMinimoDemoraDespensa());
                parametrosCambioDistribucion.setUnifB(parametrosNegocio.getMaximoDemoraDespensa());
                // calculo variable
                VaribaleAleatoria tiempoAtencionDespensa = generadorUniformeAB
                        .siguienteRandom(parametrosCambioDistribucion, parametrosGenerador, randomCUBase);
                randomCUBase = tiempoAtencionDespensa.getSiguienteRandomBase();
                //crea evento fin at despensa con sus datos
                EventoFinAtencionDespensa eventoFinAtencionDespensa = new EventoFinAtencionDespensa();
                eventoFinAtencionDespensa.setRandomTiempoAtencion(randomCUBase);
                eventoFinAtencionDespensa.setTiempoAtencion(tiempoAtencionDespensa.getRandomGenerado());
                double momentoEventoFinAtDespensa = vectorEstadoActual.getReloj()+tiempoAtencionDespensa.getRandomGenerado();
                momentoEventoFinAtDespensa = CommonFunc.round(momentoEventoFinAtDespensa ,4);
                eventoFinAtencionDespensa.setMomentoEvento(momentoEventoFinAtDespensa);
                eventoFinAtencionDespensa.setCliente(cliente);
                // se actualiza el evento de atencion despensa
                vectorEstadoActual.actualizarEventoFinAtencionDespensa(eventoFinAtencionDespensa);
                heapEventos.add(eventoFinAtencionDespensa);
            }
            cliente.setNumeroCliente(vectorEstadoActual.getContadorClientes());
            vectorEstadoActual.setSiguientePseudoCU(randomCUBase);
            return vectorEstadoActual;
        }
        // aca seria si el destino es panaderia
        else{
            Servidor empleadoPanaderia = this.obtenerEmpleadoLibrePanaderia(vectorEstadoActual);
            // si el empleado esta ocupado{
            if (empleadoPanaderia == null){
                cliente.setEstado(EstadoCliente.getInstanceEsperandoPanaderia());
                vectorEstadoActual.agregarCliente(cliente);
                vectorEstadoActual.agregarClienteColaPanaderia(cliente);
                vectorEstadoActual.actualizarMaximoLargoColaPanaderia();
            }
            // si hay un empleado libre de panaderia
            else {
                // actualiza el cliente
                vectorEstadoActual.agregarCliente(cliente);
                cliente.setEstado(EstadoCliente.getInstanceSiendoAtendidoPanaderia());
                cliente.setServidorActual(empleadoPanaderia);
                // actualiza el servidor
                empleadoPanaderia.setEstado(EstadoServidor.getInstanceServidorOcupado());
                empleadoPanaderia.setClienteActual(cliente);
                //setea los valores para calcular la variable demora
                parametrosCambioDistribucion.setUnifA(parametrosNegocio.getMinimoDemoraPanaderia());
                parametrosCambioDistribucion.setUnifB(parametrosNegocio.getMaximoDemoraPanaderia());
                // calculo variable
                VaribaleAleatoria tiempoAtencionPanaderia = generadorUniformeAB
                        .siguienteRandom(parametrosCambioDistribucion, parametrosGenerador, randomCUBase);
                randomCUBase = tiempoAtencionPanaderia.getSiguienteRandomBase();
                // crea evento fin at. panaderia con sus datos
                EventoFinAtencionPanaderia eventoFinAtencionPanaderia = new EventoFinAtencionPanaderia();
                eventoFinAtencionPanaderia.setRandomTiempoAtencion(randomCUBase);
                eventoFinAtencionPanaderia.setTiempoAtencion(tiempoAtencionPanaderia.getRandomGenerado());
                double momentoEventoFinAtPanaderia = vectorEstadoActual.getReloj()+tiempoAtencionPanaderia.getRandomGenerado();
                momentoEventoFinAtPanaderia = CommonFunc.round( momentoEventoFinAtPanaderia,4);
                eventoFinAtencionPanaderia.setMomentoEvento(momentoEventoFinAtPanaderia);
                eventoFinAtencionPanaderia.setCliente(cliente);
                // se actualiza el evento de atencion despensa
                vectorEstadoActual.actualizarEventoFinAtencionPanaderia(eventoFinAtencionPanaderia, empleadoPanaderia);
                heapEventos.add(eventoFinAtencionPanaderia);
            }
            //cliente.setNumeroCliente(vectorEstadoActual.getProximoNumeroCliente());

            cliente.setNumeroCliente(vectorEstadoActual.getContadorClientes());
            vectorEstadoActual.setSiguientePseudoCU(randomCUBase);
            return vectorEstadoActual;
        }

    }

    // Devuelve el empleado de la panaderia o null si esta ocupado
    private Servidor obtenerEmpleadoDespensa(VectorEstadoNegocio vectorEstadoNegocio){
        Servidor empleadoDespensa = vectorEstadoNegocio.getEmpleadoDespensa();
        if (empleadoDespensa.estaLibre()) return empleadoDespensa;
        else return null;
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
        evento.setRandomDestinoCliente((Pseudoaleatorio) randomDestinoCliente.clone());
        evento.setDestinoCliente((Evento) destinoCliente.clone());
        return evento;
    }

    public String getNombreDestinoCliente(){
        if (destinoCliente.getClass().equals(EventoFinAtencionDespensa.class)){
            return "Despensa";
        }
        else{
            return "Panaderia";
        }
    }

}
