package finalsim.finalsim_zupichiatti.fxcontroller;

import finalsim.finalsim_zupichiatti.dto.VectorEstadoDtoActual;
import finalsim.finalsim_zupichiatti.modelo.colas.VectorEstadoNegocio;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Lazy
public class ResultadoFxController {


    @FXML
    private TableView<VectorEstadoDtoActual> tv_Simulacion;

    @FXML
    private TextField tf_cantidadArticulos;

    @FXML
    private TextField tf_porcentajeOciosoCaja;

    @FXML
    private TextField tf_colaMaxDespensa;

    @FXML
    private TextField tf_colaMaxPanaderia;

    @FXML
    private TextField tf_cajaAyudaA;


    public void mostrarResultadoSimulacion(List<VectorEstadoNegocio> resultadoSimulacion){


    }

    public List<VectorEstadoDtoActual> mapVectoresEstado(List<VectorEstadoNegocio> resultado){
        List<VectorEstadoDtoActual> resultadoActual = new ArrayList<>();
        for (VectorEstadoNegocio vector : resultado){
            VectorEstadoDtoActual vectorEstadoDtoActual = mapVectorEstado(vector);
            resultadoActual.add(vectorEstadoDtoActual);
        }
        return resultadoActual;
    }

    public VectorEstadoDtoActual mapVectorEstado(VectorEstadoNegocio vector){

        VectorEstadoDtoActual vectorEstadoDtoActual = new VectorEstadoDtoActual();

        // set colas
        vectorEstadoDtoActual.setColaDespensa(vector.getColaDespensa().size());
        vectorEstadoDtoActual.setColaPanaderia(vector.getColaPanaderia().size());
        vectorEstadoDtoActual.setColaCaja(vector.getColaCaja().size());

        // primitivos
        vectorEstadoDtoActual.setNombreEvento(vector.getNombreEvento());
        vectorEstadoDtoActual.setReloj(vector.getReloj());

        // contadores y acumuladores
        vectorEstadoDtoActual.setContadorArticulos(vector.getContadorArticulos());
        vectorEstadoDtoActual.setAcumuladorTiempoOciosoCaja(vector.getAcumuladorTiempoOciosoCaja());
        vectorEstadoDtoActual.setMaximoLargoColaDespensa(vector.getMaximoLargoColaDespensa());
        vectorEstadoDtoActual.setMaximoLargoColaPanaderia(vector.getMaximoLargoColaPanaderia());
        vectorEstadoDtoActual.setContadorClientes(vector.getContadorClientes());

        // set Eventos
        vectorEstadoDtoActual.setEventoFinAtencionDespensa(vector.getFinAtencionDespensa());
        vectorEstadoDtoActual.setEventoFinAtencionPanaderia1(vector.getFinAtencionPanaderia()[0]);
        vectorEstadoDtoActual.setEventoFinAtencionPanaderia2(vector.getFinAtencionPanaderia()[1]);
        vectorEstadoDtoActual.setEventoFinAtencionCaja(vector.getFinAtencionCaja());

        //set Empleados
        vectorEstadoDtoActual.setEmpleadoDespensa(vector.getEmpleadoDespensa());
        vectorEstadoDtoActual.setEmpleadoPanaderia1(vector.getEmpleadosPanaderia().get(0));
        vectorEstadoDtoActual.setEmpleadoPanaderia2(vector.getEmpleadosPanaderia().get(1));
        vectorEstadoDtoActual.setEmpleadoCaja(vector.getEmpleadoCaja());

        return vectorEstadoDtoActual;

    }









}
