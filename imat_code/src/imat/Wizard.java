package imat;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Order;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class Wizard extends StackPane {
    @FXML
    AnchorPane stepOne;
    @FXML
    TextField firstName;
    @FXML
    TextField lastName;
    @FXML
    TextField postCode;
    @FXML
    TextField address;
    @FXML
    TextField phone;
    @FXML
    TextField email;

    @FXML
    Label editAccountLabel;
    @FXML
    Button confirmChangeButton;
    @FXML
    Button cancelButton;
    @FXML
    Label createErrorMsg;
    @FXML
    FlowPane calenderPane;
    @FXML
    Label dateDisplay;
    @FXML
    Label startLabel;
    @FXML
    Label chooseDateLabel;

    @FXML
    Button toScreenTwoOne;
    @FXML
    Button toScreenTwoTwo;
    @FXML
    Circle circleTwoOne;
    @FXML
    Label circleTwoOneText;
    @FXML
    Circle circleThreeOne;
    @FXML
    Label circleThreeOneText;
    @FXML
    Circle circleOneOne;

    @FXML
    AnchorPane stepTwo;
    @FXML
    ComboBox<String> paymentCombo;
    @FXML
    TextField numberTextField;
    @FXML
    TextField cvvTextField;
    @FXML
    Label totalLabel;
    @FXML
    Label rememberCardLabel;
    @FXML
    CheckBox rememberCardCheck;
    @FXML
    Button addButton;
    @FXML
    Button regretButton;
    @FXML
    AnchorPane typeSelect;
    @FXML
    Label addLabel;
    @FXML
    AnchorPane switchPane;
    @FXML
    AnchorPane switchButton;
    @FXML
    Button toScreenThreeOne;
    @FXML
    Button toScreenThreeTwo;
    @FXML
    Label newCardLabel;
    @FXML
    Label newCardCross;
    @FXML
    Label fakturaLabel;
    @FXML
    Label directPayLabel;
    @FXML
    Circle circleThreeTwo;
    @FXML
    Label circleThreeTwoText;
    @FXML
    Label dateLabel;
    @FXML
    Circle circleTwoTwo;
    @FXML
    TextField dayField;
    @FXML
    TextField monthField;
    @FXML
    TextField yearField;
    @FXML
    Button confirmAndPayButton;
    @FXML
    AnchorPane confirmPaymentPane;


    @FXML
    AnchorPane stepThree;
    @FXML
    FlowPane overviewPane;
    @FXML
    Label totalLabelTwo;
    @FXML
    Circle circleThreeThree;

    @FXML
    AnchorPane orderFinished;
    @FXML
            FlowPane orderPane;

    Order order;
    LocalDate deliveryDate;
    String type;
    paymentMethod paymentMethod;


    IMatDataHandler db = IMatDataHandler.getInstance();
    iMatController parentController;



    public Wizard(iMatController parentController) {
        init(parentController);
    }


    private void setup(iMatController parentController) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmlFiles/purchaseWizard.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setRoot(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.parentController = parentController;
        circleTwoTwo.setEffect(circleThreeThree.getEffect());
        circleOneOne.setEffect(circleThreeThree.getEffect());
    }

    private void init(iMatController parentController) {
        setup(parentController);
        getSwitchBtn();
        createCalender();
        update();
    }

    private void getSwitchBtn(){
        switchButton = new SwitchButton(this);
        stepTwo.getChildren().add(switchButton);

        switchButton.setLayoutX(516);
        switchButton.setLayoutY(234);
    }


    void update() {
        totalLabel.setText(String.format("%.2f", db.getShoppingCart().getTotal() + 139) + ":-");
        paymentCombo.getItems().clear();
        for (paymentMethod pm : parentController.PMC.paymentMethods) {
            paymentCombo.getItems().add(pm.name.getText());
        }
        updateCirclesOne();
        stepOne.toFront();
        if (db.isCustomerComplete()) {
            starting();
        }
    }

    private void starting() {
        updateLabels();
        startLabel.setVisible(false);
        chooseDateLabel.setVisible(true);
        calenderPane.setVisible(true);
        editCustomerEnable(false);
    }

    @FXML
    public void toHomePage() {
        parentController.toHomepage();
    }


    // ----------------- STEP ONE -------------- //

    private void updateCirclesOne() {
        if (paymentMethod == null) {
            circleTwoOne.setFill(Color.WHITE);
            circleThreeOne.setFill(Color.WHITE);
            circleThreeOneText.setDisable(true);
        } else {
            circleTwoOne.setFill(new Color(0.7, 0.7, 0.7, 0.75));
            circleThreeOne.setFill(new Color(0.7, 0.7, 0.7, 0.75));
            circleThreeOneText.setDisable(false);
        }
    }


    private void updateLabels() {
        firstName.setText(db.getCustomer().getFirstName());
        lastName.setText(db.getCustomer().getLastName());
        address.setText(db.getCustomer().getAddress());
        email.setText(db.getCustomer().getEmail());
        postCode.setText(db.getCustomer().getPostCode());
        phone.setText(db.getCustomer().getPhoneNumber());
    }

    private void updateCustomer() {
        db.getCustomer().setFirstName(firstName.getText());
        db.getCustomer().setLastName(lastName.getText());
        db.getCustomer().setAddress(address.getText());
        db.getCustomer().setPostAddress(address.getText());
        db.getCustomer().setEmail(email.getText());
        db.getCustomer().setPostCode(postCode.getText());
        db.getCustomer().setPhoneNumber(phone.getText());
    }

    @FXML
    private void editCustomerEnable(Boolean b) {
        firstName.setDisable(!b);
        lastName.setDisable(!b);
        address.setDisable(!b);
        phone.setDisable(!b);
        postCode.setDisable(!b);
        email.setDisable(!b);
        confirmChangeButton.setVisible(b);
        cancelButton.setVisible(b);
        editAccountLabel.setVisible(!b);
    }

    @FXML
    private void editAccountStart() {
        toScreenTwoOne.setDisable(true);
        toScreenTwoTwo.setDisable(true);
        editCustomerEnable(true);
    }

    @FXML
    private void editAccountStop() {
        doneWithFirst(deliveryDate);
        editCustomerEnable(false);
        updateLabels();
    }

    @FXML
    private void confirmChange() {
        if (!firstName.getText().equals("") && !lastName.getText().equals("") && !address.getText().equals("") &&
                !postCode.getText().equals("") && !email.getText().equals("") && !phone.getText().equals("")) {
            updateCustomer();
            updateLabels();
            startLabel.setVisible(false);
            chooseDateLabel.setVisible(true);
            editCustomerEnable(false);
            doneWithFirst(deliveryDate);
            calenderPane.setVisible(true);
        } else {
            createErrorMsg.setVisible(true);
            iMatController.delay(2000, () -> createErrorMsg.setVisible(false));
        }
    }

    public void doneWithFirst(LocalDate deliveryDate) {
        if (deliveryDate != null) {
            if (db.isCustomerComplete()) {
                this.deliveryDate = deliveryDate;
                toScreenTwoOne.setDisable(false);
                toScreenTwoTwo.setDisable(false);
                circleTwoOneText.setDisable(false);
            }
            dateDisplay.setText(deliveryDate.toString());
        }
    }

    private void createCalender() {
        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.valueProperty().addListener((observable, oldValue, newValue)
                -> doneWithFirst(newValue));
        DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);
        Node popupContent = datePickerSkin.getPopupContent();
        calenderPane.getChildren().add(popupContent);

    }


    //----------- STEP TWO -------------//

    public void switchButtonPressedOn() {
        paymentCombo.setDisable(true);
        newCardLabel.setDisable(true);
        newCardCross.setDisable(true);
        paymentMethod = new paymentMethod("Faktura", "Faktura", "", "", "",
                null, null, parentController.PMC);
        isDoneStepTwo();
        fakturaLabel.getStyleClass().remove("choiceLabelOff");
        directPayLabel.getStyleClass().remove("choiceLabelOn");
        fakturaLabel.getStyleClass().add("choiceLabelOn");
        directPayLabel.getStyleClass().add("choiceLabelOff");

    }

    public void switchButtonPressedOff() {
        paymentCombo.setDisable(false);
        newCardLabel.setDisable(false);
        newCardCross.setDisable(false);
        if (!Objects.equals(paymentCombo.getValue(), null)) {
            chosePayment();
            isDoneStepTwo();
        } else {
            notDoneStepTwo();
        }
        fakturaLabel.getStyleClass().remove("choiceLabelOn");
        directPayLabel.getStyleClass().remove("choiceLabelOff");
        fakturaLabel.getStyleClass().add("choiceLabelOff");
        directPayLabel.getStyleClass().add("choiceLabelOn");
    }

    @FXML
    public void chosePayment() {
        for (paymentMethod pm : parentController.PMC.paymentMethods) {
            if (Objects.equals(paymentCombo.getSelectionModel().getSelectedItem(), pm.name.getText())) {
                cvvTextField.setText(pm.cvvNum);
                numberTextField.setText(pm.number.getText());
                dayField.setText(pm.day);
                monthField.setText(pm.month);
                yearField.setText(pm.year);
                dayField.setText(pm.day);
                monthField.setText(pm.month);
                yearField.setText(pm.year);
                paymentMethod = pm;
                isDoneStepTwo();
            }
        }
    }

    @FXML
    public void newCard() {
        typeSelect.setVisible(true);
        switchButton.setDisable(true);
        toScreenThreeTwo.setDisable(true);
        toScreenThreeOne.setDisable(true);
        numberTextField.setText("");
        cvvTextField.setText("");
        dayField.setText("");
        monthField.setText("");
        yearField.setText("");
    }

    private void choseType() {
        dayField.setDisable(false);
        monthField.setDisable(false);
        yearField.setDisable(false);
        cvvTextField.setDisable(false);
        numberTextField.setDisable(false);
        paymentCombo.setDisable(true);
        rememberCardCheck.setVisible(true);
        rememberCardLabel.setVisible(true);
        addButton.setVisible(true);
        regretButton.setVisible(true);
        typeSelect.setVisible(false);
    }

    @FXML
    public void stopAddCard() {
        dayField.setDisable(true);
        monthField.setDisable(true);
        yearField.setDisable(true);
        cvvTextField.setDisable(true);
        numberTextField.setDisable(true);
        paymentCombo.setDisable(false);
        rememberCardCheck.setVisible(false);
        rememberCardLabel.setVisible(false);
        addButton.setVisible(false);
        regretButton.setVisible(false);
        switchButton.setDisable(false);
        numberTextField.setText("");
        cvvTextField.setText("");
        dayField.setText("");
        monthField.setText("");
        yearField.setText("");
    }

    @FXML
    public void onClickMaster() {
        type = "MasterCard";
        choseType();
    }

    @FXML
    public void onClickVisa() {
        type = "Visa";
        choseType();
    }

    @FXML
    public void onClickOther() {
        type = "Annan";
        choseType();
    }

    private String getCardName(){
        if(this.type.equals("Annan")){
            return (firstName.getText() + "s kort");
        }
        return (firstName.getText() + "s " + type);
    }

    @FXML
    public void addCard() {
        if (!numberTextField.getText().equals("") && !dayField.getText().equals("") &&
                !monthField.getText().equals("") && !yearField.getText().equals("") &&
                (cvvTextField.getText().length() == 3)) {
            paymentMethod = new paymentMethod(type, getCardName(), cvvTextField.getText(),
                    numberTextField.getText(), dayField.getText(), monthField.getText(), yearField.getText(), parentController.PMC);
            if (rememberCardCheck.isSelected()) {
                parentController.PMC.paymentMethods.add(paymentMethod);
                updateCombo();
            }
            chosePayment();
            addLabel.setText("Kortet har lagts till, du kan nu gå vidare");
            numberTextField.setText("");
            cvvTextField.setText("");
            dayField.setText("");
            monthField.setText("");
            yearField.setText("");
            stopAddCard();
        } else {
            addLabel.setText("Vänligen fyll i alla fält, CVV är begränsat till 3 tecken");
        }
        addLabel.setVisible(true);
        iMatController.delay(3000, () -> addLabel.setVisible(false));
    }

    private void updateCombo() {
        paymentCombo.setValue(paymentMethod.name.getText());
        paymentCombo.getItems().add(paymentMethod.name.getText());
    }

    private void isDoneStepTwo() {
        toScreenThreeOne.setDisable(false);
        toScreenThreeTwo.setDisable(false);
        updateCirclesTwo();
    }

    private void updateCirclesTwo() {
        if (paymentMethod == null) {
            circleThreeTwo.setFill(Color.WHITE);
            circleThreeTwoText.setDisable(true);
        } else {
            circleThreeTwo.setFill(new Color(0.7, 0.7, 0.7, 0.75));
            circleThreeTwoText.setDisable(false);
        }
    }

    private void notDoneStepTwo() {
        toScreenThreeOne.setDisable(true);
        toScreenThreeTwo.setDisable(true);
        paymentMethod = null;
        updateCirclesTwo();
    }


    // ----------------------- STEP 3 ----------------- //

    private Label makeLabel(String string, String style){
        Label label = new Label(string);
        label.getStyleClass().add(style);
        return label;
    }
    private void addLabel(String string, String style) {
        overviewPane.getChildren().add(makeLabel(string, style));
    }

    private void addTimeLabel(){
        FlowPane fp = new FlowPane();
        fp.setPrefHeight(60);
        fp.setOrientation(Orientation.VERTICAL);
        fp.getChildren().add(makeLabel("Levereras till address: ", "conclusionText"));
        fp.getChildren().add(makeLabel(address.getText() + "\s kl: 14:00-17:00", "conclusionTextSmall"));
        overviewPane.getChildren().add(fp);
    }

    private void addCheckBox(String string, boolean on) {
        CheckBox ch = new CheckBox();
        ch.setSelected(on);
        ch.setText(string);
        ch.getStyleClass().add("conclusionCheckBox");
        ch.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        overviewPane.getChildren().add(ch);
    }

    private void updateOverview() {
        overviewPane.getChildren().clear();
        addLabel("Din order:", "orderNumber");
        addLabel("Leveransdatum: " + deliveryDate.toString(), "conclusionText");
        addTimeLabel();
        addLabel("""
                Om vi bemöter några hinder angående din beställning \s 
                så vill jag helst bli kontaktad via:""", "conclusionTextSmall");
        addCheckBox("Via E-mail: " + email.getText(), true);
        addCheckBox("Via telefon: " + phone.getText(), false);
        paymentText();
        totalLabelTwo.setText("Totalt : " + String.format("%.2f", db.getShoppingCart().getTotal()+ 139) + ":-");
    }

    private String getLatestDate() {
        String date;
        int month = this.deliveryDate.getMonthValue();
        if (this.deliveryDate.getDayOfMonth() > 30) {
            date = (deliveryDate.getDayOfMonth() - 16) + "/" + (month + 1) + "-" + deliveryDate.getYear();
            return date;
        }
        date = (deliveryDate.getDayOfMonth()) + "/" + (month) + "-" + deliveryDate.getYear();
        return date;
    }

    private void paymentText() {
        String latestDate = this.getLatestDate();
        if (Objects.equals(this.paymentMethod.name.getText(), "Faktura")) {
            addLabel("Betalas via faktura", "conclusionText");
            addLabel("Skickas med beställning", "conclusionText");
            addLabel("Betalas senast: " + latestDate, "conclusionText");
        } else {
            addLabel("Betalas med: " + this.paymentMethod.name.getText(), "conclusionText");
            addLabel("Kortnummer: " + numberTextField.getText(), "conclusionText");
            addLabel("Säkerhetskod: " + paymentMethod.cvvNum, "conclusionText");
            addLabel("Utgångsdatum: " + paymentMethod.date, "conclusionText");
        }
    }

    @FXML
    public void payOrder() {
        confirmPaymentPane.setVisible(true);
        confirmPaymentPane.toFront();
    }

    @FXML
    public void cancelPay() {
        confirmPaymentPane.setVisible(false);
        confirmPaymentPane.toBack();
    }


    // --------------------- TRAVEL ------------------ //
    @FXML
    public void toStepOne() {
        updateCirclesOne();
        stepOne.toFront();
    }

    @FXML
    public void toScreenTwo() {
        stepTwo.toFront();
        dateLabel.setText("Leveransdatum: " + deliveryDate.toString());
    }

    @FXML
    public void toScreenThree() {
        updateOverview();
        stepThree.toFront();
    }

    @FXML
    public void orderConfirmed() throws ParseException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(deliveryDate.toString());
        this.order = db.placeOrder(false);
        order.setDate(date);
        orderFinished.toFront();
        orderPane.getChildren().add(new OrderListItem(order));
    }

    @FXML
    public void finishOrder(){
        parentController.wizardHolder.getChildren().clear();
        toHomePage();
    }

    @FXML
    public void shutDown(){
        db.shutDown();
        Platform.exit();
    }
    //add user add getstartedquare addorder format Strings
}
