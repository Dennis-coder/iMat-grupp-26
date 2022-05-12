package imat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;
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

    public productDisplayItem(Product product, iMatController controller){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("productDisplayItem.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);


        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.parentController = controller;
        this.product = product;

        init();
    }

    private void init(){
        title.setText(product.getName());
        prize.setText(String.valueOf(product.getPrice()));
        prizePerUnit.setText(String.valueOf(product.getPrice()) + "/" + product.getUnit());
    }


}
