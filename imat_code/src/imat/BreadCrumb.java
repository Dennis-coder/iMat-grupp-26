package imat;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;

public class BreadCrumb extends AnchorPane {
    String name;
    categoryHandler cH = categoryHandler.getInstance();

    @FXML
    Label label;

    iMatController parentController;

    public BreadCrumb(String name, Boolean last, iMatController parentController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmlFiles/BreadCrumb.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.name = name;
        this.parentController = parentController;
        setText(last);
    }

    private void setText(Boolean last){
        if (!last){
            label.setText(name + " -> ");
        }
        else {
            label.setText(name);
        }
    }

    @FXML
    public void onHover(){
        label.setUnderline(true);
        label.setTextFill(Color.gray(0.3));
    }

    @FXML
    public void onHoverStopped(){
        label.setUnderline(false);
        label.setTextFill(Color.BLACK);
    }

    @FXML
    public void onClick(){
        parentController.displayCategory(cH.getProductsFromName(name));
    }

}
