
package imat;

import java.net.URL;
import java.util.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import se.chalmers.cse.dat216.project.*;


public class iMatController implements Initializable {

    iMatBackendController iMatBackendController = new iMatBackendController();
    IMatDataHandler db = IMatDataHandler.getInstance();
    Map<Integer, productDisplayItem> productDisplayMap = new HashMap<>();
    Map<Integer, shoppingCartItemCard> shoppingItemsMap = new HashMap<>();
    categoryHandler categories = categoryHandler.getInstance();
    SimpleDoubleProperty scale = new SimpleDoubleProperty(1);
    Timeline beat;

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
    @FXML
    TextField firstTimeTextField;
    @FXML
    TextField firstName;
    @FXML
    TextField lastName;
    @FXML
    TextField postCode;
    @FXML
    TextField phoneNumber;
    @FXML
    TextField email;
    @FXML
    TextField address;

    Customer customer;
    @FXML
    Label skapa;
    @FXML
    Label errorLabel;
    @FXML
    Pane menuPane;
    @FXML
    Pane createdSuccessfully;

    @FXML
    ImageView profilePicOne;
    @FXML
    ImageView profilePicTwo;
    @FXML
    Rectangle rect;
    @FXML
    Label arrow;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (Product product : db.getProducts()) {
            productDisplayItem dispItem = new productDisplayItem(product, this);
            productDisplayMap.put(product.getProductId(), dispItem);
            shoppingCartItemCard SCI = new shoppingCartItemCard(product, this);
            shoppingItemsMap.put(product.getProductId(), SCI);
        }
        if (db.isFirstRun()) {
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

    private void setSmall(){
        rect.setWidth(50);
        rect.setLayoutX(566);
        this.arrow.setText("<--");
        arrow.setPrefWidth(50);
        arrow.setLayoutX(566);
    }
    private void setLarge(){
        rect.setWidth(65);
        rect.setLayoutX(546);
        this.arrow.setText("<-----");
        arrow.setPrefWidth(80);
        arrow.setLayoutX(546);
    }

    void getBeat() {
        if (beat == null){
        this.beat = new Timeline(
                new KeyFrame(Duration.ZERO, event -> setSmall()),
                new KeyFrame(Duration.seconds(0.5), event -> setLarge()));
        }
    }

    public void pulseAnimation() {
        arrow.setPrefWidth(70);
        arrow.setLayoutX(566);
        beat.setAutoReverse(true);
        beat.setCycleCount(Timeline.INDEFINITE);
        beat.play();
    }

    @FXML
    public void onHoverRect(){
        getBeat();
        pulseAnimation();
    }

    @FXML
    public void rectHoverStopped(){
        setSmall();
        this.beat.stop();
    }

    @FXML
    public void closeProfilePicPane(){
        rect.setVisible(true);
        arrow.setVisible(true);
        profilePicPane.setVisible(false);
    }

    @FXML
    public void openProfilePicPane(){
        rect.setVisible(false);
        arrow.setVisible(false);
        this.beat.stop();
        setSmall();
        profilePicPane.setVisible(true);
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
        } else {
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

    private void addBreadCrumbsMain(Product p) {
        breadCrumbPane.getChildren().add(new BreadCrumb("Alla Produkter", false, this));
        breadCrumbPane.getChildren().add(new BreadCrumb(categories.mainToString(p), true, this));
    }

    private void addBreadCrumbsSub(Product product) {
        breadCrumbPane.getChildren().add(new BreadCrumb("Alla Produkter", false, this));
        if (product.getCategory() == ProductCategory.DAIRIES) {
            breadCrumbPane.getChildren().add(new BreadCrumb(categories.mainToString(product), true, this));
            return;
        }
        breadCrumbPane.getChildren().add(new BreadCrumb(categories.mainToString(product), false, this));
        breadCrumbPane.getChildren().add(new BreadCrumb(categories.subToString(product), true, this));
    }

    private void onHover(Label skipLabel) {
        skipLabel.setTextFill(Color.rgb(255, 255, 255, 0.47));
        skipLabel.setUnderline(true);
    }

    private void onHoverStop(Label skipLabel) {
        skipLabel.setTextFill(Color.rgb(255, 255, 255, 1));
        skipLabel.setUnderline(false);
    }

    @FXML
    private void onHoverSkipStop() {
        onHoverStop(skipLabel);
    }

    @FXML
    private void onHoverSkip(){
        onHover(skipLabel);
    }

    @FXML
    private void onHoverSkapa(){
        onHover(skapa);
    }

    @FXML
    private void onHoverSkapaStop(){
        onHoverStop(skapa);
    }



    @FXML
    private void skipClick() {
        firstTimeScreen.toBack();
    }

    @FXML
    private void goToCreateAccount() {
        if(firstTimeTextField.getText() != ""){
            db.getCustomer().setLastName(firstTimeTextField.getText());
            lastName.setText(firstTimeTextField.getText());
            firstTimeTextField.getParent().toBack();
        }
        else {
            errorLabel.setVisible(true);
            delay(2000, ()-> errorLabel.setVisible(false));
        }
    }

    @FXML
    private void createAccount(){
        setTextField(firstName);
        setTextField(lastName);
        setTextField(address);
        setTextField(postCode);
        setTextField(phoneNumber);
        setTextField(email);
    }

    private void setTextField(TextField textField) {
        if (textField.getText() != "") {
            setTextFieldSwitch(textField.getId(), textField.getText());
            textField.toBack();
        } else{
            db.getCustomer().setFirstName("");
            textField.toFront();
        }
    }

    private void setTextFieldSwitch(String id, String text){
        switch (id) {
            case "firstName":
                db.getCustomer().setFirstName(text);
                break;
            case "lastName":
                db.getCustomer().setLastName(text);
                break;
            case "address":
                db.getCustomer().setPostAddress(text);
                break;
            case "postCode":
                db.getCustomer().setPostCode(text);
                break;
            case "phoneNumber":
                db.getCustomer().setPhoneNumber(text);
                break;
            case "email":
                db.getCustomer().setEmail(text);
                break;
        }
    }

    @FXML
    private void createAccountPressed(){
        if (db.isCustomerComplete()){
            firstTimeScreen.toBack();
            menuPane.toFront();
            createdSuccessfully.setVisible(true);
            createdSuccessfully.toFront();
            delay(4000, () -> createdSuccessfully.setVisible(false));
            createdSuccessfully.toBack();
        }
        else{
            errorLabel.setVisible(true);
            delay(2000, ()-> errorLabel.setVisible(false));
        }
    }

    @FXML
    AnchorPane profilePicPane;

    @FXML
    public void pickOwl(){
        Image owl = new Image("/imat/resources/imgs/owl.png");
        profilePicOne.setImage(owl);
        profilePicTwo.setImage(owl);
    }

    @FXML
    public void pickMonkey(){
        Image monkey = new Image("/imat/resources/imgs/monkey.png");
        profilePicOne.setImage(monkey);
        profilePicTwo.setImage(monkey);
    }

    @FXML
    public void pickPenguin(){
        Image penguin = new Image("/imat/resources/imgs/penguin.png");
        profilePicOne.setImage(penguin);
        profilePicTwo.setImage(penguin);
    }








}


