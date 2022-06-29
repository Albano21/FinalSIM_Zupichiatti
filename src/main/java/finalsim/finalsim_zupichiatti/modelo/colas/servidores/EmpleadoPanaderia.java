package finalsim.finalsim_zupichiatti.modelo.colas.servidores;

public class EmpleadoPanaderia extends Servidor{

    public Servidor clone(){
        EmpleadoPanaderia empleadoPanaderia = new EmpleadoPanaderia();
        empleadoPanaderia.setEstado(this.estado);
        empleadoPanaderia.setId(this.id);
        empleadoPanaderia.setClienteActual(this.clienteActual);
        empleadoPanaderia.setMomentoLiberacion(this.momentoLiberacion);
        empleadoPanaderia.setNombre(this.nombre);
        return empleadoPanaderia;
    }
}
