package imat;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class SwitchButton extends AnchorPane
{
    private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(true);

    public SwitchButton(Wizard wiz)
    {
        Label label = new Label();
        label.setPrefWidth(60);
        label.setPrefHeight(20);
        this.getChildren().add(label);
        label.setLayoutY(5);
        label.getStyleClass().add("switchButton");
        label.setStyle("-fx-background-color: rgba(180,185,180,0.75);");

        Button switchBtn = new Button();
        switchBtn.setPrefWidth(30);
        switchBtn.setPrefHeight(30);
        switchBtn.getStyleClass().add("switchButton");

        switchBtn.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent t)
            {
                switchedOn.set(!switchedOn.get());
            }
        });

        this.getChildren().add(switchBtn);

        switchedOn.addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean t, Boolean t1)
            {
                if (t1)
                {
                    switchBtn.setLayoutX(30);
                    wiz.switchButtonPressedOn();
                }
                else
                {
                    switchBtn.setLayoutX(0);
                    wiz.switchButtonPressedOff();
                }
            }
        });
        switchedOn.set(false);
    }

    public SimpleBooleanProperty switchOnProperty() { return switchedOn; }
}