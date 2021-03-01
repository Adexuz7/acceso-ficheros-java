package aed.files.app;

import aed.files.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
	
	private Controller controller;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		controller = new Controller();

		Scene scene = new Scene(controller.getView());
		primaryStage.setTitle("Acceso a ficheros");
		primaryStage.setScene(scene);
		primaryStage.show();	
	}

}
