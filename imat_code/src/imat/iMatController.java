
package imat;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;


public class iMatController implements Initializable {

    iMatBackendController iMatBackendController = new iMatBackendController();
    IMatDataHandler db = IMatDataHandler.getInstance();
    private Map<Integer, productDisplayItem> productDisplayMap = new HashMap<Integer, productDisplayItem>();
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for(Product product : db.getProducts()){
            productDisplayItem dispItem = new productDisplayItem(product, this);
            productDisplayMap.put(product.getProductId(), dispItem);
        }
        populateProductScreen();
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

    @FXML
    public void populateProductScreen(){
        for(Product p : db.getProducts()){
            productScreen.getChildren().add(productDisplayMap.get(p.getProductId()));
        }
    }


}