package imat;


import java.awt.*;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.chalmers.cse.dat216.project.IMatDataHandler;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class iMat extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        ResourceBundle bundle = java.util.ResourceBundle.getBundle("imat/resources/iMat");
        
        Parent root = FXMLLoader.load(getClass().getResource("fxmlFiles/iMat.fxml"), bundle);

        Dimension launchSize = getLaunchSize();

        Scene scene = new Scene(root, launchSize.width, launchSize.height);
        stage.setTitle(bundle.getString("application.name"));
        stage.setScene(scene);
        stage.show();

    }

    @Override
    public void stop(){
        IMatDataHandler db = IMatDataHandler.getInstance();
        db.resetFirstRun();
        db.shutDown();
    }

    private Dimension getLaunchSize(){
        Dimension launchSize = new Dimension(1440, 786);

        Dimension screenSize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());

        if (screenSize.width < 1440){
            launchSize.width = screenSize.width;
        }

        if (screenSize.height < 786){
            launchSize.height = screenSize.height - 50;
        }

        return launchSize;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
