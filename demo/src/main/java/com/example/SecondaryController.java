package com.example;

import java.io.IOException;
import javafx.fxml.FXML;

// for the snake game, this could be where all the high scores are presented

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}