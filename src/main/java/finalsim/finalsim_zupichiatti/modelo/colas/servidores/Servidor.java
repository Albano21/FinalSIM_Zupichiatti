package finalsim.finalsim_zupichiatti.modelo.colas.servidores;

import finalsim.finalsim_zupichiatti.modelo.colas.Cliente;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Servidor {

    protected EstadoServidor estado;

    protected Cliente clienteActual;

    protected String nombre;

    protected int id;

    protected double momentoLiberacion;

    public boolean estaLibre(){
        return estado.estaLibre();
    }

    @Override
    public abstract Servidor clone();
}
