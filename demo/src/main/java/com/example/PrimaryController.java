package com.example;

import java.io.IOException;
import javafx.fxml.FXML;

// for the snake game, this could be where the main game is played

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
