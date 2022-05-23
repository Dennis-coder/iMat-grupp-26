package imat;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import se.chalmers.cse.dat216.project.*;

import java.io.IOException;

public class productView extends AnchorPane {

    @FXML
    Label viewScreenPriceTwo;
    @FXML
    Label viewScreenPriceUnit;
    @FXML
    Label viewScreenUnit;
    @FXML
    Label viewScreenTitle;
    @FXML
    ImageView viewScreenPicture;
    @FXML
    ImageView viewScreenEkoPicture;
    @FXML
    ComboBox viewScreenAmountSelect;
    @FXML
    TextField addedItemsTextField;
    @FXML
    ImageView viewScreenHeart;
    @FXML
    FlowPane BreadCrumbPane;
    @FXML
    AnchorPane controlPane;
    @FXML
    Label inBasketLabel;


    IMatDataHandler db = IMatDataHandler.getInstance();
    categoryHandler ch = categoryHandler.getInstance();
    iMatController parentController;
    Product product;
    int currentVal;
    Timeline beat;

    SimpleDoubleProperty scale = new SimpleDoubleProperty(1);
    ShoppingCartListener scl = new ShoppingCartListener() {
        @Override
        public void shoppingCartChanged(CartEvent cartEvent) {
            for (ShoppingItem SI : db.getShoppingCart().getItems()) {
                if (SI.getProduct().equals(product)) {
                    inBasketLabel.setVisible(true);
                    inBasketLabel.setText(String.valueOf((int) SI.getAmount()) + "x");
                    return;
                }
            }
            inBasketLabel.setVisible(false);
        }
    };


    public productView(Product product, int currentVal, iMatController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmlFiles/productView.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        db.getShoppingCart().addShoppingCartListener(scl);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.parentController = parentController;
        this.product = product;
        this.currentVal = currentVal;
        populateProductViewScreen();
    }

    public void populateProductViewScreen() {
        viewScreenPicture.setImage(db.getFXImage(product, 400, 300));
        viewScreenPriceTwo.setText(String.format("%.2f", product.getPrice() * currentVal) + "kr");
        viewScreenTitle.setText(product.getName());
        viewScreenUnit.setText(product.getUnit());
        viewScreenPriceUnit.setText(String.format("%.2f", product.getPrice()) + " " + product.getUnit());
        viewScreenAmountSelect.getItems().clear();
        colorCategory();
        getBeat();
        initializeComboBox();
        setBreadCrumbs();
        if (product.isEcological()) {
            viewScreenEkoPicture.setVisible(true);
        }
        if (db.isFavorite(product)) {
            viewScreenHeart.setImage(new Image("imat/resources/imgs/heartFilled.png"));
        }
    }

    private void setBreadCrumbs() {
        BreadCrumbPane.getChildren().add(new BreadCrumb("Alla Produkter", false, parentController));
        if (product.getCategory() == ProductCategory.DAIRIES) {
            BreadCrumbPane.getChildren().add(new BreadCrumb(ch.mainToString(product), true, parentController));
            return;
        }
        BreadCrumbPane.getChildren().add(new BreadCrumb(ch.mainToString(product), false, parentController));
        BreadCrumbPane.getChildren().add(new BreadCrumb(ch.subToString(product), true, parentController));
    }


    private void initializeComboBox() {
        viewScreenAmountSelect.getItems().addAll("1", "2", "3", "4", "5", "6");
        viewScreenAmountSelect.getSelectionModel().select(String.valueOf(currentVal));
        viewScreenAmountSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                currentVal = Integer.valueOf(newValue);
                viewScreenPriceTwo.setText(String.format("%.2f", (product.getPrice() * currentVal)) + ":-");
            }
        });
    }

    @FXML
    public void closeProductViewScreen() {
        parentController.closeProductViewScreen();
    }

    @FXML
    public void addButtonPressed() throws InterruptedException {
        parentController.addItemToCart(product, currentVal);
        addedItemsTextField.setVisible(true);
        addedItemsTextField.toFront();
        iMatController.delay(1000, () -> addedItemsTextField.setVisible(false));
    }

    void getBeat() {
        this.beat = new Timeline(
                new KeyFrame(Duration.ZERO, event -> scale.setValue(1)),
                new KeyFrame(Duration.seconds(0.5), event -> scale.setValue(1.1)));
    }

    private void pulseAnimation() {
        viewScreenHeart.scaleYProperty().bind(scale);
        viewScreenHeart.scaleXProperty().bind(scale);
        beat.setAutoReverse(true);
        beat.setCycleCount(Timeline.INDEFINITE);
        beat.play();
    }

    @FXML
    public void heartHover() {
        if (db.isFavorite(product)) {
            viewScreenHeart.setImage(new Image("imat/resources/imgs/heart.png"));
        } else {
            viewScreenHeart.scaleYProperty().bind(scale);
            pulseAnimation();
        }
    }

    @FXML
    public void heartHoverStopped() {
        if (db.isFavorite(product)) {
            viewScreenHeart.setImage(new Image("imat/resources/imgs/heartFilled.png"));
        } else {
            scale.setValue(1);
            beat.stop();
        }
    }

    @FXML
    public void onClick() {
        if (db.isFavorite(product)) {
            viewScreenHeart.setImage(new Image("imat/resources/imgs/heart.png"));
            parentController.removeFavorite(product);
        } else {
            viewScreenHeart.setImage(new Image("imat/resources/imgs/heartFilled.png"));
            parentController.addFavorite(product);
            scale.setValue(1);
            beat.stop();
        }
    }

    private void colorCategory() {
        parentController.colorCategory(controlPane, product);
    }


}
