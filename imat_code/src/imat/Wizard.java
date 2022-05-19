package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class Wizard extends StackPane {
    @FXML
    TextField firstName;
    @FXML
    TextField lastName;
    @FXML
    TextField postCode;
    @FXML
    TextField address;
    @FXML
    TextField phone;
    @FXML
    TextField email;

    private static Wizard instance;

    private Wizard(){

    }

    public static Wizard getInstance(){
        if (instance == null){
            instance = new Wizard();
            instance.init();
        }
        return instance;
    }

    private void init(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmlFiles/purchaseWizard.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}
