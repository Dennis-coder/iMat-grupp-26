
package imat;

import java.net.URL;
import java.util.*;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import se.chalmers.cse.dat216.project.*;


public class iMatController implements Initializable {

    IMatDataHandler db = IMatDataHandler.getInstance();
    Map<Integer, productDisplayItem> productDisplayMap = new HashMap<>();
    Map<Integer, productDisplayItem> favoriteDisplayMap = new HashMap<>();
    Map<Integer, shoppingCartItemCard> shoppingItemsMap = new HashMap<>();

    categoryHandler categories = categoryHandler.getInstance();
    SimpleDoubleProperty scale = new SimpleDoubleProperty(1);
    Timeline beat = this.getBeat();

    @FXML
    Pane fruitButton;
    @FXML
    Pane startButton;
    @FXML
    Pane dairyButton;
    @FXML
    Pane meatButton;
    @FXML
    Pane pantryButton;
    @FXML
    Pane drinksButton;
    Pane[] navButtons = {fruitButton, startButton, meatButton, pantryButton, dairyButton, drinksButton};
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
    Label[] navLabels = {fruitLabel, meatLabel, dairyLabel, pantryLabel, drinksLabel, startLabel};

    @FXML
    FlowPane favoriteScreen;
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
    @FXML
    Label skapa;
    @FXML
    Label errorLabel;
    @FXML
    Pane menuPane;
    @FXML
    Pane createdSuccessfully;
    @FXML
    Label editLabel;
    @FXML
    Label nameLabel;

    @FXML
    ImageView profilePicOne;
    @FXML
    ImageView profilePicTwo;
    @FXML
    ImageView profilePicThree;
    @FXML
    Rectangle rect;
    @FXML
    Label arrow;
    @FXML
    TextField searchBar;
    @FXML
    Label suggestionLabel;
    @FXML
    FlowPane suggestionPane;
    @FXML
    AnchorPane firstTimePane;
    @FXML
    AnchorPane notLoggedIn;
    @FXML
    AnchorPane profilePicPane1;
    @FXML
    FlowPane editScreen;
    @FXML
    FlowPane paymentPane;
    @FXML
    FlowPane wizardHolder;

    paymentMethodCreate PMC = new paymentMethodCreate(this);

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        db.reset();
        for (Product product : db.getProducts()) {
            productDisplayItem dispItem = new productDisplayItem(product, this);
            productDisplayMap.put(product.getProductId(), dispItem);
            shoppingCartItemCard SCI = new shoppingCartItemCard(product, this);
            shoppingItemsMap.put(product.getProductId(), SCI);
        }

        //populateProductScreen();
        db.getShoppingCart().addShoppingCartListener(scl);
        if (db.getShoppingCart().getItems().size() != 0) {
            db.getShoppingCart().fireShoppingCartChanged(db.getShoppingCart().getItems().get(0), false);
        }
    }

    @FXML
    public void toAccount() {
        if (paymentPane.getChildren().isEmpty()){
            paymentPane.getChildren().add(PMC);
        }
        PMC.displayScreen.toFront();
        updateAccountText();
        accountDetail.toFront();
        if (!db.isCustomerComplete()) {
            notLoggedIn.toFront();
        } else {
            notLoggedIn.toBack();
        }
    }

    @FXML
    public void toHomepage() {
        home.toFront();
        clearFills();
        fillBackground(startButton, startLabel);
        startScreen.toFront();

    }

    @FXML
    public void toCreate() {
        firstTimeScreen.toFront();
        firstTimePane.toBack();
    }

    @FXML
    public void keepBrowsing() {
        accountDetail.toBack();
    }

    public void addFavorite(Product product) {
        productDisplayItem disp = new productDisplayItem(product, this);
        disp.becameFavorite();
        favoriteDisplayMap.put(product.getProductId(), disp);
        favoriteScreen.getChildren().add(disp);
        productDisplayMap.get(product.getProductId()).becameFavorite();
        db.favorites().add(product);
    }

    public void removeFavorite(Product product) {
        favoriteScreen.getChildren().remove(favoriteDisplayMap.get(product.getProductId()));
        productDisplayMap.get(product.getProductId()).removedFavorite();
        db.favorites().remove(product);
    }

    public void fillBackground(Pane pane, Label label) {
        Background background = new Background(new BackgroundFill(Color.NAVY, null, null));
        pane.setBackground(background);
        label.setTextFill(Color.WHITE);
    }

    public void stopFill(Pane pane, Label label) {
        Background background = new Background(new BackgroundFill(Color.WHITE, null, null));
        pane.setBackground(background);
        label.setTextFill(Color.BLACK);
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

    /* @FXML
    public void populateProductScreen() {
        for (Product p : db.favorites()) {
            favoriteScreen.getChildren().add(productDisplayMap.get(p.getProductId()));
        }
    }*/


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
                shoppingCartCurrentItems.setText(String.valueOf(db.getShoppingCart().getItems().size()));
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

    private void setSmall() {
        rect.setWidth(50);
        rect.setLayoutX(566);
        this.arrow.setText("<--");
        arrow.setPrefWidth(50);
        arrow.setLayoutX(566);
    }

    private void setLarge() {
        rect.setWidth(65);
        rect.setLayoutX(546);
        this.arrow.setText("<-----");
        arrow.setPrefWidth(80);
        arrow.setLayoutX(546);
    }

    Timeline getBeat() {
        if (beat == null) {
            this.beat = new Timeline(
                    new KeyFrame(Duration.ZERO, event -> setSmall()),
                    new KeyFrame(Duration.seconds(0.5), event -> setLarge()));
        }
        return this.beat;
    }

    public void pulseAnimation() {
        arrow.setPrefWidth(70);
        arrow.setLayoutX(566);
        beat.setAutoReverse(true);
        beat.setCycleCount(Timeline.INDEFINITE);
        beat.play();
    }

    @FXML
    public void onHoverRect() {
        getBeat();
        pulseAnimation();
    }

    @FXML
    public void rectHoverStopped() {
        setSmall();
        this.beat.stop();
    }

    @FXML
    public void closeProfilePicPane() {
        rect.setVisible(true);
        arrow.setVisible(true);
        profilePicPane.setVisible(false);
    }

    @FXML
    public void openProfilePicPane() {
        rect.setVisible(false);
        arrow.setVisible(false);
        this.beat.stop();
        setSmall();
        profilePicPane.setVisible(true);
    }

    @FXML
    public void openProfilePicPane1() {
        profilePicPane1.setVisible(true);
    }

    @FXML
    public void closeProfilePicPane1() {
        profilePicPane1.setVisible(false);
    }

    private void clearFills() {
        stopFill(startButton, startLabel);
        stopFill(fruitButton, fruitLabel);
        stopFill(meatButton, meatLabel);
        stopFill(dairyButton, dairyLabel);
        stopFill(pantryButton, pantryLabel);
        stopFill(drinksButton, drinksLabel);
    }

    private void setFillFromString(String s){
        switch (s){
            case "Frukt & Grönt":
               fillBackground(fruitButton, fruitLabel);
               break;
            case "Mejeri":
                fillBackground(dairyButton, dairyLabel);
                break;
            case "Kött & Fisk":
                fillBackground(meatButton, meatLabel);
                break;
            case "Skafferi":
                fillBackground(pantryButton, pantryLabel);
                break;
            case "Dryck":
                fillBackground(drinksButton, drinksLabel);
                break;

        }
    }


    public void displayCategory(List<Product> products) {
        clearHits();
        clearFills();
        updateSearchScreenLabel(products);
        delay(8, () -> breadCrumbPane.setLayoutX(searchScreenLabel.getLayoutBounds().getWidth() + 10));
        for (Product product : products) {
            hitsFlowPane.getChildren().add(productDisplayMap.get(product.getProductId()));
        }
        if(products.size() < 100){
            setFillFromString(categories.mainToString(products.get(0)));
        }
        home.toFront();

        searchScreen.toFront();
    }

    private void clearHits() {
        hitsFlowPane.getChildren().clear();
        breadCrumbPane.getChildren().clear();
        shoppingCartHolder.getChildren().clear();
        suggestionPane.setVisible(false);
    }

    @FXML
    public void startEditScreen() {
        editScreen.getChildren().add(new EditAccountScreen(this));
        editLabel.setDisable(true);
    }

    @FXML
    public void stopEditScreen() {
        updateAccountText();
        editScreen.getChildren().clear();
        editLabel.setDisable(false);
    }

    @FXML
    public void displayCatVeg() {
        displayCategory(categories.getVegCategoryProducts());
        fillBackground(fruitButton, fruitLabel);
    }

    @FXML
    public void displayCatMeat() {
        displayCategory(categories.getMeatCategoryProducts());
        fillBackground(meatButton, meatLabel);
    }

    @FXML
    public void displayCatDairy() {
        displayCategory(categories.getDairyCategoryProducts());
        fillBackground(dairyButton, dairyLabel);
    }

    @FXML
    public void displayCatDrinks() {
        displayCategory(categories.getDrinksCategoryProducts());
        fillBackground(drinksButton, drinksLabel);
    }

    @FXML
    public void displayCatPantry() {
        displayCategory(categories.getPantryCategoryProducts());
        fillBackground(pantryButton, pantryLabel);
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
    private void onHoverSkip() {
        onHover(skipLabel);
    }

    @FXML
    private void onHoverSkapa() {
        onHover(skapa);
    }

    @FXML
    private void onHoverSkapaStop() {
        onHoverStop(skapa);
    }

    @FXML
    private void skipClick() {
        firstTimeScreen.toBack();
    }

    @FXML
    private void goToCreateAccount() {
        if (!Objects.equals(firstTimeTextField.getText(), "")) {
            db.getCustomer().setLastName(firstTimeTextField.getText());
            lastName.setText(firstTimeTextField.getText());
            firstTimeTextField.getParent().toBack();
        } else {
            errorLabel.setVisible(true);
            delay(2000, () -> errorLabel.setVisible(false));
        }
    }

    @FXML
    private void createAccount() {
        setTextField(firstName);
        setTextField(lastName);
        setTextField(address);
        setTextField(postCode);
        setTextField(phoneNumber);
        setTextField(email);
    }

    private void setTextField(TextField textField) {
        if (!Objects.equals(textField.getText(), "")) {
            setTextFieldSwitch(textField.getId(), textField.getText());
            textField.toBack();
        } else {
            textField.toFront();
        }
    }

    private void setTextFieldSwitch(String id, String text) {
        switch (id) {
            case "firstName":
                db.getCustomer().setFirstName(text);
                break;
            case "lastName":
                db.getCustomer().setLastName(text);
                break;
            case "address":
                db.getCustomer().setPostAddress(text);
                db.getCustomer().setAddress(text);
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
    private void createAccountPressed() {
        createAccount();
        if (db.isCustomerComplete()) {
            firstTimeScreen.toBack();
            menuPane.toFront();
            updateAccountText();
            createdSuccessfully.setVisible(true);
            createdSuccessfully.toFront();
            delay(4000, () -> createdSuccessfully.setVisible(false));
            createdSuccessfully.toBack();
        } else {
            errorLabel.setVisible(true);
            delay(2000, () -> errorLabel.setVisible(false));
        }
    }

    private void updateAccountText() {
        userTextLabel.setText(db.getCustomer().getFirstName() + "\n\n" +
                db.getCustomer().getLastName() + "\n\n" + db.getCustomer().getAddress() + "\n\n" +
                db.getCustomer().getPhoneNumber() + "\n\n"
                + db.getCustomer().getEmail() + "\n\n" + db.getCustomer().getPostCode() + "\n\n");

        nameLabel.setText(db.getCustomer().getFirstName() + " " + db.getCustomer().getLastName());
    }

    @FXML
    AnchorPane profilePicPane;
    @FXML
    Label userTextLabel;

    @FXML
    public void pickOwl() {
        Image owl = new Image("/imat/resources/imgs/owl.png");
        profilePicOne.setImage(owl);
        profilePicTwo.setImage(owl);
        profilePicThree.setImage(owl);
    }

    @FXML
    public void pickMonkey() {
        Image monkey = new Image("/imat/resources/imgs/monkey.png");
        profilePicOne.setImage(monkey);
        profilePicTwo.setImage(monkey);
        profilePicThree.setImage(monkey);
    }

    @FXML
    public void pickPenguin() {
        Image penguin = new Image("/imat/resources/imgs/penguin.png");
        profilePicOne.setImage(penguin);
        profilePicTwo.setImage(penguin);
        profilePicThree.setImage(penguin);
    }

    @FXML
    public void search() {
        displaySearch(searchBar.getText());
    }

    public void displaySearch(String searched) {
        clearHits();

        List<Product> itemsFound = new ArrayList<>();

        for (Product p : db.getProducts()) {
            if (almostEqual(p.getName(), searched, false) || almostEqual(categories.subToString(p), searched, false) ||
                    almostEqual(categories.mainToString(p), searched, false)) {
                itemsFound.add(p);
            }
        }
        if (!itemsFound.isEmpty()) {
            for (Product p : itemsFound) {
                hitsFlowPane.getChildren().add(productDisplayMap.get(p.getProductId()));
            }
            searchScreenLabel.setText("Sökning " + searched + " gav:");
            setSuggestionLabel(searched);

        } else {
            searchScreenLabel.setText("Sökning på " + searched + " gav tyvärr inga resultat");
            setSuggestionLabel(searched);
        }
        home.toFront();
        searchScreen.toFront();
    }

    private void setSuggestionLabel(String searched) {
        if (!getSuggestion(searched).equals("")) {
            delay(8, () -> suggestionPane.setLayoutX(searchScreenLabel.getLayoutBounds().getWidth() + 30));
            suggestionPane.setVisible(true);
            suggestionLabel.setText(getSuggestion(searched));
        }
    }

    private boolean almostEqual(String one, String two, Boolean generous) {
        int equalChars = 0;
        if (generous) {
            equalChars = 1;
        }
        int shorter = Math.min(one.length(), two.length());
        for (int i = 0; i < shorter; i++) {
            if (one.toLowerCase().charAt(i) == two.toLowerCase().charAt(i)) {
                equalChars++;
            }
        }
        if (shorter < 4) {
            return equalChars == shorter;
        }
        if (shorter < 6) {
            return (shorter - 1) <= equalChars;
        } else {
            return (shorter - 2) <= equalChars;
        }
    }

    @FXML
    private void suggestionClicked() {
        System.out.println(suggestionLabel.getText());
        displaySearch(suggestionLabel.getText());
    }

    private String getSuggestion(String searched) {
        if (!findSuggestion(searched, categories.getAllSubStrings()).equals("")) {
            return findSuggestion(searched, categories.getAllSubStrings());
        }
        if (!findSuggestion(searched, categories.getAllMainStrings()).equals("")) {
            return findSuggestion(searched, categories.getAllMainStrings());
        }
        return "";
    }

    private String findSuggestion(String searched, String[] options) {
        if (searched.length() > 2) {
            for (String main : options) {
                if (Math.abs(searched.length() - main.length()) <= 3) {
                    if (almostEqual(main, searched, true) && !searched.equalsIgnoreCase(main)) {
                        return main;
                    }
                }
            }
        }
        return "";
    }

    public void toWizard() {
        wizardHolder.getChildren().add( Wizard.getInstance());
        wizardHolder.toFront();

    }
}


