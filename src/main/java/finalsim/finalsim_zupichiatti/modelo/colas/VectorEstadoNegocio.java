package finalsim.finalsim_zupichiatti.modelo.colas;

import finalsim.finalsim_zupichiatti.modelo.Pseudoaleatorio;
import finalsim.finalsim_zupichiatti.modelo.colas.eventos.*;
import finalsim.finalsim_zupichiatti.modelo.colas.servidores.EmpleadoPanaderia;
import finalsim.finalsim_zupichiatti.modelo.colas.servidores.Servidor;
import lombok.Data;
import lombok.SneakyThrows;

import javax.print.PrintService;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class VectorEstadoNegocio {

    private String nombreEvento;
    private float reloj;
    // Eventos
    private EventoLlegadaCliente proximaLlegadaCliente;
    private EventoFinAtencionDespensa finAtencionDespensa;
    private EventoFinAtencionPanaderia[] finAtencionPanaderia;
    private EventoFinAtencionCaja finAtencionCaja;
    private EventoFinSimulacion finSimulacion;
    // Servidores
    private Servidor empleadoDespensa;
    private List<Servidor> empleadosPanaderia;
    private Servidor empleadoCaja;
    // Colas
    private Queue<Cliente> colaDespensa;
    private Queue<Cliente> colaPanaderia;
    private Queue<Cliente> colaCaja;
    // Acumuladores para estadisticas
    // A)
    private int contadorArticulos;
    // B)
    private float acumuladorTiempoOciosoCaja;
    // C)
    private int maximoLargoColaDespensa;
    private int maximoLargoColaPanaderia;
    // Clientes
    private List<Cliente> clientes;

    private Pseudoaleatorio siguientePseudoCU;


    @Override
    @SneakyThrows
    public Object clone(){
        VectorEstadoNegocio nuevoVector = new VectorEstadoNegocio();
        // Seteo de valores
        nuevoVector.setReloj(reloj);
        nuevoVector.setContadorArticulos(contadorArticulos);
        nuevoVector.setAcumuladorTiempoOciosoCaja(acumuladorTiempoOciosoCaja);
        nuevoVector.setMaximoLargoColaDespensa(maximoLargoColaDespensa);
        nuevoVector.setMaximoLargoColaPanaderia(maximoLargoColaPanaderia);
        nuevoVector.setNombreEvento(nombreEvento);

        // Servidores
        this.clonarServidores(nuevoVector);

        // Colas
        nuevoVector.setColaDespensa(new ArrayDeque<>(colaDespensa));
        nuevoVector.setColaPanaderia(new ArrayDeque<>(colaPanaderia));
        nuevoVector.setColaCaja(new ArrayDeque<>(colaCaja));
        // Clientes
        this.clonarClientes(nuevoVector);
        // Eventos
        this.clonarEventos(nuevoVector);
        nuevoVector.setFinSimulacion(finSimulacion);
        return nuevoVector;


    }

    private void clonarServidores(VectorEstadoNegocio nuevoVector){

        Servidor nuevoEmpleadoDespensa = empleadoDespensa.clone();
        List<Servidor> nuevosEmpleadosPanaderia =
                empleadosPanaderia.stream().map(empleado -> (EmpleadoPanaderia) empleado.clone())
                        .collect(Collectors.toCollection(LinkedList::new));
        Servidor nuevoEmpleadoCaja = empleadoCaja.clone();

        nuevoVector.setEmpleadoDespensa(nuevoEmpleadoDespensa);
        nuevoVector.setEmpleadosPanaderia(nuevosEmpleadosPanaderia);
        nuevoVector.setEmpleadoCaja(nuevoEmpleadoCaja);
    }

    private void clonarClientes(VectorEstadoNegocio nuevoVector){
        List<Cliente> nuevosClientes = new LinkedList<>();
        clientes.forEach(cliente -> nuevosClientes.add((Cliente) cliente.clone()));
        nuevoVector.setClientes(nuevosClientes);
    }

    @SneakyThrows
    private void clonarEventos(VectorEstadoNegocio nuevoVector){
        EventoFinAtencionPanaderia[] finAtencionPanaderias =
                 new EventoFinAtencionPanaderia[this.finAtencionPanaderia.length];

        for (int i = 0; i < finAtencionPanaderias.length; i++){
            if (this.finAtencionPanaderia[i] != null){
                finAtencionPanaderias[i] = (EventoFinAtencionPanaderia) this.finAtencionPanaderia[i].clone();
            }
        }

        //aca no seria clone?
        nuevoVector.setProximaLlegadaCliente(this.getProximaLlegadaCliente());

        nuevoVector.setFinAtencionDespensa((EventoFinAtencionDespensa) this.finAtencionDespensa.clone());
        nuevoVector.setFinAtencionPanaderia(finAtencionPanaderias);
        nuevoVector.setFinAtencionCaja((EventoFinAtencionCaja) this.finAtencionCaja.clone());
    }

    public void eliminarClienteAtendido(Cliente clienteAtencionFinalizada) {

        Iterator<Cliente> clienteIterator = clientes.iterator();
        while(clienteIterator.hasNext()){
            Cliente cliente = clienteIterator.next();
            if(cliente.equals(clienteAtencionFinalizada)){
                clienteIterator.remove();
                break;
            }
        }

    }

    // aca van mas metodos segun lo que me haga falta actualizar desde cada evento
    public void agregarCliente(Cliente cliente){
        if (clientes == null) clientes = new LinkedList<>();
        clientes.add(cliente);
    }

    public void agregarClienteColaDespensa(Cliente cliente){
        if (colaDespensa == null) colaDespensa = new ArrayDeque<>();
        colaDespensa.add(cliente);
    }

    public void actualizarEventoFinAtencionDespensa(EventoFinAtencionDespensa evento){
        finAtencionDespensa = evento;
    }

    public void actualizarMaximoLargoColaDespensa(){
        if (maximoLargoColaDespensa < colaDespensa.size()){
            maximoLargoColaDespensa = colaDespensa.size();
        }
    }

    public int getProximoNumeroCliente(){
        return (clientes.size()+1);
    }

    public void agregarClienteColaPanaderia(Cliente cliente){
        if (colaPanaderia == null) colaPanaderia = new ArrayDeque<>();
        colaPanaderia.add(cliente);
    }

    public void actualizarMaximoLargoColaPanaderia(){
        if (maximoLargoColaPanaderia < colaPanaderia.size()){
            maximoLargoColaPanaderia = colaPanaderia.size();
        }
    }

    public void actualizarEventoFinAtencionPanaderia(EventoFinAtencionPanaderia evento, Servidor empleadoPanaderia){
        finAtencionPanaderia[empleadoPanaderia.getId()-1] = evento;
    }

    public Cliente buscarClientePorId(int id){
        for(Cliente cliente: clientes){
            if(cliente.tieneId(id)){
                return cliente;
            }
        }
        return null;
    }

    public void agregarClienteColaCaja(Cliente cliente){
        if(colaCaja == null) colaCaja = new ArrayDeque<>();
        colaCaja.add(cliente);
    }

    public void actualizarEventoFinAtencionCaja(EventoFinAtencionCaja evento){
        finAtencionCaja = evento;
    }

    public void acumularTiempoOcioso(){
        float tiepoAAcumular = reloj - empleadoCaja.getMomentoLiberacion();
        acumuladorTiempoOciosoCaja += tiepoAAcumular;
    }

    public Cliente getSiguienteClienteColaDespensa(){
        if (colaDespensa != null) {
            return colaDespensa.poll();
        }
        return null;
    }




}
