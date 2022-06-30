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
public class EventoFinAtencionCaja extends Evento{

    private Pseudoaleatorio randomCantidadArticulos;

    private int cantidadArticulos;

    private Pseudoaleatorio[] randomsDemoraPorArticulo;

    private float[] tiemposDemoraPorArticulo;
    private float tiempoAtencionCaja;

    private Cliente clienteAtencionFinalizada;

    public EventoFinAtencionCaja(){
        this.setNombreEvento("Fin At. Caja");
    }

    // aca va toda la logica, se supone que aca se muere el objeto cliente
    @Override
    public VectorEstadoNegocio procesarEvento(VectorEstadoNegocio estadoAnterior,
                                              ParametrosGenerador parametrosGenerador,
                                              Pseudoaleatorio randomCUBase,
                                              IGeneradorRandom generadorRandom,
                                              ParametrosNegocio parametrosNegocio,
                                              ICambioDistribucion generadorVariableAleatoria,
                                              TSBHeap<Evento> heapEventos) {

        VectorEstadoNegocio vectorEstadoActual = (VectorEstadoNegocio) estadoAnterior.clone();
        vectorEstadoActual.setReloj(this.momentoEvento);
        vectorEstadoActual.setNombreEvento(this.nombreEvento);
        Servidor empleadoCajaAnterior = cliente.getServidorActual();
        Cliente clienteActual = vectorEstadoActual.buscarClientePorId(cliente.getNumeroCliente());
        Servidor empleadoCaja = vectorEstadoActual.getEmpleadoCaja();

        this.setClienteAtencionFinalizada(clienteActual);

        Cliente siguienteClienteCola = vectorEstadoActual.getSiguienteClienteColaCaja();

        //----------------------------MANEJO PROXIMA ATENCION CAJA-------------------------
        //si no hay clientes en cola de caja
        if(siguienteClienteCola == null){
            // actualiza el servidor
            empleadoCaja.setEstado(EstadoServidor.getInstanceServidorLibre());
            empleadoCaja.setMomentoLiberacion(this.momentoEvento);
            empleadoCaja.setClienteActual(null);
            // actualiza el evento en el vector
            vectorEstadoActual.actualizarEventoFinAtencionCaja(null);
        }
        // si hay clientes en la cola
        else{
            // actualiza la referencia al siguiente cliente cola
            siguienteClienteCola = vectorEstadoActual.buscarClientePorId(siguienteClienteCola.getNumeroCliente());
            // actualiza el cliente
            siguienteClienteCola.setEstado(EstadoCliente.getInstanceSiendoAtendidoCaja());
            siguienteClienteCola.setServidorActual(empleadoCaja);
            // actualiza el servidor
            empleadoCaja.setClienteActual(siguienteClienteCola);
            empleadoCaja.setEstado(EstadoServidor.getInstanceServidorOcupado());
            // aca se calcula la cantidad de articulos
            // ver si esta bien pasarle el randomCUBase
            Pseudoaleatorio randomCantidadArticulos = generadorRandom.siguientePseudoAleatoreo(randomCUBase, parametrosGenerador);
            int cantidadArticulos;
            if (randomCantidadArticulos.getRandom() <= 0.39){
                cantidadArticulos = 1;
            }
            else{
                if(randomCantidadArticulos.getRandom() <= 0.74){
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
            // calculo variable demora por articulo y arma los vectores de randoms y demora por articulo
            VaribaleAleatoria tiempoAtencionArticulo;
            Pseudoaleatorio[] randomsDemoraPorArticulo = new Pseudoaleatorio[cantidadArticulos];
            float[] tiemposDemoraPorArticulo = new float[cantidadArticulos];
            for (int i = 0; i < cantidadArticulos; i++) {
                tiempoAtencionArticulo = generadorVariableAleatoria
                        .siguienteRandom(parametrosCambioDistribucion, parametrosGenerador, randomCUBase);
                randomsDemoraPorArticulo[i] = tiempoAtencionArticulo.getSiguienteRandomBase();
                tiemposDemoraPorArticulo[i] = tiempoAtencionArticulo.getRandomGenerado();
                randomCUBase = tiempoAtencionArticulo.getSiguienteRandomBase();
            }
            // calcula el tiempo total
            float tiempoAtencionCaja = 0;
            for (float t : tiemposDemoraPorArticulo) {
                tiempoAtencionCaja += t;
            }

            // crea evento fin atencion caja
            EventoFinAtencionCaja eventoFinAtencionCaja = new EventoFinAtencionCaja();
            eventoFinAtencionCaja.setRandomCantidadArticulos(randomCantidadArticulos);
            eventoFinAtencionCaja.setCantidadArticulos(cantidadArticulos);
            eventoFinAtencionCaja.setRandomsDemoraPorArticulo(randomsDemoraPorArticulo);
            eventoFinAtencionCaja.setTiemposDemoraPorArticulo(tiemposDemoraPorArticulo);
            eventoFinAtencionCaja.setTiempoAtencionCaja(tiempoAtencionCaja);
            eventoFinAtencionCaja.setMomentoEvento(vectorEstadoActual.getReloj()+eventoFinAtencionCaja.getTiempoAtencionCaja());
            eventoFinAtencionCaja.setCliente(clienteActual);
            // se actualiza el evento fin atencion caja
            vectorEstadoActual.actualizarEventoFinAtencionCaja(eventoFinAtencionCaja);
            heapEventos.add(eventoFinAtencionCaja);
            // no se acumula el tiempo ocioso ya que viene de estar ocupado con el cliente anterior

        }

        vectorEstadoActual.incrementarContadorDeArticulos(cantidadArticulos);

        vectorEstadoActual.setSiguientePseudoCU(randomCUBase);
        return vectorEstadoActual;



    }

    @Override
    public Object clone() throws CloneNotSupportedException{
        EventoFinAtencionCaja evento = new EventoFinAtencionCaja();
        evento.setRandomCantidadArticulos((Pseudoaleatorio) randomCantidadArticulos.clone());
        evento.setCantidadArticulos(cantidadArticulos);
        evento.setRandomsDemoraPorArticulo(randomsDemoraPorArticulo.clone());
        evento.setTiemposDemoraPorArticulo(tiemposDemoraPorArticulo.clone());
        evento.setTiempoAtencionCaja(tiempoAtencionCaja);
        evento.setMomentoEvento(super.getMomentoEvento());

        return evento;

    }
}
