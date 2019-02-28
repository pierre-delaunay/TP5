package delaunay.diabat.tp5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class MainTP5 extends Application {
	@Override
	public void start(Stage primaryStage) {
		try
		{
	        primaryStage.setTitle("TP5 binome1-binome2");
			FXMLLoader loader = new FXMLLoader() ;
            loader.setLocation(MainTP5.class.getResource("VueTP5.fxml"));
            Parent root = (Parent) loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
