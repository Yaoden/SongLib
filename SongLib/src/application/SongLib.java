package application;
//Matthew Ya, Taehee Lee	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import view.SongLibController;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;


public class SongLib extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();   
			loader.setLocation(getClass().getResource("/view/SongLibUI.fxml"));
			AnchorPane root = (AnchorPane)loader.load();

			SongLibController listController = loader.getController();
			listController.start(primaryStage);

			Scene scene = new Scene(root, root.getPrefWidth(), root.getPrefHeight());
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	//Testing commit from my Taehee Lee Compooper!!!
	public static void main(String[] args) {
		launch(args);
	}
}
