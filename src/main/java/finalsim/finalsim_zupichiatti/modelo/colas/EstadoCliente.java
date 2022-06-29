package finalsim.finalsim_zupichiatti.modelo.colas;

import java.util.Hashtable;
import java.util.Map;

public class EstadoCliente {

    public static final String ESPERANDO_DESPENSA = "Esperando Despensa";

    public static final String ESPERANDO_PANADERIA = "Esperando Panaderia";

    public static final String ESPERANDO_CAJA = "Esperando Caja";

    public static final String SIENDO_ATENDIDO_DESPENSA = "Siendo Atendido Despensa";

    public static final String SIENDO_ATENDIDO_PANADERIA = "Siendo Atendido Panaderia";

    public static final String SIENDO_ATENDIDO_CAJA = "Siendo Atendido Caja";

    public static final String ATENCION_FINALIDA = "Atencion Finalizada";


    private static Map<String, EstadoCliente> estadosPosiblesMap;

    private String estado;

    private EstadoCliente(String estado){
        this.estado = estado;
    }

    //si no existe de antes la lista de estados la crea vacia
    private static void inicializarMap(){
        if(estadosPosiblesMap == null) estadosPosiblesMap = new Hashtable<>();
    }


    public static EstadoCliente getInstanceEsperandoDespensa(){
        inicializarMap();
        if(!estadosPosiblesMap.containsKey("ED")){
            estadosPosiblesMap.put("ED", new EstadoCliente(ESPERANDO_DESPENSA));
        }
        return estadosPosiblesMap.get("ED");
    }

    public static EstadoCliente getInstanceEsperandoPanaderia(){
        inicializarMap();
        if(!estadosPosiblesMap.containsKey("EP")){
            estadosPosiblesMap.put("EP", new EstadoCliente(ESPERANDO_PANADERIA));
        }
        return estadosPosiblesMap.get("EP");
    }

    public static EstadoCliente getInstanceEsperandoCaja(){
        inicializarMap();
        if(!estadosPosiblesMap.containsKey("EC")){
            estadosPosiblesMap.put("EC", new EstadoCliente(ESPERANDO_CAJA));
        }
        return estadosPosiblesMap.get("EC");
    }

    public static EstadoCliente getInstanceSiendoAtendidoDespensa(){
        inicializarMap();
        if(!estadosPosiblesMap.containsKey("SAD")){
            estadosPosiblesMap.put("SAD", new EstadoCliente(SIENDO_ATENDIDO_DESPENSA));
        }
        return estadosPosiblesMap.get("SAD");
    }

    public static EstadoCliente getInstanceSiendoAtendidoPanaderia(){
        inicializarMap();
        if(!estadosPosiblesMap.containsKey("SAP")){
            estadosPosiblesMap.put("SAP", new EstadoCliente(SIENDO_ATENDIDO_PANADERIA));
        }
        return estadosPosiblesMap.get("SAP");
    }

    public static EstadoCliente getInstanceSiendoAtendidoCaja(){
        inicializarMap();
        if(!estadosPosiblesMap.containsKey("SAC")){
            estadosPosiblesMap.put("SAC", new EstadoCliente(SIENDO_ATENDIDO_CAJA));
        }
        return estadosPosiblesMap.get("SAC");
    }

    public static EstadoCliente getInstanceAtencionFinalizada(){
        inicializarMap();
        if(!estadosPosiblesMap.containsKey("AF")){
            estadosPosiblesMap.put("AF", new EstadoCliente(ATENCION_FINALIDA));
        }
        return estadosPosiblesMap.get("AF");
    }

    public String getEstado(){
        return estado;
    }
}
