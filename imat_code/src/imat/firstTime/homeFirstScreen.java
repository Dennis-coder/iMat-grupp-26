package imat.firstTime;

import imat.iMatController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;

import java.io.IOException;

public class homeFirstScreen extends StackPane {
    @FXML
    AnchorPane choseYesPane;
    @FXML
    AnchorPane firstTimePane;
    @FXML
    AnchorPane notFirstPane;
    @FXML
    AnchorPane introPane;
    @FXML
    AnchorPane introPaneTwo;
    @FXML
    AnchorPane introPaneLast;
    @FXML
    AnchorPane itemsLeftPane;

    IMatDataHandler db = IMatDataHandler.getInstance();
    iMatController parentController;

    public homeFirstScreen(iMatController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("firstTimeScreen.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.parentController = parentController;
        if (db.getShoppingCart().getItems().size() == 0) {
            firstTimePane.toFront();
        } else {
            itemsLeftPane.toFront();
        }
    }

    @FXML
    public void clickedYesFirst() {
        choseYesPane.setVisible(true);
    }

    @FXML
    public void clickedNoFirst() {
        notFirstPane.toFront();
    }

    @FXML
    public void clickedYesShow() {
        parentController.showIntro();
        choseYesPane.setVisible(false);
        introPane.toFront();
    }

    @FXML
    public void clickedNoShow() {
        choseYesPane.setVisible(false);
        notFirstPane.toFront();
    }

    @FXML
    public void toIntroOne() {
        introPane.toFront();
        this.parentController.showIntroOne();
    }

    @FXML
    public void toIntroTwo() {
        introPaneTwo.toFront();
        this.parentController.showIntroTwo();
    }

    @FXML
    public void toIntroLast() {
        introPaneLast.toFront();
        this.parentController.showIntroLast();
    }

    @FXML
    public void stopIntro() {
        notFirstPane.toFront();
        parentController.stopIntro();
    }

    @FXML
    public void startShop() {
        this.parentController.displayAll();
    }

    @FXML
    public void clearCart() {
        db.getShoppingCart().clear();
        notFirstPane.toFront();
    }
}
