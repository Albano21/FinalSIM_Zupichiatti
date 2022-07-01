package finalsim.finalsim_zupichiatti.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
@Lazy
public class StageManager {

    private final Stage stage;

    private final ApplicationContext applicationContext;


    public StageManager(Stage stage, ApplicationContext applicationContext) {
        this.stage = stage;
        this.applicationContext = applicationContext;
    }

    public Stage getStage() {
        return stage;
    }

    public void loadStageParentScene(URL fxmlUrl, int width, int height) throws IOException {
        //Obtiene el recurso correspondiente al archivo fxml
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        //Este paso no hace falta normalmente con JavaFx, pero se hace para indicarle
        //al loader de donde sacar el controlador del fxml, así usa el ApplicationContext de spring
        //y no la inyección de dependencias por defecto de JavaFx
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        Parent parent = fxmlLoader.load();

        stage.setScene(new Scene(parent, width ,height));
        stage.centerOnScreen();
        //stage.setMaximized(true);

    }

    public void showStage(){
        stage.show();
    }

}
