package imat;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import se.chalmers.cse.dat216.project.IMatDataHandler;
import se.chalmers.cse.dat216.project.Order;
import se.chalmers.cse.dat216.project.ShoppingItem;

import java.text.SimpleDateFormat;

public class OrderListItem extends AnchorPane {
    Order order;
    Label nmbrLabel;
    Label dateLabel;
    Label itemsLabel;
    Button expandButton;
    Button minimizeButton;
    ScrollPane scrollPane = new ScrollPane();
    FlowPane flowPane = new FlowPane();
    IMatDataHandler db = IMatDataHandler.getInstance();


    public OrderListItem(Order order) {
        this.order = order;
        this.setPrefWidth(300);
        this.setPrefHeight(100);
        this.getStyleClass().add("navButton");
        this.getStyleClass().add("whitePane");
        setLabels();
        addLabels();
        addExpandButton();
        setMinimizeButton();
        setFlowPane();
        setScrollPane();
        populateFlowPane();
    }

    private void setLabels() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String format = formatter.format(order.getDate());
        this.nmbrLabel = new Label("Order " + order.getOrderNumber());
        this.dateLabel = new Label("Levereras: " + format);
        this.itemsLabel = new Label(String.valueOf(order.getItems().size()) + " varor");
        nmbrLabel.getStyleClass().add("orderText");
        dateLabel.getStyleClass().add("conclusionCheckBox");
        itemsLabel.getStyleClass().add("orderText");
    }

    private void addLabels() {
        this.getChildren().add(nmbrLabel);
        this.getChildren().add(dateLabel);
        this.getChildren().add(itemsLabel);
        this.nmbrLabel.setLayoutX(10);
        this.dateLabel.setLayoutX(150);
        this.itemsLabel.setLayoutX(10);
        this.itemsLabel.setLayoutY(60);
    }

    private void addExpandButton() {
        this.expandButton = new Button();
        this.expandButton.setText("Visa mer");
        this.expandButton.getStyleClass().add("navButton");
        this.expandButton.setPrefHeight(20);
        this.expandButton.setLayoutX(250);
        this.expandButton.setLayoutY(60);
        this.expandButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                onExpand();
            }
        });
        this.getChildren().add(expandButton);
    }

    private void setMinimizeButton() {
        this.minimizeButton = new Button();
        this.minimizeButton.setText("Visa mindre");
        this.minimizeButton.getStyleClass().add("navButton");
        this.minimizeButton.setPrefHeight(20);
        this.minimizeButton.setLayoutX(240);
        this.minimizeButton.setLayoutY(60);
        this.minimizeButton.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                onMinimize();
            }
        });
    }

    private void setScrollPane() {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setContent(flowPane);
        scrollPane.setPannable(true);
        scrollPane.setMaxHeight(250);
        scrollPane.setPrefWidth(280);
        scrollPane.setLayoutY(90);
        scrollPane.setLayoutX(10);
    }

    private void setFlowPane() {
        flowPane.setOrientation(Orientation.VERTICAL);
        flowPane.setPrefWrapLength(5000);
    }

    private void onMinimize() {
        this.getChildren().remove(scrollPane);
        this.getChildren().add(expandButton);
        this.getChildren().remove(minimizeButton);

    }

    private void onExpand() {
        this.getChildren().add(scrollPane);
        this.getChildren().remove(expandButton);
        this.getChildren().add(minimizeButton);
    }

    private void populateFlowPane() {
        for (ShoppingItem SI : order.getItems()) {
            flowPane.getChildren().add(displayItem(SI));
        }
    }

    private AnchorPane displayItem(ShoppingItem SI) {
        Label Title = new Label(SI.getProduct().getName());
        ImageView img = new ImageView(db.getFXImage(SI.getProduct()));
        Title.getStyleClass().add("conclusionText");
        Label Amount = new Label("Antal: " + (int) SI.getAmount());
        Amount.getStyleClass().add("conclusionTextSmall");
        Title.setLayoutX(10);
        Title.setLayoutY(10);
        Amount.setLayoutY(50);
        Amount.setLayoutX(10);
        img.setPreserveRatio(true);
        img.setFitHeight(70);
        img.setFitWidth(70);
        img.setLayoutY(15);
        img.setLayoutX(160);
        AnchorPane card = new AnchorPane(Title, img, Amount);
        card.setPrefHeight(90);
        card.setPrefWidth(280);
        return card;
    }
}