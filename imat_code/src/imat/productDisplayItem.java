package imat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import javafx.scene.control.Label;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.io.IOException;


public class productDisplayItem extends AnchorPane {

    IMatDataHandler db = IMatDataHandler.getInstance();
    private iMatController parentController;

    Product product;
    @FXML
    ImageView picture;
    @FXML
    Label title;
    @FXML
    Label prize;
    @FXML
    Label prizePerUnit;
    @FXML
    ComboBox amountSelect;

    public productDisplayItem(Product product, iMatController controller){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("productDisplayItem.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);


        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.parentController = controller;
        this.product = product;

        init();
    }

    private void init(){
        title.setText(product.getName());
        prize.setText(String.valueOf(product.getPrice()) + ":-");
        prizePerUnit.setText(String.valueOf(product.getPrice()) + " " + product.getUnit());
        initializeComboBox();
        setPicture();
    }

    private void initializeComboBox() {
        /*if ((product.getUnit() == "kr/kg") || product.getUnit() == "kr/l"){  kan lägga till om vi vill ha olika
        alternativ för lösvikt
            amountSelect.getItems().addAll("100g", "200g", "3",)
        }*/
        amountSelect.getItems().addAll("1", "2", "3", "4", "5", "6");
        amountSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int currentVal = Integer.valueOf(newValue);
                prize.setText(String.valueOf(product.getPrice() * currentVal) + ":-");
            }
        });
    }

    private void setPicture(){
        picture.setImage(db.getFXImage(product, 220, 180));
    }




}
