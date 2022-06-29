package finalsim.finalsim_zupichiatti.modelo.colas.servidores;

public class EmpleadoDespensa extends Servidor{

    public Servidor clone(){
        EmpleadoDespensa empleadoDespensa = new EmpleadoDespensa();
        empleadoDespensa.setEstado(this.estado);
        empleadoDespensa.setId(this.id);
        empleadoDespensa.setClienteActual(this.clienteActual);
        empleadoDespensa.setMomentoLiberacion(this.momentoLiberacion);
        empleadoDespensa.setNombre(this.nombre);
        return empleadoDespensa;
    }
}
