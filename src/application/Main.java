package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("GeneratorWindow.fxml"));
			Scene scene = new Scene(root,640,466);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.setTitle("Password");
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}