package imat;

import imat.iMatController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;

import java.io.IOException;
import java.util.*;

public class paymentMethodCreate extends StackPane {
    @FXML
    ComboBox<String> typeComboOne;
    @FXML
    ComboBox<String> typeComboTwo;
    @FXML
    TextField numberTextField;
    @FXML
    TextField cvvTextField;
    @FXML
    TextField nameTextField;
    @FXML
    Button createButton;
    @FXML
    Label emptyLabel;
    @FXML
    AnchorPane displayScreen;
    @FXML
    AnchorPane createScreen;
    @FXML
    Label numberLabel;
    @FXML
    FlowPane paymentMethodPane;
    @FXML
    Label noTypeLabel;
    @FXML
    TextArea errorCreate;
    @FXML
    TextField dayField;
    @FXML
    TextField monthField;
    @FXML
    TextField yearField;

    List<paymentMethod> paymentMethods = new ArrayList<paymentMethod>();

    iMatController parentController;
    IMatDataHandler db = IMatDataHandler.getInstance();

    public paymentMethodCreate(iMatController controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmlFiles/paymentMethodCreate.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.parentController = controller;

        init();
    }

    private void init() {
        initializeComboBox();

    }

    @FXML
    private void addButtonPressed() {
        if (!numberTextField.getText().equals("") && !nameTextField.getText().equals("") && (cvvTextField.getText().length() == 3)
        && !dayField.getText().equals("") && !monthField.getText().equals("") && !yearField.getText().equals("")) {
            paymentMethodPane.getChildren().clear();
            paymentMethods.add(new paymentMethod(typeComboTwo.getValue(), nameTextField.getText(),
                    cvvTextField.getText(), numberTextField.getText(), dayField.getText(), monthField.getText(),
                    yearField.getText(),this));
            for (paymentMethod pm : paymentMethods) {
                paymentMethodPane.getChildren().add(pm);
            }
            cvvTextField.setText("");
            nameTextField.setText("");
            numberTextField.setText("");
            paymentMethodPane.getChildren().add(createButton);
            paymentMethodPane.getChildren().add(typeComboOne);
            displayScreen.toFront();
        } else {
            errorCreate.setVisible(true);
            iMatController.delay(3000, () -> errorCreate.setVisible(false));
        }
    }


    private void initializeComboBox() {
        typeComboOne.getItems().addAll("Visa", "MasterCard", "Annan");
        typeComboTwo.getItems().addAll("Visa", "MasterCard", "Annan");
    }

    @FXML
    public void createButtonPressed() {
        numberLabel.setText("Nummer:");
        if (typeComboOne.getValue() == null) {
            noTypeLabel.setVisible(true);
            iMatController.delay(2000, () -> noTypeLabel.setVisible(false));
            return;
        }
        typeComboTwo.setValue(typeComboOne.getValue());
        nameTextField.setText(db.getCustomer().getFirstName() + "s " + typeComboOne.getValue());
        createScreen.toFront();
    }

    public void remove(paymentMethod pm) {
        paymentMethods.remove(pm);
        paymentMethodPane.getChildren().remove(pm);
        if (paymentMethods.isEmpty())
            paymentMethodPane.getChildren().add(0, emptyLabel);
    }

    @FXML
    public void cancelCreate() {
        cvvTextField.setText("");
        numberLabel.setText("");
        displayScreen.toFront();
    }
}
