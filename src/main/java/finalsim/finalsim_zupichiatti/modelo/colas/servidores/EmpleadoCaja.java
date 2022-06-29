package finalsim.finalsim_zupichiatti.modelo.colas.servidores;

public class EmpleadoCaja extends Servidor{

    public Servidor clone(){
        EmpleadoCaja empleadoCaja = new EmpleadoCaja();
        empleadoCaja.setEstado(this.estado);
        empleadoCaja.setId(this.id);
        empleadoCaja.setClienteActual(this.clienteActual);
        empleadoCaja.setMomentoLiberacion(this.momentoLiberacion);
        empleadoCaja.setNombre(this.nombre);
        return empleadoCaja;
    }
}
