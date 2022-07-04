package finalsim.finalsim_zupichiatti.fxcontroller;

import finalsim.finalsim_zupichiatti.controller.ControladorFinalSim;
import finalsim.finalsim_zupichiatti.controller.utils.ConstantesGenerador;
import finalsim.finalsim_zupichiatti.modelo.ParametrosGenerador;
import finalsim.finalsim_zupichiatti.modelo.colas.ParametrosNegocio;
import finalsim.finalsim_zupichiatti.modelo.colas.VectorEstadoNegocio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

@Component
@Lazy
public class MainFxController implements Initializable {

    @Autowired
    private ControladorFinalSim controladorFinalSim;

    @Autowired
    private ApplicationContext applicationContext;

    @FXML
    private TextField tf_mediaLlegada;

    @FXML
    private TextField tf_minDemoraDespensa;

    @FXML
    private TextField tf_maxDemoraDespensa;

    @FXML
    private TextField tf_minDemoraPanaderia;

    @FXML
    private TextField tf_maxDemoraPanaderia;

    @FXML
    private TextField tf_MediaDemoraCaja;

    @FXML
    private TextField tf_desviacionEstandarCaja;

    @FXML
    private TextField tf_cantMinutos;

    @FXML
    private TextField tf_cantidadFilas;

    @FXML
    private TextField tf_primerFila;

    @Value("classpath:/fxml/resultadoSimulacion.fxml")
    private Resource modalResultado;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UnaryOperator<TextFormatter.Change> floatFilter = t -> {
            if (t.isReplaced())
                if(t.getText().matches("[^0-9]"))
                    t.setText(t.getControlText().substring(t.getRangeStart(), t.getRangeEnd()));

            if (t.isAdded()) {
                if (t.getControlText().contains(".")) {
                    if (t.getText().matches("[^0-9]")) {
                        t.setText("");
                    }
                } else if (t.getText().matches("[^0-9.]")) {
                    t.setText("");
                }
            }
            return t;
        };

        setTfTextFormatter(tf_mediaLlegada, floatFilter);
        setTfTextFormatter(tf_minDemoraDespensa, floatFilter);
        setTfTextFormatter(tf_maxDemoraDespensa, floatFilter);
        setTfTextFormatter(tf_minDemoraPanaderia, floatFilter);
        setTfTextFormatter(tf_maxDemoraPanaderia, floatFilter);
        setTfTextFormatter(tf_MediaDemoraCaja, floatFilter);
        setTfTextFormatter(tf_desviacionEstandarCaja, floatFilter);
        setTfTextFormatter(tf_cantMinutos, floatFilter);
        setTfTextFormatter(tf_cantidadFilas, floatFilter);
        setTfTextFormatter(tf_primerFila, floatFilter);




    }

    private void setTfTextFormatter(TextField textField, UnaryOperator<TextFormatter.Change> floatFilter){
        textField.setTextFormatter(new TextFormatter<Float>(floatFilter));

    }

    @FXML
    void generarSimulacion(ActionEvent event){
        ParametrosNegocio parametrosNegocio = new ParametrosNegocio();
        obtenerParametrosNegocio(parametrosNegocio);
        ParametrosGenerador parametrosGenerador = new ParametrosGenerador();
        parametrosGenerador.setMetodoGeneradorRandom(ConstantesGenerador.LENGUAJE);
        parametrosGenerador.setPresicion(4);
        parametrosGenerador.setN(1);

        List<VectorEstadoNegocio> simulacion = controladorFinalSim
                .generarSimulacion(parametrosNegocio, parametrosGenerador);

        Stage modalStage = new Stage();
        ResultadoFxController modalResultadoSimulacion = setModalScene(modalStage);
        modalResultadoSimulacion.mostrarResultadoSimulacion(simulacion);
        modalStage.showAndWait();

    }

    // falta el setModalScene()
    @SneakyThrows
    private ResultadoFxController setModalScene(Stage modalStage){

        FXMLLoader fxmlLoader = new FXMLLoader(modalResultado.getURL());
        fxmlLoader.setControllerFactory(applicationContext::getBean);

        Parent parent = fxmlLoader.load();
        ResultadoFxController resultadoSimulacion = (ResultadoFxController) fxmlLoader.getController();
        modalStage.setScene(new Scene(parent, 600, 400));
        modalStage.centerOnScreen();
        modalStage.setMaximized(true);

        return resultadoSimulacion;
    }

    private void obtenerParametrosNegocio(ParametrosNegocio parametrosNegocio){

        try{
            float lamdaExpLlegadas = Float.parseFloat(tf_mediaLlegada.getText().trim());
            float minDespensaA = Float.parseFloat(tf_minDemoraDespensa.getText().trim());
            float maxDespensaB = Float.parseFloat(tf_maxDemoraDespensa.getText().trim());
            float minPanaderiaA = Float.parseFloat(tf_minDemoraPanaderia.getText().trim());
            float maxPanaderiaB = Float.parseFloat(tf_maxDemoraPanaderia.getText().trim());
            float mediaCaja = Float.parseFloat(tf_MediaDemoraCaja.getText().trim());
            float desvEstCaja = Float.parseFloat(tf_desviacionEstandarCaja.getText().trim());

            float cantMinutos = Float.parseFloat(tf_cantMinutos.getText().trim());
            int cantFilas = Integer.parseInt(tf_cantidadFilas.getText().trim());
            int primerFila = Integer.parseInt(tf_primerFila.getText().trim());

            parametrosNegocio.setLambdaExpLlegadaClientes(lamdaExpLlegadas);
            parametrosNegocio.setMinimoDemoraDespensa(minDespensaA);
            parametrosNegocio.setMaximoDemoraDespensa(maxDespensaB);
            parametrosNegocio.setMinimoDemoraPanaderia(minPanaderiaA);
            parametrosNegocio.setMaximoDemoraPanaderia(maxPanaderiaB);
            parametrosNegocio.setMediaDemoraCaja(mediaCaja);
            parametrosNegocio.setDesviacionEstCaja(desvEstCaja);

            parametrosNegocio.setMaxMinutosSimular(cantMinutos);
            parametrosNegocio.setCantFilasMostrar(cantFilas);
            parametrosNegocio.setMostrarFilaDesde(primerFila);

            parametrosNegocio.validar();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Datos Incorrectos!");
            alert.setContentText("Algunos de los datos ingresados no poseen el formato correcto.");
            alert.showAndWait();
        } catch (IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Datos Incorrectos!");
            alert.setHeaderText("Algunos de los valores ingresados son incorrectos.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }


    }


}
