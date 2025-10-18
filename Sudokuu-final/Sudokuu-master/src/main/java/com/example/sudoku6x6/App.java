package com.example.sudoku6x6;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/sudoku6x6/menu_main.fxml")));
		primaryStage.setTitle("Menú Principal - Sudoku 6x6");
		primaryStage.setScene(new Scene(root, 700, 600));
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

