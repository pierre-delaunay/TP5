package delaunay.diabat.tp5;

import delaunay.diabat.tp5.view.ControleurPrincipal;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

public class MainTP5 extends Application {

	@Override
	public void start(Stage primaryStage) {
		try
		{			
            primaryStage.setTitle("TP5 Delaunay - Diabat");
            Image icone = new Image("file:images/istic.png");
            primaryStage.getIcons().add(icone);
            
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainTP5.class.getResource("view/VuePrincipale.fxml"));
            BorderPane root = loader.load();
            ControleurPrincipal rootController = loader.getController();

            FXMLLoader l = new FXMLLoader();
            l.setLocation(MainTP5.class.getResource("view/VueTP5.fxml"));
            BorderPane grille = l.load();

            rootController.setControleur(l.getController());

            root.setCenter(grille);

            Scene scene = new Scene(root, 550, 650);
            
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
