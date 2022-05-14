package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.*;

import java.io.IOException;

public class shoppingCartScreen extends AnchorPane {
    iMatController parentController;
    IMatDataHandler db = IMatDataHandler.getInstance();
    @FXML
    AnchorPane clearCartConfirmationScreen;
    @FXML
    TextArea itemListTextArea;
    @FXML
    TextArea itemPricesTextArea;
    @FXML
    Label totalPriceLabel;
    @FXML
    FlowPane itemsFlowPane;

    public shoppingCartScreen(iMatController controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmlFiles/shoppingCartScreen.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.parentController = controller;

        init();
    }

    private void init() {
        updateTextField();
        db.getShoppingCart().addShoppingCartListener(scl);
        itemPricesTextArea.scrollTopProperty().bindBidirectional(itemListTextArea.scrollTopProperty());
        populateFlowPane();
    }

    private void populateFlowPane(){
        itemsFlowPane.getChildren().clear();
        for(ShoppingItem SI: db.getShoppingCart().getItems()){
            shoppingCartItemCard SCI = parentController.shoppingItemsMap.get(SI.getProduct().getProductId());
            SCI.amount.setText(String.valueOf((int)SI.getAmount()));
            itemsFlowPane.getChildren().add(SCI);
        }
    }

    private void updateTextField() {
        itemListTextArea.clear();
        itemPricesTextArea.clear();
        for (ShoppingItem SI: db.getShoppingCart().getItems()){
            itemListTextArea.setText(itemListTextArea.getText() + "\n\n" +
                    SI.getProduct().getName() + " x" + (int)SI.getAmount());
            itemPricesTextArea.setText(itemPricesTextArea.getText() + "\n\n" + String.format("%.2f", SI.getTotal()) + "kr");
        }
        totalPriceLabel.setText(String.format("%.2f", db.getShoppingCart().getTotal()) + ":-");
    }

    ShoppingCartListener scl = new ShoppingCartListener() {
        @Override
        public void shoppingCartChanged(CartEvent cartEvent) {
            updateTextField();
            populateFlowPane();
        }
    };

    @FXML
    public void clearCartPressed(){
        clearCartConfirmationScreen.toFront();
    }

    @FXML
    public void backToCartScreen(){
        clearCartConfirmationScreen.toBack();
    }

    @FXML
    public void confirmedClearCart(){
        db.getShoppingCart().clear();
        backToCartScreen();
    }

    @FXML
    public void closeScreen(){
        parentController.closeShoppingCartScreen();
    }
}
