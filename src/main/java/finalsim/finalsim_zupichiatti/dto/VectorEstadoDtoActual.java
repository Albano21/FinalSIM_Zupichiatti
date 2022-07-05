package finalsim.finalsim_zupichiatti.dto;

import finalsim.finalsim_zupichiatti.modelo.colas.eventos.*;
import finalsim.finalsim_zupichiatti.modelo.colas.servidores.Servidor;
import lombok.Data;

@Data
public class VectorEstadoDtoActual {

    private String nombreEvento;
    private double reloj;

    private EventoLlegadaCliente eventoLlegadaCliente;
    private EventoFinAtencionDespensa eventoFinAtencionDespensa;
    private EventoFinAtencionPanaderia eventoFinAtencionPanaderia1;
    private EventoFinAtencionPanaderia eventoFinAtencionPanaderia2;
    private EventoFinAtencionCaja eventoFinAtencionCaja;
    private EventoFinSimulacion eventoFinSimulacion;

    private Servidor empleadoDespensa;
    private Servidor empleadoPanaderia1;
    private Servidor empleadoPanaderia2;
    private Servidor empleadoCaja;

    private int colaDespensa;
    private int colaPanaderia;
    private int colaCaja;

    private int contadorClientes;

    // punto A
    private int contadorArticulos;
    // punto B
    private double acumuladorTiempoOciosoCaja;
    // punto C
    private int maximoLargoColaDespensa;
    private int maximoLargoColaPanaderia;

}
