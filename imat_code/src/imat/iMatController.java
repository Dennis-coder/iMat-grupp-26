
package imat;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import se.chalmers.cse.dat216.project.*;


public class iMatController implements Initializable {

    iMatBackendController iMatBackendController = new iMatBackendController();
    IMatDataHandler db = IMatDataHandler.getInstance();
    Map<Integer, productDisplayItem> productDisplayMap = new HashMap<Integer, productDisplayItem>();
    Map<Integer, shoppingCartItemCard> shoppingItemsMap = new HashMap<Integer, shoppingCartItemCard>();

    @FXML
    AnchorPane accountDetail;
    @FXML
    AnchorPane home;
    @FXML
    AnchorPane shoppingCart;
    @FXML
    Label startLabel;
    @FXML
    Label fruitLabel;
    @FXML
    Label meatLabel;
    @FXML
    Label dairyLabel;
    @FXML
    Label pantryLabel;
    @FXML
    Label drinksLabel;
    @FXML
    FlowPane productScreen;
    @FXML
    AnchorPane productViewScreen;
    @FXML
    FlowPane productViewHolder;
    @FXML
    AnchorPane shoppingCartHolder;
    @FXML
    Label shoppingCartCurrentItems;
    @FXML
    Circle shoppingCartCircle;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for(Product product : db.getProducts()){
            productDisplayItem dispItem = new productDisplayItem(product, this);
            productDisplayMap.put(product.getProductId(), dispItem);
            shoppingCartItemCard SCI = new shoppingCartItemCard(product, this);
            shoppingItemsMap.put(product.getProductId(), SCI);
        }
        populateProductScreen();
        db.getShoppingCart().addShoppingCartListener(scl);
    }

    @FXML
    public void toAccount(){
        accountDetail.toFront();
    }

    @FXML
    public void toHomepage(){
        home.toFront();
    }

    @FXML
    public void toShoppingCart(){
        shoppingCartHolder.getChildren().add(new shoppingCartScreen(this));
        shoppingCart.toFront();
    }

    public void mouseEnteredCategoryButton(Label label){
        DropShadow drop = new DropShadow(50, 0, 0, Color.GOLD);
        label.setEffect(drop);
        label.setUnderline(true);
    }

    public void mouseExitedCategoryButton(Label label){
        label.setUnderline(false);
        label.setEffect(null);
    }

    @FXML
    public void mouseEnteredStartButton(){
        mouseEnteredCategoryButton(startLabel);
    }

    @FXML
    public void mouseExitedStartButton(){
        mouseExitedCategoryButton(startLabel);
    }

    @FXML
    public void mouseEnteredFruitButton(){
        mouseEnteredCategoryButton(fruitLabel);
    }

    @FXML
    public void mouseExitedFruitButton(){
        mouseExitedCategoryButton(fruitLabel);
    }

    @FXML
    public void mouseEnteredMeatButton(){
        mouseEnteredCategoryButton(meatLabel);
    }

    @FXML
    public void mouseExitedMeatButton(){
        mouseExitedCategoryButton(meatLabel);
    }

    @FXML
    public void mouseEnteredDairyButton(){
        mouseEnteredCategoryButton(dairyLabel);
    }

    @FXML
    public void mouseExitedDairyButton(){
        mouseExitedCategoryButton(dairyLabel);
    }

    @FXML
    public void mouseEnteredPantryButton(){
        mouseEnteredCategoryButton(pantryLabel);
    }

    @FXML
    public void mouseExitedPantryButton(){
        mouseExitedCategoryButton(pantryLabel);
    }

    @FXML
    public void mouseEnteredDrinkButton(){
        mouseEnteredCategoryButton(drinksLabel);
    }

    @FXML
    public void mouseExitedDrinkButton(){
        mouseExitedCategoryButton(drinksLabel);
    }

    @FXML
    public void populateProductScreen(){
        for(Product p : db.getProducts()){
            productScreen.getChildren().add(productDisplayMap.get(p.getProductId()));
        }
    }



    public void openProductViewScreen(Product product, int currentVal){
        productView productView = new productView(product, currentVal, this);
        productViewHolder.getChildren().add(productView);
        productViewScreen.toFront();
    }

    public void closeProductViewScreen(){
        productViewHolder.getChildren().clear();
        home.toFront();
    }

    @FXML
    public void mouseTrap(Event event){
        event.consume();
    }

    ShoppingCartListener scl = new ShoppingCartListener() {
        @Override
        public void shoppingCartChanged(CartEvent cartEvent) {
            int itemsInCart = 0;
            for (ShoppingItem SI: db.getShoppingCart().getItems()){
                itemsInCart += SI.getAmount();
            }
            if (itemsInCart != 0){
                shoppingCartCurrentItems.setVisible(true);
                shoppingCartCircle.setVisible(true);
                shoppingCartCurrentItems.setText(String.valueOf(itemsInCart));
            }
            else{
                shoppingCartCurrentItems.setVisible(false);
                shoppingCartCircle.setVisible(false);
            }
        }
    };

    @FXML
    public void addItemToCart(Product product, int currentVal) {
        for (ShoppingItem SI : db.getShoppingCart().getItems()) {
            if (SI.getProduct().equals(product)) {
                SI.setAmount(SI.getAmount() + currentVal);
                db.getShoppingCart().fireShoppingCartChanged(SI, true);
                return;
            }
        }
        db.getShoppingCart().addItem(new ShoppingItem(product, currentVal));
    }

    public void removeItemFromCart(Product p){
        for (ShoppingItem SI : db.getShoppingCart().getItems()) {
            if (SI.getProduct().equals(p)) {
                db.getShoppingCart().removeItem(SI);
            }
        }
    }

    @FXML
    public void closeShoppingCartScreen(){
        shoppingCartHolder.getChildren().clear();
        home.toFront();
    }

    public static void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try { Thread.sleep(millis); }
                catch (InterruptedException e) { }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> continuation.run());
        new Thread(sleeper).start();
    }

    public void pulseAnimation(ImageView img, Timeline beat, DoubleProperty scale) {
        img.scaleXProperty().bind(scale);
        img.scaleYProperty().bind(scale);
        beat.setAutoReverse(true);
        beat.setCycleCount(Timeline.INDEFINITE);
        beat.play();
    }
}


