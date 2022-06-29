package finalsim.finalsim_zupichiatti.modelo.colas;

import finalsim.finalsim_zupichiatti.modelo.colas.servidores.Servidor;
import lombok.Data;

import java.util.Objects;

@Data
public class Cliente {

    private int numeroCliente;

    EstadoCliente estado;

    private Servidor servidorActual;

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return getNumeroCliente() == cliente.getNumeroCliente();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumeroCliente());
    }

    @Override
    public Object clone(){
        Cliente cliente = new Cliente();
        cliente.setNumeroCliente(numeroCliente);
        cliente.setServidorActual(servidorActual);
        cliente.setEstado(estado);
        return cliente;
    }

    public boolean tieneId(int numeroCliente) {
        return this.numeroCliente == numeroCliente;
    }

}
