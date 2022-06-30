package finalsim.finalsim_zupichiatti.modelo.colas.eventos;

import finalsim.finalsim_zupichiatti.controller.cambioDistribucion.ICambioDistribucion;
import finalsim.finalsim_zupichiatti.controller.generadorRandom.IGeneradorRandom;
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

@Data
public class EventoFinAtencionPanaderia extends Evento{

    private Pseudoaleatorio randomTiempoAtencion;
    private float tiempoAtencion;

    public EventoFinAtencionPanaderia(){
        this.setNombreEvento("Fin At. Panaderia");
    }

    // aca va la logica del evento, deberia fijarse si esta libre la caja sino ponerlo en la cola
    @Override
    public VectorEstadoNegocio procesarEvento(VectorEstadoNegocio estadoAnterior,
                                              ParametrosGenerador parametrosGenerador,
                                              Pseudoaleatorio randomCUBase,
                                              IGeneradorRandom generadorRandom,
                                              ParametrosNegocio parametrosNegocio,
                                              ICambioDistribucion generadorVariableAleatoria,
                                              TSBHeap<Evento> heapEventos) {

        VectorEstadoNegocio vectorEstadoActual = (VectorEstadoNegocio) estadoAnterior.clone();
        vectorEstadoActual.setReloj(this.getMomentoEvento());
        vectorEstadoActual.setNombreEvento(this.nombreEvento);

        Servidor empleadoPanaderiaAnterior = cliente.getServidorActual();

        Cliente clienteActual = vectorEstadoActual.buscarClientePorId(cliente.getNumeroCliente());

        Servidor empleadoPanaderia = vectorEstadoActual.getEmpleadoPanaderiaPorId(empleadoPanaderiaAnterior.getId());

        //-------------------MANEJO DEL FIN AT ACTUAL-------------------
        // verifica si esta disponible el empleado de caja
        Servidor empleadoCaja = this.obtenerEmpleadoCaja(vectorEstadoActual);
        // Si el empleado caja esta ocupado
        if (empleadoCaja == null){
            vectorEstadoActual.agregarClienteColaCaja(clienteActual);
            clienteActual.setEstado(EstadoCliente.getInstanceEsperandoCaja());
        }
        // si el empleado caja esta libre
        else {
            // actualiza cliente
            clienteActual.setEstado(EstadoCliente.getInstanceSiendoAtendidoCaja());
            clienteActual.setServidorActual(empleadoCaja);
            // actualiza servidor
            empleadoCaja.setEstado(EstadoServidor.getInstanceServidorOcupado());
            empleadoCaja.setClienteActual(clienteActual);
            // setea los valores para calcular la variable demora caja
            ParametrosCambioDistribucion parametrosCambioDistribucion = new ParametrosCambioDistribucion();
            parametrosCambioDistribucion.setMedia(parametrosNegocio.getMediaDemoraCaja());
            parametrosCambioDistribucion.setDesvEst(parametrosNegocio.getDesviacionEstCaja());
            // calculo variable
            VaribaleAleatoria tiempoAtencionCaja = generadorVariableAleatoria
                    .siguienteRandom(parametrosCambioDistribucion, parametrosGenerador, randomCUBase);
            // crea evento fin atencion caja
            EventoFinAtencionCaja eventoFinAtencionCaja = new EventoFinAtencionCaja();

            eventoFinAtencionCaja.setTiempoAtencionCaja(tiempoAtencionCaja.getRandomGenerado());
            eventoFinAtencionCaja.setMomentoEvento(vectorEstadoActual.getReloj() + eventoFinAtencionCaja.getTiempoAtencionCaja());
            eventoFinAtencionCaja.setCliente(clienteActual);
            randomCUBase = tiempoAtencionCaja.getSiguienteRandomBase();
            // se actualiza el evento fin atencion caja
            vectorEstadoActual.actualizarEventoFinAtencionCaja(eventoFinAtencionCaja);
            heapEventos.add(eventoFinAtencionCaja);
            // actualiza el acumulador ded tiempo ocioso empleado caja
            vectorEstadoActual.acumularTiempoOcioso();
        }
        //--------------------MANEJO PROXIMA ATENCION PANADERIA---------------
        Cliente clienteEsperaColaPanaderia = vectorEstadoActual.getSiguienteClienteColaPanaderia();
        // si no hay clientes en la cola
        if (clienteEsperaColaPanaderia == null){
            //actualiza el servidor
            empleadoPanaderia.setEstado(EstadoServidor.getInstanceServidorLibre());
            empleadoPanaderia.setClienteActual(null);
            //el evento en el vector
            vectorEstadoActual.actualizarEventoFinAtencionPanaderia(null, empleadoPanaderia);
        }
        // si hay clientes en cola
        else{
            // se recupera el cliente de la lista del cliente, actualiza referencia
            clienteEsperaColaPanaderia = vectorEstadoActual.buscarClientePorId(clienteEsperaColaPanaderia.getNumeroCliente());
            // se actualiza el cliente
            clienteEsperaColaPanaderia.setEstado(EstadoCliente.getInstanceSiendoAtendidoPanaderia());
            clienteEsperaColaPanaderia.setServidorActual(empleadoPanaderia);
            // se actualiza el servidor
            empleadoPanaderia.setEstado(EstadoServidor.getInstanceServidorOcupado());
            empleadoPanaderia.setClienteActual(clienteEsperaColaPanaderia);
            // setea los valores para calcular la variable demora
            ParametrosCambioDistribucion parametrosCambioDistribucion = new ParametrosCambioDistribucion();
            parametrosCambioDistribucion.setUnifA(parametrosNegocio.getMinimoDemoraPanaderia());
            parametrosCambioDistribucion.setUnifB(parametrosNegocio.getMaximoDemoraPanaderia());
            parametrosCambioDistribucion.setPresicion(parametrosGenerador.getPresicion());
            // calculo variable
            VaribaleAleatoria tiempoAtencionPanaderia = generadorVariableAleatoria
                    .siguienteRandom(parametrosCambioDistribucion, parametrosGenerador, randomCUBase);
            // crea evento fin at panaderia con sus datos
            EventoFinAtencionPanaderia siguienteFinAtencionPanaderia = new EventoFinAtencionPanaderia();
            siguienteFinAtencionPanaderia.setRandomTiempoAtencion(randomCUBase);
            siguienteFinAtencionPanaderia.setTiempoAtencion(tiempoAtencionPanaderia.getRandomGenerado());
            siguienteFinAtencionPanaderia.setMomentoEvento(vectorEstadoActual.getReloj()+siguienteFinAtencionPanaderia.getTiempoAtencion());
            siguienteFinAtencionPanaderia.setCliente(clienteEsperaColaPanaderia);
            // se actualiza el evento fin at despensa en el vector
            vectorEstadoActual.actualizarEventoFinAtencionPanaderia(siguienteFinAtencionPanaderia, empleadoPanaderia);
            heapEventos.add(siguienteFinAtencionPanaderia);
            // se actualiza el randomCUBase
            randomCUBase = tiempoAtencionPanaderia.getSiguienteRandomBase();
        }
        vectorEstadoActual.setSiguientePseudoCU(randomCUBase);

        return vectorEstadoActual;

    }

    public Servidor obtenerEmpleadoCaja(VectorEstadoNegocio vectorEstadoNegocio){
        Servidor empleadoCaja = vectorEstadoNegocio.getEmpleadoCaja();
        if (empleadoCaja.estaLibre()){
            return empleadoCaja;
        }
        else return null;
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
