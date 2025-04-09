package cr.ac.una.cineuna;

import cr.ac.una.cineuna.util.FlowController;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        
        FlowController.getInstance().InitializeFlow(stage, null);
         stage.getIcons().add(new Image(App.class.getResourceAsStream("/cr/ac/una/cineuna/resources/Logo.png")));
        stage.setTitle("CINEUNA");
        FlowController.getInstance().goViewInWindow("LogInView");
        
      
    }

    public static void main(String[] args) {
        launch();
    }

}