
package imat;

import java.net.URL;
import java.util.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
    Map<Integer, productDisplayItem> productDisplayMap = new HashMap<>();
    Map<Integer, shoppingCartItemCard> shoppingItemsMap = new HashMap<>();
    categoryHandler categories = categoryHandler.getInstance();

    @FXML
    AnchorPane accountDetail;
    @FXML
    AnchorPane home;
    @FXML
    AnchorPane startScreen;
    @FXML
    AnchorPane searchScreen;
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
    @FXML
    FlowPane hitsFlowPane;
    @FXML
    FlowPane breadCrumbPane;
    @FXML
    Label searchScreenLabel;
    @FXML
    AnchorPane firstTimeScreen;
    @FXML
    Label skipLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (Product product : db.getProducts()) {
            productDisplayItem dispItem = new productDisplayItem(product, this);
            productDisplayMap.put(product.getProductId(), dispItem);
            shoppingCartItemCard SCI = new shoppingCartItemCard(product, this);
            shoppingItemsMap.put(product.getProductId(), SCI);
        }
        if(db.isFirstRun()){
            firstTimeScreen.toFront();
        }
        populateProductScreen();
        db.getShoppingCart().addShoppingCartListener(scl);
        if (db.getShoppingCart().getItems().size() != 0) {
            db.getShoppingCart().fireShoppingCartChanged(db.getShoppingCart().getItems().get(0), false);
        }
    }

    @FXML
    public void toAccount() {
        accountDetail.toFront();
    }

    @FXML
    public void toHomepage() {
        home.toFront();
        startScreen.toFront();
    }

    @FXML
    public void toShoppingCart() {
        if (shoppingCartHolder.getChildren().isEmpty()) {
            shoppingCartHolder.getChildren().add(shoppingCartScreen.getInstance(this));
        }
        shoppingCart.toFront();
    }

    public void mouseEnteredCategoryButton(Label label) {
        DropShadow drop = new DropShadow(50, 0, 0, Color.GOLD);
        label.setEffect(drop);
        label.setUnderline(true);
    }

    public void mouseExitedCategoryButton(Label label) {
        label.setUnderline(false);
        label.setEffect(null);
    }

    @FXML
    public void mouseEnteredStartButton() {
        mouseEnteredCategoryButton(startLabel);
    }

    @FXML
    public void mouseExitedStartButton() {
        mouseExitedCategoryButton(startLabel);
    }

    @FXML
    public void mouseEnteredFruitButton() {
        mouseEnteredCategoryButton(fruitLabel);
    }

    @FXML
    public void mouseExitedFruitButton() {
        mouseExitedCategoryButton(fruitLabel);
    }

    @FXML
    public void mouseEnteredMeatButton() {
        mouseEnteredCategoryButton(meatLabel);
    }

    @FXML
    public void mouseExitedMeatButton() {
        mouseExitedCategoryButton(meatLabel);
    }

    @FXML
    public void mouseEnteredDairyButton() {
        mouseEnteredCategoryButton(dairyLabel);
    }

    @FXML
    public void mouseExitedDairyButton() {
        mouseExitedCategoryButton(dairyLabel);
    }

    @FXML
    public void mouseEnteredPantryButton() {
        mouseEnteredCategoryButton(pantryLabel);
    }

    @FXML
    public void mouseExitedPantryButton() {
        mouseExitedCategoryButton(pantryLabel);
    }

    @FXML
    public void mouseEnteredDrinkButton() {
        mouseEnteredCategoryButton(drinksLabel);
    }

    @FXML
    public void mouseExitedDrinkButton() {
        mouseExitedCategoryButton(drinksLabel);
    }

    @FXML
    public void populateProductScreen() {
        for (Product p : db.getProducts()) {
            productScreen.getChildren().add(productDisplayMap.get(p.getProductId()));
        }
    }


    public void openProductViewScreen(Product product, int currentVal) {
        productView productView = new productView(product, currentVal, this);
        productViewHolder.getChildren().clear();
        productViewHolder.getChildren().add(productView);
        productViewScreen.toFront();
    }

    public void closeProductViewScreen() {
        if (shoppingCartHolder.getChildren().isEmpty()) {
            productViewHolder.getChildren().clear();
            productViewScreen.toBack();
            System.out.println(productViewHolder.getChildren().isEmpty());
        } else {
            shoppingCartScreen.getInstance(this).singltemAnchorPane.toBack();
        }

    }

    @FXML
    public void mouseTrap(Event event) {
        event.consume();
    }

    ShoppingCartListener scl = new ShoppingCartListener() {
        @Override
        public void shoppingCartChanged(CartEvent cartEvent) {
            int itemsInCart = 0;
            for (ShoppingItem SI : db.getShoppingCart().getItems()) {
                itemsInCart += SI.getAmount();
            }
            if (itemsInCart != 0) {
                shoppingCartCurrentItems.setVisible(true);
                shoppingCartCircle.setVisible(true);
                shoppingCartCurrentItems.setText(String.valueOf(itemsInCart));
            } else {
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

    public void removeItemFromCart(Product p) {
        for (ShoppingItem SI : db.getShoppingCart().getItems()) {
            if (SI.getProduct().equals(p)) {
                db.getShoppingCart().removeItem(SI);
            }
        }
    }

    @FXML
    public void closeShoppingCartScreen() {
        shoppingCartHolder.getChildren().clear();
        home.toFront();
    }

    public static void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                }
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

    public void displayCategory(List<Product> products) {
        hitsFlowPane.getChildren().clear();
        breadCrumbPane.getChildren().clear();
        shoppingCartHolder.getChildren().clear();
        updateSearchScreenLabel(products);
        for (Product product : products) {
            hitsFlowPane.getChildren().add(productDisplayMap.get(product.getProductId()));
        }
        delay(4, () -> breadCrumbPane.setLayoutX(searchScreenLabel.getLayoutBounds().getWidth() + 10));

        home.toFront();
        searchScreen.toFront();
    }



    @FXML
    public void displayCatVeg() {
        displayCategory(categories.getVegCategoryProducts());
    }

    @FXML
    public void displayCatMeat() {
        displayCategory(categories.getMeatCategoryProducts());
    }

    @FXML
    public void displayCatDairy() {
        displayCategory(categories.getDairyCategoryProducts());
    }

    @FXML
    public void displayCatDrinks() {
        displayCategory(categories.getDrinksCategoryProducts());
    }

    @FXML
    public void displayCatPantry() {
        displayCategory(categories.getPantryCategoryProducts());
    }

    public void updateSearchScreenLabel(List<Product> products) {
        if (products.equals(db.getProducts())) {
            searchScreenLabel.setText("Alla Produkter  ");
            breadCrumbPane.getChildren().add(new BreadCrumb("Alla Produkter", true, this));
        }
        else{
            for (Product p : products) {
                if (p.getCategory() != products.get(0).getCategory()) {
                    searchScreenLabel.setText(categories.mainToString(p) + "  ");
                    addBreadCrumbsMain(p);
                    return;
                }
            }
            searchScreenLabel.setText(categories.subToString(products.get(0)) + "  ");
            addBreadCrumbsSub(products.get(0));
        }
    }

    private void addBreadCrumbsMain(Product p){
        breadCrumbPane.getChildren().add(new BreadCrumb("Alla Produkter", false, this));
        breadCrumbPane.getChildren().add(new BreadCrumb(categories.mainToString(p), true, this));
    }

    private void addBreadCrumbsSub(Product product){
        breadCrumbPane.getChildren().add(new BreadCrumb("Alla Produkter", false, this));
        if (product.getCategory() == ProductCategory.DAIRIES){
            breadCrumbPane.getChildren().add(new BreadCrumb(categories.mainToString(product),true, this));
            return;
        }
        breadCrumbPane.getChildren().add(new BreadCrumb(categories.mainToString(product), false, this));
        breadCrumbPane.getChildren().add(new BreadCrumb(categories.subToString(product), true, this));
    }

    @FXML
    private void onHoverSkip(){
        skipLabel.setTextFill(Color.rgb(255, 255, 255, 0.47));
        skipLabel.setUnderline(true);
    }

    @FXML
    private void onHoverSkipStop(){
        skipLabel.setTextFill(Color.rgb(255, 255, 255, 1));
        skipLabel.setUnderline(false);
    }

    @FXML
    private void skipClick() {
        firstTimeScreen.toBack();
    }


}


