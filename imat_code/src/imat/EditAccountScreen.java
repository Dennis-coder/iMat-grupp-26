package imat;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Product;

import java.io.IOException;
import java.util.Objects;

public class EditAccountScreen extends AnchorPane {
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
    TextField Error;
    IMatDataHandler db;
    iMatController parentController;


    public EditAccountScreen(iMatController controller){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmlFiles/editAccountScreen.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);


        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        db = controller.db;
        this.parentController = controller;

        init();
    }

    private void init() {
        firstName.setText(parentController.db.getCustomer().getFirstName());
        lastName.setText(parentController.db.getCustomer().getLastName());
        address.setText(parentController.db.getCustomer().getAddress());
        postCode.setText(parentController.db.getCustomer().getPostCode());
        phoneNumber.setText(parentController.db.getCustomer().getPhoneNumber());
        email.setText(parentController.db.getCustomer().getEmail());
    }



    private void createAccount() {
        setTextField(firstName);
        setTextField(lastName);
        setTextField(address);
        setTextField(postCode);
        setTextField(phoneNumber);
        setTextField(email);
    }

    @FXML
    public void changeConfirmed(){
        if(!firstName.getText().equals("") && !lastName.getText().equals("") &&
                !address.getText().equals("") && !phoneNumber.getText().equals("") && !postCode.getText().equals("")
                && !email.getText().equals("")) {
            createAccount();
            parentController.stopEditScreen();
        }
        else {
            Error.setVisible(true);
            iMatController.delay(2000, ()-> Error.setVisible(false));
        }
    }

    @FXML
    public void closeField(){
        parentController.stopEditScreen();
    }

    private void setTextField(TextField textField) {
        setTextFieldSwitch(textField.getId(), textField.getText());
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


}
