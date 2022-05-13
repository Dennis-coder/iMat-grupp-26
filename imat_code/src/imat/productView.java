package imat;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;

import java.io.IOException;

public class productView extends AnchorPane {
    @FXML
    Label viewScreenPriceOne;
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

    IMatDataHandler db = IMatDataHandler.getInstance();

    iMatController parentController;
    Product product;

    public productView(Product product, iMatController parentController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("productView.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);


        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.parentController = parentController;
        this.product = product;

        init();
    }

    private void init() {
        populateProductViewScreen();
    }

    public void populateProductViewScreen(){
        viewScreenPicture.setImage(db.getFXImage(product, 400, 300));
        viewScreenPriceOne.setText(String.valueOf(product.getPrice()) + ":-");
        viewScreenPriceTwo.setText(String.valueOf(product.getPrice()) + "kr");
        viewScreenTitle.setText(product.getName());
        viewScreenUnit.setText(product.getUnit());
        viewScreenPriceUnit.setText(String.valueOf(product.getPrice()) + " " + product.getUnit());
        viewScreenAmountSelect.getItems().clear();
        initializeComboBox();
        if(product.isEcological()){
            viewScreenEkoPicture.setVisible(true);
        }
    }

    private void initializeComboBox() {
        viewScreenAmountSelect.getItems().addAll("1", "2", "3", "4", "5", "6");
        viewScreenAmountSelect.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                int currentVal = Integer.valueOf(newValue);
                viewScreenPriceTwo.setText(String.valueOf(product.getPrice() * currentVal) + ":-");
            }
        });
    }

    @FXML
    public void closeProductViewScreen(){
        parentController.closeProductViewScreen();
    }


}
