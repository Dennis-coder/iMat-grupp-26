
package imat;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class iMatController implements Initializable {

    iMatBackendController iMatBackendController = new iMatBackendController();
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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


}