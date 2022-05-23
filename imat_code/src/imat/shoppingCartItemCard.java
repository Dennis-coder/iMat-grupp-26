package imat;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
import javafx.scene.image.ImageView;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.io.IOException;

public class shoppingCartItemCard extends AnchorPane {
    @FXML
    ImageView picture;
    @FXML
    Label title;
    @FXML
    Label amount;
    @FXML
    ImageView ekoImg;
    @FXML
    Circle circlePlus;
    @FXML
    Circle circleMinus;
    @FXML
    AnchorPane removePromptPane;
    @FXML
    Pane pictureLightPane;
    @FXML
    ImageView cross;

    iMatController parentController;
    Product product;
    IMatDataHandler db = IMatDataHandler.getInstance();
    DoubleProperty scalePlus = new SimpleDoubleProperty(1);
    DoubleProperty scaleMinus = new SimpleDoubleProperty(1);
    DoubleProperty scaleCross = new SimpleDoubleProperty(1);


    public shoppingCartItemCard(Product p, iMatController controller){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmlFiles/shoppingCartItemCard.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.parentController = controller;
        this.product = p;
        init();

    }

    private void init(){
        this.picture.setImage(db.getFXImage(product, 300, 240));
        this.title.setText(product.getName());
        if(product.isEcological()){
            ekoImg.setVisible(true);
        }
        bindScales();
    }

    private void bindScales(){
        circlePlus.scaleXProperty().bind(scalePlus);
        circlePlus.scaleYProperty().bind(scalePlus);
        circleMinus.scaleXProperty().bind(scaleMinus);
        circleMinus.scaleYProperty().bind(scaleMinus);
        cross.scaleXProperty().bind(scaleCross);
        cross.scaleYProperty().bind(scaleCross);

    }

    @FXML
    public void onHoverPlus(){
        scalePlus.setValue(1.1);
    }

    @FXML
    public void onHoverMinus(){
        scaleMinus.setValue(1.1);
    }

    @FXML
    public void onHoverCross(){
        scaleCross.setValue(1.1);
    }

    @FXML
    public void hoverExitedPlus(){
        scalePlus.setValue(1);
    }

    @FXML
    public void hoverExitedMinus(){
        scaleMinus.setValue(1);
    }

    @FXML
    public void hoverExitedCross(){
        scaleCross.setValue(1);
    }

    @FXML
    public void onClickMinus(){
        for (ShoppingItem SI :db.getShoppingCart().getItems()){
            if (SI.getProduct().equals(product)){
                if(SI.getAmount() == 1){
                    removePrompt();
                }
                else{
                    SI.setAmount(SI.getAmount() - 1);
                    db.getShoppingCart().fireShoppingCartChanged(SI, true);
                }
                break;
            }
        }
    }

    @FXML
    public void onClickPlus() {
        for (ShoppingItem SI : db.getShoppingCart().getItems()) {
            if (SI.getProduct().equals(product)) {
                SI.setAmount(SI.getAmount() + 1);
                db.getShoppingCart().fireShoppingCartChanged(SI, true);
                break;
            }
        }
    }

    @FXML
    public void removePrompt(){
        removePromptPane.setVisible(true);
        removePromptPane.toFront();
    }

    @FXML
    public void confirmRemoval(){
        removePromptPane.setVisible(false);
        removePromptPane.toBack();
        parentController.removeItemFromCart(product);
    }

    @FXML
    public void cancelRemoval(){
        removePromptPane.setVisible(false);
        removePromptPane.toBack();
    }

    @FXML
    public void onHoverClickableArea(){
        pictureLightPane.setVisible(true);
        title.setUnderline(true);
    }

    @FXML
    public void hoverStoppedClickableArea(){
        pictureLightPane.setVisible(false);
        title.setUnderline(false);
    }

    @FXML
    public void onClickProduct(){
        shoppingCartScreen screenSC = shoppingCartScreen.getInstance(parentController);
        screenSC.itemInCartView.getChildren().clear();
        screenSC.itemInCartView.getChildren().add(
                new productView(product, Integer.parseInt(amount.getText()), parentController));
        screenSC.singltemAnchorPane.toFront();
    }
}
