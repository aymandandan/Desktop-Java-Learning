package hellofxml;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {

    @FXML
    private Label helloLabel;

    @FXML
    protected void onHelloButtonClick() {
        helloLabel.setText("Button Clicked via FXML!");
    }
}
