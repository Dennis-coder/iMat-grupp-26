package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Objects;

public class paymentMethod extends AnchorPane {
    @FXML
    ImageView picture;
    @FXML
    Label name;
    @FXML
    Label cvv;
    @FXML
    Label type;
    @FXML
    Label number;
    String date;
    String day;
    String month;
    String year;
    String cvvNum;

    paymentMethodCreate parentController;

    public paymentMethod(String type, String name, String cvv, String number, String day,
                         String month, String year, paymentMethodCreate parentController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmlFiles/paymentMethod.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.parentController = parentController;
        setTypeText(type);
        this.type.setText(type);
        this.name.setText(name);
        this.cvv.setText("CVV: ***");
        this.cvvNum = cvv;
        this.number.setText(number);
        this.day = day;
        this.month = month;
        this.year = year;
        this.date = day + " / " + month + " - "+ year;
        setImg(type);
    }

    private void setTypeText(String type) {
        if (Objects.equals(type, "Annan")){
            this.type.setText("Kreditkort");
        }
        else {
            this.type.setText(type);
        }
    }

    @FXML
    public void cvvHover(){
        cvv.setText("CVV: " + cvvNum);
    }

    @FXML
    public void cvvHoverStopped(){
        cvv.setText("CVV: ***");
    }

    private void setImg(String type){
        switch (type){
            case "MasterCard":
                picture.setImage(new Image("/imat/resources/imgs/mastercard.png"));
                break;
            case "Visa":
                picture.setImage(new Image("/imat/resources/imgs/visa.png"));
                break;
            case "Annan":
                picture.setImage(new Image("/imat/resources/imgs/card.png"));
        }
    }

    @FXML
    public void remove(){
        parentController.remove(this);
    }



}
