package finalsim.finalsim_zupichiatti.modelo.colas.eventos;

import finalsim.finalsim_zupichiatti.controller.cambioDistribucion.CambioDistribucionNormalConvolucion;
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
public class EventoFinAtencionDespensa extends Evento{

    private Pseudoaleatorio randomTiempoAtencion;
    private float tiempoAtencion;

    public EventoFinAtencionDespensa(){
        this.setNombreEvento("Fin At. Despensa");
    }

    // aca va la logica del evento,
    @Override
    public VectorEstadoNegocio procesarEvento(VectorEstadoNegocio estadoAnterior,
                                              ParametrosGenerador parametrosGenerador,
                                              Pseudoaleatorio randomCUBase,
                                              IGeneradorRandom generadorRandom,
                                              ParametrosNegocio parametrosNegocio,
                                              Map<String, ICambioDistribucion> generadoresVariableAleatoria,
                                              TSBHeap<Evento> heapEventos) {

        VectorEstadoNegocio vectorEstadoActual = (VectorEstadoNegocio) estadoAnterior.clone();
        vectorEstadoActual.setReloj(this.getMomentoEvento());
        vectorEstadoActual.setNombreEvento(this.nombreEvento);

        Servidor empleadoDespensaAnterior = cliente.getServidorActual();

        Cliente clienteActual = vectorEstadoActual.buscarClientePorId(cliente.getNumeroCliente());

        Servidor empleadoDespensa = vectorEstadoActual.getEmpleadoDespensa();

        //-------------------MANEJO DEL FIN AT ACTUAL-------------------
        // verifica si esta disponible el empleado de caja
        Servidor empleadoCaja = this.obtenerEmpleadoCaja(vectorEstadoActual);
        // Si el empleado caja esta ocupado
        if (empleadoCaja == null){
            vectorEstadoActual.agregarClienteColaCaja(clienteActual);
            clienteActual.setEstado(EstadoCliente.getInstanceEsperandoCaja());
        }
        // si el empleado caja esta libre
        else{
            // actualiza cliente
            clienteActual.setEstado(EstadoCliente.getInstanceSiendoAtendidoCaja());
            clienteActual.setServidorActual(empleadoCaja);
            // actualiza servidor
            empleadoCaja.setEstado(EstadoServidor.getInstanceServidorOcupado());
            empleadoCaja.setClienteActual(clienteActual);
            // aca se calcula la cantidad de articulos
            // ver si esta bien pasarle el randomCUBase
            Pseudoaleatorio randomCantidadArticulos = generadorRandom.siguientePseudoAleatoreo(randomCUBase, parametrosGenerador);
            int cantidadArticulos;
            if (randomCantidadArticulos.getRandom() < 0.4){
                cantidadArticulos = 1;
            }
            else{
                if(randomCantidadArticulos.getRandom() < 0.75){
                    cantidadArticulos = 2;
                }
                else{
                    cantidadArticulos = 3;
                }
            }
            // setea los valores para calcular la variable demora por articulo
            ParametrosCambioDistribucion parametrosCambioDistribucion = new ParametrosCambioDistribucion();
            parametrosCambioDistribucion.setMedia(parametrosNegocio.getMediaDemoraCaja());
            parametrosCambioDistribucion.setDesvEst(parametrosNegocio.getDesviacionEstCaja());
            // busco el generador normal
            CambioDistribucionNormalConvolucion generadorNormal = (CambioDistribucionNormalConvolucion) generadoresVariableAleatoria.get(ConstantesCambioDistribucion.NORMAL_CONVOLUCION);
            // calculo variable demora por articulo y arma los vectores de randoms y demora por articulo
            VaribaleAleatoria tiempoAtencionArticulo;
            Pseudoaleatorio[] randomsDemoraPorArticulo = new Pseudoaleatorio[3];
            float[] tiemposDemoraPorArticulo = new float[3];
            for (int i = 0; i < cantidadArticulos; i++) {
                tiempoAtencionArticulo = generadorNormal
                        .siguienteRandom(parametrosCambioDistribucion, parametrosGenerador, randomCUBase);
                randomsDemoraPorArticulo[i] = tiempoAtencionArticulo.getSiguienteRandomBase();
                tiemposDemoraPorArticulo[i] = tiempoAtencionArticulo.getRandomGenerado();
                randomCUBase = tiempoAtencionArticulo.getSiguienteRandomBase();
            }
            // calcula el tiempo total
            double tiempoAtencionCaja = 0;
            for (float t : tiemposDemoraPorArticulo) {
                if (t < 0){
                    t = t*(-1);
                }
                tiempoAtencionCaja += t;
            }
            tiempoAtencionCaja = CommonFunc.round(tiempoAtencionCaja ,4);

            // crea evento fin atencion caja
            EventoFinAtencionCaja eventoFinAtencionCaja = new EventoFinAtencionCaja();
            eventoFinAtencionCaja.setRandomCantidadArticulos(randomCantidadArticulos);
            eventoFinAtencionCaja.setCantidadArticulos(cantidadArticulos);
            eventoFinAtencionCaja.setRandomsDemoraPorArticulo(randomsDemoraPorArticulo);
            eventoFinAtencionCaja.setTiemposDemoraPorArticulo(tiemposDemoraPorArticulo);
            eventoFinAtencionCaja.setTiempoAtencionCaja(tiempoAtencionCaja);
            double momentoEventoFinAtCaja = vectorEstadoActual.getReloj()+eventoFinAtencionCaja.getTiempoAtencionCaja();
            momentoEventoFinAtCaja = CommonFunc.round(momentoEventoFinAtCaja ,4);
            eventoFinAtencionCaja.setMomentoEvento(momentoEventoFinAtCaja);
            eventoFinAtencionCaja.setCliente(clienteActual);
            // se actualiza el evento fin atencion caja
            vectorEstadoActual.actualizarEventoFinAtencionCaja(eventoFinAtencionCaja);
            heapEventos.add(eventoFinAtencionCaja);
            // actualiza el acumulador ded tiempo ocioso empleado caja
            vectorEstadoActual.acumularTiempoOcioso();
        }
        //--------------------MANEJO PROXIMA ATENCION DESPENSA---------------
        Cliente clienteEsperaColaDespensa = vectorEstadoActual.getSiguienteClienteColaDespensa();
        // si no hay clientes en la cola
        if(clienteEsperaColaDespensa == null){
            //actualiza el servidor
            empleadoDespensa.setEstado(EstadoServidor.getInstanceServidorLibre());
            empleadoDespensa.setClienteActual(null);
            //el evento en el vector
            vectorEstadoActual.actualizarEventoFinAtencionDespensa(null);
        }
        // si hay clientes en cola
        else{
            // se recupera el cliente de la lista del cliente, actualiza referencia
            clienteEsperaColaDespensa = vectorEstadoActual.buscarClientePorId(clienteEsperaColaDespensa.getNumeroCliente());
            // se actualiza el cliente
            clienteEsperaColaDespensa.setEstado(EstadoCliente.getInstanceSiendoAtendidoDespensa());
            clienteEsperaColaDespensa.setServidorActual(empleadoDespensa);
            // se actualiza el servidor
            empleadoDespensa.setEstado(EstadoServidor.getInstanceServidorOcupado());
            empleadoDespensa.setClienteActual(clienteEsperaColaDespensa);
            // setea los valores para calcular la variable demora
            ParametrosCambioDistribucion parametrosCambioDistribucion = new ParametrosCambioDistribucion();
            parametrosCambioDistribucion.setUnifA(parametrosNegocio.getMinimoDemoraDespensa());
            parametrosCambioDistribucion.setUnifB(parametrosNegocio.getMaximoDemoraDespensa());
            parametrosCambioDistribucion.setPresicion(parametrosGenerador.getPresicion());
            // busco el generador UniformeAB
            CambioDistribucionUniformeAB generadorUniformeAB = (CambioDistribucionUniformeAB) generadoresVariableAleatoria.get(ConstantesCambioDistribucion.UNIFORME);
            // calculo variable
            VaribaleAleatoria tiempoAtencionDespensa = generadorUniformeAB
                    .siguienteRandom(parametrosCambioDistribucion, parametrosGenerador, randomCUBase);
            // crea evento fin at despensa con sus datos
            EventoFinAtencionDespensa siguienteFinAtencionDespensa = new EventoFinAtencionDespensa();
            siguienteFinAtencionDespensa.setRandomTiempoAtencion(randomCUBase);
            siguienteFinAtencionDespensa.setTiempoAtencion(tiempoAtencionDespensa.getRandomGenerado());
            double momentoProxEventoFinAtDespensa = vectorEstadoActual.getReloj()+ siguienteFinAtencionDespensa.getTiempoAtencion();
            momentoProxEventoFinAtDespensa = CommonFunc.round( momentoProxEventoFinAtDespensa ,4);
            siguienteFinAtencionDespensa.setMomentoEvento(momentoProxEventoFinAtDespensa);
            siguienteFinAtencionDespensa.setCliente(clienteEsperaColaDespensa);
            // se actualiza el evento fin at despensa en el vector
            vectorEstadoActual.actualizarEventoFinAtencionDespensa(siguienteFinAtencionDespensa);
            heapEventos.add(siguienteFinAtencionDespensa);
            // se actualiza el randomCUBase
            randomCUBase = tiempoAtencionDespensa.getSiguienteRandomBase();
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
    public Object clone() throws CloneNotSupportedException{
        EventoFinAtencionDespensa evento = new EventoFinAtencionDespensa();
        evento.setMomentoEvento(super.getMomentoEvento());
        evento.setRandomTiempoAtencion( (Pseudoaleatorio) randomTiempoAtencion.clone());
        evento.setTiempoAtencion(tiempoAtencion);
        return evento;
    }


}
