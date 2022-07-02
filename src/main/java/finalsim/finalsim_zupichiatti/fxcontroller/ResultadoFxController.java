package finalsim.finalsim_zupichiatti.fxcontroller;

import finalsim.finalsim_zupichiatti.dto.VectorEstadoDtoActual;
import finalsim.finalsim_zupichiatti.modelo.colas.VectorEstadoNegocio;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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

    private void generarColumnasSimulacion(){

        // reloj y nombre
        TableColumn<VectorEstadoDtoActual, String> nombreEvEcolumna = new TableColumn<>();
        nombreEvEcolumna.setCellValueFactory(new PropertyValueFactory<>("nombreEvento"));
        nombreEvEcolumna.setText("Nom. Evento");
        
        TableColumn<VectorEstadoDtoActual, Float> relojColumna = new TableColumn<>();
        relojColumna.setCellValueFactory(new PropertyValueFactory<>("reloj"));
        relojColumna.setText("Reloj (min)");

        // Evento llegada Cliente
        TableColumn<VectorEstadoDtoActual, String> rndEventoLlegadaCliente = new TableColumn<>();
        rndEventoLlegadaCliente.setCellValueFactory(cellData -> cellData.getValue().getEventoLlegadaCliente()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoLlegadaCliente().getRandomProxLlegada().getRandom())));
        rndEventoLlegadaCliente.setText("RND Llegada Cliente");
        
        TableColumn<VectorEstadoDtoActual, String> tiempoEventoLlegadaCliente = new TableColumn<>();
        tiempoEventoLlegadaCliente.setCellValueFactory(cellData -> cellData.getValue().getEventoLlegadaCliente()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoLlegadaCliente().getTiempoHastaProximaLlegada())));
        tiempoEventoLlegadaCliente.setText("Tiempo Llegada Cliente");

        TableColumn<VectorEstadoDtoActual,String> momentoEventoLLegadaCliente = new TableColumn<>();
        momentoEventoLLegadaCliente.setCellValueFactory(cellData -> cellData.getValue().getEventoLlegadaCliente()==null ? new SimpleStringProperty("") : new SimpleStringProperty(Float.toString(cellData.getValue().getEventoLlegadaCliente().getMomentoEvento())));
        momentoEventoLLegadaCliente.setText("Momento Evento Llegada Cliente");

        TableColumn<VectorEstadoDtoActual, String> rndDestinoCliente = new TableColumn<>();
        rndDestinoCliente.setCellValueFactory(cellData -> cellData.getValue().getEventoLlegadaCliente()==null ? new SimpleStringProperty("") : new SimpleStringProperty(Float.toString(cellData.getValue().getEventoLlegadaCliente().getRandomDestinoCliente().getRandom())));
        rndDestinoCliente.setText("RND Destino Cliente");

        TableColumn<VectorEstadoDtoActual, String> destinoCliente = new TableColumn<>();
        destinoCliente.setCellValueFactory(cellData -> cellData.getValue().getEventoLlegadaCliente()==null? new SimpleStringProperty("") : new SimpleStringProperty(cellData.getValue().getEventoLlegadaCliente().getNombreDestinoCliente()));
        destinoCliente.setText("Destino Cliente");

        // Evento Fin At. Despensa
        TableColumn<VectorEstadoDtoActual, String > rndEventoFinAtDespensa = new TableColumn<>();
        rndEventoFinAtDespensa.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionDespensa()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionDespensa().getRandomTiempoAtencion().getRandom())));
        rndEventoFinAtDespensa.setText("RND Tiempo Atencion Despensa");

        TableColumn<VectorEstadoDtoActual, String > tiempoEventoFinAtDespensa = new TableColumn<>();
        tiempoEventoFinAtDespensa.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionDespensa()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionDespensa().getTiempoAtencion())));
        tiempoEventoFinAtDespensa.setText("Tiempo Atencion Despensa");

        TableColumn<VectorEstadoDtoActual, String > momentoEventoFinAtDespensa = new TableColumn<>();
        momentoEventoFinAtDespensa.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionDespensa()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionDespensa().getMomentoEvento())));
        momentoEventoFinAtDespensa.setText("Momento Tiempo Atencion Despensa");

        // Evento Fin At. Panaderia 1
        TableColumn<VectorEstadoDtoActual, String > rndEventoFinAtPanaderia1 = new TableColumn<>();
        rndEventoFinAtPanaderia1.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionPanaderia1()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionPanaderia1().getRandomTiempoAtencion().getRandom())));
        rndEventoFinAtPanaderia1.setText("RND Tiempo Atencion Panaderia 1");

        TableColumn<VectorEstadoDtoActual, String > tiempoEventoFinAtPanaderia1 = new TableColumn<>();
        tiempoEventoFinAtPanaderia1.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionPanaderia1()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionPanaderia1().getTiempoAtencion())));
        tiempoEventoFinAtPanaderia1.setText("Tiempo Atencion Panaderia 1");

        TableColumn<VectorEstadoDtoActual, String > momentoEventoFinAtPanaderia1 = new TableColumn<>();
        momentoEventoFinAtPanaderia1.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionPanaderia1()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionPanaderia1().getMomentoEvento())));
        momentoEventoFinAtPanaderia1.setText("Momento Atencion Panaderia 1");

        // Evento Fin At. Panaderia 2
        TableColumn<VectorEstadoDtoActual, String > rndEventoFinAtPanaderia2 = new TableColumn<>();
        rndEventoFinAtPanaderia2.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionPanaderia2()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionPanaderia2().getRandomTiempoAtencion().getRandom())));
        rndEventoFinAtPanaderia2.setText("RND Tiempo Atencion Panaderia 2");

        TableColumn<VectorEstadoDtoActual, String > tiempoEventoFinAtPanaderia2 = new TableColumn<>();
        tiempoEventoFinAtPanaderia2.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionPanaderia2()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionPanaderia2().getTiempoAtencion())));
        tiempoEventoFinAtPanaderia2.setText("Tiempo Atencion Panaderia 2");

        TableColumn<VectorEstadoDtoActual, String > momentoEventoFinAtPanaderia2 = new TableColumn<>();
        momentoEventoFinAtPanaderia2.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionPanaderia2()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionPanaderia2().getMomentoEvento())));
        momentoEventoFinAtPanaderia2.setText("Momento Atencion Panaderia 2");

        // Evento Fin At. Caja
        TableColumn<VectorEstadoDtoActual, String> rndCantidadArticulos = new TableColumn<>();
        rndCantidadArticulos.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionCaja()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionCaja().getRandomCantidadArticulos().getRandom())));
        rndCantidadArticulos.setText("RND Cantidad Articulos");

        TableColumn<VectorEstadoDtoActual, String> cantidadArticulos = new TableColumn<>();
        cantidadArticulos.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionCaja()==null? new SimpleStringProperty(""): new SimpleStringProperty(Integer.toString(cellData.getValue().getEventoFinAtencionCaja().getCantidadArticulos())));
        cantidadArticulos.setText("Cantidad Articulos");






    }









}
