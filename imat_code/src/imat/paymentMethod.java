package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.control.Label;

import java.io.IOException;

public class paymentMethod extends AnchorPane {
    @FXML
    ImageView picture;
    @FXML
    Label name;
    @FXML
    Label bank;
    @FXML
    Label type;
    @FXML
    Label number;

    paymentMethodCreate parentController;

    public paymentMethod(String type, String name, String bank, String number, paymentMethodCreate parentController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmlFiles/paymentMethod.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.parentController = parentController;
        this.type.setText(type);
        this.name.setText(name);
        this.bank.setText(bank);
        this.number.setText(number);
        setImg(type);
    }

    private void setImg(String type){
        switch (type){
            case "MasterCard":
                picture.setImage(new Image("/imat/resources/imgs/mastercard.png"));
                break;
            case "Visa":
                picture.setImage(new Image("/imat/resources/imgs/visa.png"));
                break;
            case "Swish":
                picture.setImage(new Image("/imat/resources/imgs/swish.png"));
        }
    }

    @FXML
    public void remove(){
        parentController.remove(this);
    }



}
