package finalsim.finalsim_zupichiatti.controller.utils;


public class EstadoItvSimulacion {

    public static final String ACTUAL = "1 Caseta - 2 Circuitos - 2 Oficinas";
    public static final String DOS_CASETAS = "2 Casetas - 2 Circuitos - 2 Oficinas";
    public static final String TRES_OFIC = "1 Caseta - 2 Circuitos - 3 Oficinas";

    private String prompt;
    private int cantCaseta;
    private int cantCircuitosNave;
    private int cantOficinas;


    private EstadoItvSimulacion(){

    }

    private EstadoItvSimulacion(String prompt, int cantCaseta, int cantCircuitosNave, int cantOficinas) {
        this.prompt = prompt;
        this.cantCaseta = cantCaseta;
        this.cantCircuitosNave = cantCircuitosNave;
        this.cantOficinas = cantOficinas;
    }

    public static EstadoItvSimulacion getInstanciaActual(){
        return new EstadoItvSimulacion(ACTUAL,1,2,2);
    }

    public static EstadoItvSimulacion getInstanciaDosCasetas(){
        return new EstadoItvSimulacion(DOS_CASETAS,2,2,2);
    }
    public static EstadoItvSimulacion getInstanciaTresOficinas(){
        return new EstadoItvSimulacion(TRES_OFIC,1,2,3);
    }

    @Override
    public String toString() {
        return prompt;
    }

    public String getPrompt() {
        return prompt;
    }

    public int getCantCaseta() {
        return cantCaseta;
    }


    public int getCantCircuitosNave() {
        return cantCircuitosNave;
    }

    public int getCantOficinas() {
        return cantOficinas;
    }

}
