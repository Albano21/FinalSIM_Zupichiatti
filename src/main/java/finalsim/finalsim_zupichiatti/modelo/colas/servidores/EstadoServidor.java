package finalsim.finalsim_zupichiatti.modelo.colas.servidores;

import java.util.Hashtable;
import java.util.Map;

public class EstadoServidor {

    public static final String SERVIDOR_LIBRE = "Libre";
    public static final String SERVIDOR_OCUPADO = "Ocupado";
    private static Map<String, EstadoServidor> estadosPosiblesServidor;
    private String estado;

    private EstadoServidor(String estado){
        this.estado = estado;
    }

    public String getEstado(){
        return estado;
    }

    public static EstadoServidor getInstanceServidorLibre(){
        inicializarMap();
        if(!estadosPosiblesServidor.containsKey(SERVIDOR_LIBRE)){
            estadosPosiblesServidor.put(SERVIDOR_LIBRE, new EstadoServidor(SERVIDOR_LIBRE));
        }
        return estadosPosiblesServidor.get(SERVIDOR_LIBRE);
    }

    public static EstadoServidor getInstanceServidorOcupado(){
        inicializarMap();
        if(!estadosPosiblesServidor.containsKey(SERVIDOR_OCUPADO)){
            estadosPosiblesServidor.put(SERVIDOR_OCUPADO, new EstadoServidor(SERVIDOR_OCUPADO));
        }
        return estadosPosiblesServidor.get(SERVIDOR_OCUPADO);
    }

    private static void inicializarMap() {
        if(estadosPosiblesServidor == null) estadosPosiblesServidor = new Hashtable<>();
    }

    public boolean estaLibre(){
        return estado.equals(EstadoServidor.SERVIDOR_LIBRE);
    }


}
