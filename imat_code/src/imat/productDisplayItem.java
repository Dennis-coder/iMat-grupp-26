package imat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import se.chalmers.cse.dat216.project.*;
import javafx.scene.control.Label;

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
    int currentVal = 1;
    @FXML
    ImageView ekoImg;
    @FXML
    ImageView heartImg;
    @FXML
    TextField itemAdded;
    @FXML
    Label amountText;
    @FXML
    ImageView basketImg;

    ShoppingCartListener scl = new ShoppingCartListener() {
        @Override
        public void shoppingCartChanged(CartEvent cartEvent) {
            for (ShoppingItem SI : db.getShoppingCart().getItems()) {
                if (SI.getProduct().equals(product)) {
                    amountText.setVisible(true);
                    amountText.setText(String.valueOf((int) SI.getAmount()) + "x");
                    basketImg.setVisible(true);
                    return;
                }
            }
            amountText.setVisible(false);
        }
    };



    public productDisplayItem(Product product, iMatController controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmlFiles/productDisplayItem.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        db.getShoppingCart().addShoppingCartListener(scl);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.parentController = controller;
        this.product = product;

        init();
    }

    private void init() {
        title.setText(product.getName());
        prize.setText(String.format("%.2f", product.getPrice()) + ":-");
        prizePerUnit.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit());
        initializeComboBox();
        setPicture();
        db.getShoppingCart().addShoppingCartListener(scl);
    }

    private void initializeComboBox() {
        amountSelect.getItems().addAll("1", "2", "3", "4", "5", "6");
        amountSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != "") {
                    currentVal = Integer.valueOf(newValue);
                    prize.setText(String.format("%.2f", (product.getPrice() * currentVal)) + ":-");
                }
            }
        });
    }

    private void setPicture() {
        picture.setImage(db.getFXImage(product, 220, 180));
        if (product.isEcological()) {
            ekoImg.setVisible(true);
        }
        if (db.isFavorite(product)) {
            heartImg.setVisible(true);
        }
    }

    @FXML
    public void onClick(Event event) {
        parentController.openProductViewScreen(product, currentVal);
    }

    @FXML
    public void onHover() {
        title.setUnderline(true);
        picture.setEffect(new DropShadow(50, 0, 0, Color.GOLD));
    }

    @FXML
    public void hoverStopped() {
        title.setUnderline(false);
        picture.setEffect(null);
    }

    @FXML
    public void addButtonPressed() {
        itemAdded.setVisible(true);
        itemAdded.toFront();
        parentController.addItemToCart(product, currentVal);
        iMatController.delay(1000, () -> itemAdded.setVisible(false));
    }

    @FXML
    public void becameFavorite() {
        heartImg.setVisible(true);
    }

    @FXML
    public void removedFavorite() {
        heartImg.setVisible(false);
    }

    @FXML
    public void onHoverHeart(){
        heartImg.setVisible(true);
    }

    @FXML
    public void onHoverHeartStop(){
        if(db.isFavorite(product)){
            heartImg.setVisible(false);
        }
    }


    @FXML
    public void onClickHeart() {
        if (db.isFavorite(product)) {
            heartImg.setImage(new Image("/imat/resources/imgs/heart.png"));
            heartImg.setVisible(false);
            parentController.removeFavorite(product);
        } else {
            heartImg.setImage(new Image("/imat/resources/imgs/heartFilled.png"));
            heartImg.setVisible(true);
            parentController.addFavorite(product);
        }
    }

}
