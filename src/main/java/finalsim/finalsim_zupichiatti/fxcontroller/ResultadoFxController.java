package finalsim.finalsim_zupichiatti.fxcontroller;

import finalsim.finalsim_zupichiatti.controller.utils.CommonFunc;
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

        tv_Simulacion.getColumns().clear();
        List<VectorEstadoDtoActual> resultadoActual = this.mapVectoresEstado(resultadoSimulacion);

        VectorEstadoNegocio vectorFinSim = resultadoSimulacion.get(resultadoSimulacion.size()-1);
        this.calcularEstadisticas(vectorFinSim);
        tv_Simulacion.getItems().addAll(resultadoActual);
        tv_Simulacion.refresh();
        this.generarColumnasSimulacion();

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
        vectorEstadoDtoActual.setEventoLlegadaCliente(vector.getProximaLlegadaCliente());
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
        TableColumn<VectorEstadoDtoActual, String> nombreEvColumna = new TableColumn<>();
        nombreEvColumna.setCellValueFactory(new PropertyValueFactory<>("nombreEvento"));
        nombreEvColumna.setText("Nom. Evento");
        
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
        momentoEventoLLegadaCliente.setCellValueFactory(cellData -> cellData.getValue().getEventoLlegadaCliente()==null ? new SimpleStringProperty("") : new SimpleStringProperty(Double.toString(cellData.getValue().getEventoLlegadaCliente().getMomentoEvento())));
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
        momentoEventoFinAtDespensa.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionDespensa()==null? new SimpleStringProperty(""): new SimpleStringProperty(Double.toString(cellData.getValue().getEventoFinAtencionDespensa().getMomentoEvento())));
        momentoEventoFinAtDespensa.setText("Momento Tiempo Atencion Despensa");

        // Evento Fin At. Panaderia 1
        TableColumn<VectorEstadoDtoActual, String > rndEventoFinAtPanaderia1 = new TableColumn<>();
        rndEventoFinAtPanaderia1.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionPanaderia1()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionPanaderia1().getRandomTiempoAtencion().getRandom())));
        rndEventoFinAtPanaderia1.setText("RND Tiempo Atencion Panaderia 1");

        TableColumn<VectorEstadoDtoActual, String > tiempoEventoFinAtPanaderia1 = new TableColumn<>();
        tiempoEventoFinAtPanaderia1.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionPanaderia1()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionPanaderia1().getTiempoAtencion())));
        tiempoEventoFinAtPanaderia1.setText("Tiempo Atencion Panaderia 1");

        TableColumn<VectorEstadoDtoActual, String > momentoEventoFinAtPanaderia1 = new TableColumn<>();
        momentoEventoFinAtPanaderia1.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionPanaderia1()==null? new SimpleStringProperty(""): new SimpleStringProperty(Double.toString(cellData.getValue().getEventoFinAtencionPanaderia1().getMomentoEvento())));
        momentoEventoFinAtPanaderia1.setText("Momento Atencion Panaderia 1");

        // Evento Fin At. Panaderia 2
        TableColumn<VectorEstadoDtoActual, String > rndEventoFinAtPanaderia2 = new TableColumn<>();
        rndEventoFinAtPanaderia2.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionPanaderia2()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionPanaderia2().getRandomTiempoAtencion().getRandom())));
        rndEventoFinAtPanaderia2.setText("RND Tiempo Atencion Panaderia 2");

        TableColumn<VectorEstadoDtoActual, String > tiempoEventoFinAtPanaderia2 = new TableColumn<>();
        tiempoEventoFinAtPanaderia2.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionPanaderia2()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionPanaderia2().getTiempoAtencion())));
        tiempoEventoFinAtPanaderia2.setText("Tiempo Atencion Panaderia 2");

        TableColumn<VectorEstadoDtoActual, String > momentoEventoFinAtPanaderia2 = new TableColumn<>();
        momentoEventoFinAtPanaderia2.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionPanaderia2()==null? new SimpleStringProperty(""): new SimpleStringProperty(Double.toString(cellData.getValue().getEventoFinAtencionPanaderia2().getMomentoEvento())));
        momentoEventoFinAtPanaderia2.setText("Momento Atencion Panaderia 2");

        // Evento Fin At. Caja
        TableColumn<VectorEstadoDtoActual, String> rndCantidadArticulos = new TableColumn<>();
        rndCantidadArticulos.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionCaja()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionCaja().getRandomCantidadArticulos().getRandom())));
        rndCantidadArticulos.setText("RND Cantidad Articulos");

        TableColumn<VectorEstadoDtoActual, String> cantidadArticulos = new TableColumn<>();
        cantidadArticulos.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionCaja()==null? new SimpleStringProperty(""): new SimpleStringProperty(Integer.toString(cellData.getValue().getEventoFinAtencionCaja().getCantidadArticulos())));
        cantidadArticulos.setText("Cantidad Articulos");

        TableColumn<VectorEstadoDtoActual, String > rndArt1 = new TableColumn<>();
        rndArt1.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionCaja()==null || cellData.getValue().getEventoFinAtencionCaja().getRandomsDemoraPorArticulo()[0] == null ? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionCaja().getRandomsDemoraPorArticulo()[0].getRandom())));
        rndArt1.setText("RND Art. 1");

        TableColumn<VectorEstadoDtoActual, String> demoraArt1 = new TableColumn<>();
        demoraArt1.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionCaja()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionCaja().getTiemposDemoraPorArticulo()[0])));
        demoraArt1.setText("Demora Art. 1");

        TableColumn<VectorEstadoDtoActual, String > rndArt2 = new TableColumn<>();
        rndArt2.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionCaja()==null || cellData.getValue().getEventoFinAtencionCaja().getRandomsDemoraPorArticulo()[1] == null ? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionCaja().getRandomsDemoraPorArticulo()[1].getRandom())));
        rndArt2.setText("RND Art. 2");

        TableColumn<VectorEstadoDtoActual, String> demoraArt2 = new TableColumn<>();
        demoraArt2.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionCaja()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionCaja().getTiemposDemoraPorArticulo()[1])));
        demoraArt2.setText("Demora Art. 2");

        TableColumn<VectorEstadoDtoActual, String > rndArt3 = new TableColumn<>();
        rndArt3.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionCaja()==null || cellData.getValue().getEventoFinAtencionCaja().getRandomsDemoraPorArticulo()[2] == null ? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionCaja().getRandomsDemoraPorArticulo()[2].getRandom())));
        rndArt3.setText("RND Art. 3");

        TableColumn<VectorEstadoDtoActual, String> demoraArt3 = new TableColumn<>();
        demoraArt3.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionCaja()==null? new SimpleStringProperty(""): new SimpleStringProperty(Float.toString(cellData.getValue().getEventoFinAtencionCaja().getTiemposDemoraPorArticulo()[2])));
        demoraArt3.setText("Demora Art. 3");

        TableColumn<VectorEstadoDtoActual, String> tiempoAtencionCaja = new TableColumn<>();
        tiempoAtencionCaja.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionCaja()==null? new SimpleStringProperty(""): new SimpleStringProperty(Double.toString(cellData.getValue().getEventoFinAtencionCaja().getTiempoAtencionCaja())));
        tiempoAtencionCaja.setText("Tiempo Atencion Caja");

        TableColumn<VectorEstadoDtoActual, String> momentoAtencionCaja = new TableColumn<>();
        momentoAtencionCaja.setCellValueFactory(cellData -> cellData.getValue().getEventoFinAtencionCaja()==null? new SimpleStringProperty(""): new SimpleStringProperty(Double.toString(cellData.getValue().getEventoFinAtencionCaja().getMomentoEvento())));
        momentoAtencionCaja.setText("Momento Atencion Caja");


        // Servidores
        // Empleado Despensa
        TableColumn<VectorEstadoDtoActual, String> colaDespensa = new TableColumn<>();
        colaDespensa.setCellValueFactory(new PropertyValueFactory<>("colaDespensa"));
        colaDespensa.setText("Cola Despensa");

        TableColumn<VectorEstadoDtoActual, String> estadoDespensa = new TableColumn<>();
        estadoDespensa.setCellValueFactory(cellData -> cellData.getValue().getEmpleadoDespensa()==null? new SimpleStringProperty(""): new SimpleStringProperty(cellData.getValue().getEmpleadoDespensa().getEstado().getEstado()));
        estadoDespensa.setText("Estado Despensa");

        TableColumn<VectorEstadoDtoActual,String> clienteEmpleadoDespensa = new TableColumn<>();
        clienteEmpleadoDespensa.setCellValueFactory(cellData -> cellData.getValue().getEmpleadoDespensa() == null || cellData.getValue().getEmpleadoDespensa().getClienteActual() == null ? new SimpleStringProperty("") : new SimpleStringProperty(Integer.toString(cellData.getValue().getEmpleadoDespensa().getClienteActual().getNumeroCliente())));
        clienteEmpleadoDespensa.setText("Cliente Despensa");

        // Panaderias
        TableColumn<VectorEstadoDtoActual, String> colaPanaderia = new TableColumn<>();
        colaPanaderia.setCellValueFactory(new PropertyValueFactory<>("colaPanaderia"));
        colaPanaderia.setText("Cola Panaderia");

        // Empleado Panaderia 1
        TableColumn<VectorEstadoDtoActual, String> idPanaderia1 = new TableColumn<>();
        idPanaderia1.setCellValueFactory(cellData -> cellData.getValue().getEmpleadoPanaderia1()==null? new SimpleStringProperty(""): new SimpleStringProperty(Integer.toString(cellData.getValue().getEmpleadoPanaderia1().getId())));
        idPanaderia1.setText("Id Panaderia");

        TableColumn<VectorEstadoDtoActual, String> estadoPanaderia1 = new TableColumn<>();
        estadoPanaderia1.setCellValueFactory(cellData -> cellData.getValue().getEmpleadoPanaderia1()==null? new SimpleStringProperty(""): new SimpleStringProperty(cellData.getValue().getEmpleadoPanaderia1().getEstado().getEstado()));
        estadoPanaderia1.setText("Estado Panaderia 1");

        TableColumn<VectorEstadoDtoActual,String> clienteEmpleadoPanaderia1 = new TableColumn<>();
        clienteEmpleadoPanaderia1.setCellValueFactory(cellData -> cellData.getValue().getEmpleadoPanaderia1() == null || cellData.getValue().getEmpleadoPanaderia1().getClienteActual() == null ? new SimpleStringProperty("") : new SimpleStringProperty(Integer.toString(cellData.getValue().getEmpleadoPanaderia1().getClienteActual().getNumeroCliente())));
        clienteEmpleadoPanaderia1.setText("Cliente Panaderia 1");

        // Empleado Panaderia 2
        TableColumn<VectorEstadoDtoActual, String> idPanaderia2 = new TableColumn<>();
        idPanaderia2.setCellValueFactory(cellData -> cellData.getValue().getEmpleadoPanaderia2()==null? new SimpleStringProperty(""): new SimpleStringProperty(Integer.toString(cellData.getValue().getEmpleadoPanaderia2().getId())));
        idPanaderia2.setText("Id Panaderia");

        TableColumn<VectorEstadoDtoActual, String> estadoPanaderia2 = new TableColumn<>();
        estadoPanaderia2.setCellValueFactory(cellData -> cellData.getValue().getEmpleadoPanaderia2()==null? new SimpleStringProperty(""): new SimpleStringProperty(cellData.getValue().getEmpleadoPanaderia2().getEstado().getEstado()));
        estadoPanaderia2.setText("Estado Panaderia 2");

        TableColumn<VectorEstadoDtoActual,String> clienteEmpleadoPanaderia2 = new TableColumn<>();
        clienteEmpleadoPanaderia2.setCellValueFactory(cellData -> cellData.getValue().getEmpleadoPanaderia2() == null || cellData.getValue().getEmpleadoPanaderia2().getClienteActual() == null ? new SimpleStringProperty("") : new SimpleStringProperty(Integer.toString(cellData.getValue().getEmpleadoPanaderia2().getClienteActual().getNumeroCliente())));
        clienteEmpleadoPanaderia2.setText("Cliente Panaderia 2");

        // Empleado Caja
        TableColumn<VectorEstadoDtoActual, String> colaCaja = new TableColumn<>();
        colaCaja.setCellValueFactory(new PropertyValueFactory<>("colaCaja"));
        colaCaja.setText("Cola Caja");

        TableColumn<VectorEstadoDtoActual, String> estadoCaja = new TableColumn<>();
        estadoCaja.setCellValueFactory(cellData -> cellData.getValue().getEmpleadoCaja()==null? new SimpleStringProperty(""): new SimpleStringProperty(cellData.getValue().getEmpleadoCaja().getEstado().getEstado()));
        estadoCaja.setText("Estado Caja");

        TableColumn<VectorEstadoDtoActual,String> clienteCaja = new TableColumn<>();
        clienteCaja.setCellValueFactory(cellData -> cellData.getValue().getEmpleadoCaja() == null || cellData.getValue().getEmpleadoCaja().getClienteActual() == null ? new SimpleStringProperty("") : new SimpleStringProperty(Integer.toString(cellData.getValue().getEmpleadoCaja().getClienteActual().getNumeroCliente())));
        clienteCaja.setText("Cliente Caja");

        // Contadores y acumuladores
        TableColumn<VectorEstadoDtoActual, String> contadorArticulos = new TableColumn<>();
        contadorArticulos.setCellValueFactory(new PropertyValueFactory<>("contadorArticulos"));
        contadorArticulos.setText("Contador Articulos");

        TableColumn<VectorEstadoDtoActual, String> acumuladorOciosoCaja = new TableColumn<>();
        acumuladorOciosoCaja.setCellValueFactory(new PropertyValueFactory<>("acumuladorTiempoOciosoCaja"));
        acumuladorOciosoCaja.setText("Acum. Tiempo Ocioso Caja");

        TableColumn<VectorEstadoDtoActual, String> maxColaDespensa = new TableColumn<>();
        maxColaDespensa.setCellValueFactory(new PropertyValueFactory<>("maximoLargoColaDespensa"));
        maxColaDespensa.setText("Max. Cola Despensa");

        TableColumn<VectorEstadoDtoActual, String> maxColaPanaderia = new TableColumn<>();
        maxColaPanaderia.setCellValueFactory(new PropertyValueFactory<>("maximoLargoColaPanaderia"));
        maxColaPanaderia.setText("Max. Cola Panaderia");

        tv_Simulacion.getColumns().addAll(nombreEvColumna, relojColumna,
                rndEventoLlegadaCliente, tiempoEventoLlegadaCliente, momentoEventoLLegadaCliente, rndDestinoCliente, destinoCliente,
                rndEventoFinAtDespensa, tiempoEventoFinAtDespensa, momentoEventoFinAtDespensa,
                rndEventoFinAtPanaderia1, tiempoEventoFinAtPanaderia1, momentoEventoFinAtPanaderia1,
                rndEventoFinAtPanaderia2, tiempoEventoFinAtPanaderia2, momentoEventoFinAtPanaderia2,
                rndCantidadArticulos, cantidadArticulos, rndArt1, demoraArt1, rndArt2, demoraArt2, rndArt3, demoraArt3, tiempoAtencionCaja, momentoAtencionCaja,
                colaDespensa, estadoDespensa, clienteEmpleadoDespensa,
                colaPanaderia, idPanaderia1, estadoPanaderia1, clienteEmpleadoPanaderia1, idPanaderia2, estadoPanaderia2, clienteEmpleadoPanaderia2,
                colaCaja, estadoCaja, clienteCaja,
                contadorArticulos, acumuladorOciosoCaja, maxColaDespensa, maxColaPanaderia);


        tv_Simulacion.refresh();

    }

    private void calcularEstadisticas(VectorEstadoNegocio vectorEstadoNegocio){

        int cantidadArticulos = vectorEstadoNegocio.getContadorArticulos();

        double porcentajeOciosoCaja = vectorEstadoNegocio.getAcumuladorTiempoOciosoCaja()*100/ vectorEstadoNegocio.getReloj();
        porcentajeOciosoCaja = CommonFunc.round( porcentajeOciosoCaja ,4);

        int colaMaxDespensa = vectorEstadoNegocio.getMaximoLargoColaDespensa();

        int colaMaxPanaderia = vectorEstadoNegocio.getMaximoLargoColaPanaderia();

        String cajaAyudaA;
        if (colaMaxDespensa >= colaMaxPanaderia){
            cajaAyudaA = "Despensa";
        }
        else{
            cajaAyudaA = "Panaderia";
        }

        tf_cantidadArticulos.setText(Integer.toString(cantidadArticulos));

        tf_porcentajeOciosoCaja.setText(Double.toString(porcentajeOciosoCaja));

        tf_colaMaxDespensa.setText(Integer.toString(colaMaxDespensa));

        tf_colaMaxPanaderia.setText(Integer.toString(colaMaxPanaderia));

        tf_cajaAyudaA.setText(cajaAyudaA);
    }









}
